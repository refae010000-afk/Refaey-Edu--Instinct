package com.example.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.GeomagneticField
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.display.DisplayManager
import android.location.Location
import android.view.Display
import android.view.Surface
import androidx.core.content.ContextCompat
import com.example.data.PilgrimContent
import com.example.model.CompassAccuracy
import com.example.model.CompassReading
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class GeoPoint(val lat: Double, val lng: Double)

data class SensorOrientation(
    val azimuthDeg: Float,
    val pitchDeg: Float,
    val rollDeg: Float
)

object GeoMath {
    const val KAABA_LAT = 21.4225
    const val KAABA_LNG = 39.8262

    fun distanceMeters(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val r = 6371000.0
        val dLat = (lat2 - lat1) * PI / 180.0
        val dLng = (lng2 - lng1) * PI / 180.0
        val a = sin(dLat / 2).pow(2) +
            cos(lat1 * PI / 180.0) * cos(lat2 * PI / 180.0) * sin(dLng / 2).pow(2)
        return r * 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
    }

    fun qiblaBearingDegrees(lat: Double, lng: Double): Double {
        val l1 = lat * PI / 180.0
        val n1 = lng * PI / 180.0
        val l2 = KAABA_LAT * PI / 180.0
        val n2 = KAABA_LNG * PI / 180.0
        val y = sin(n2 - n1) * cos(l2)
        val x = cos(l1) * sin(l2) - sin(l1) * cos(l2) * cos(n2 - n1)
        return ((atan2(y, x) * 180.0 / PI) + 360.0) % 360.0
    }

    fun nearestMiqat(lat: Double, lng: Double): Pair<String, Double> {
        var bestName = PilgrimContent.miqats.first().name
        var minKm = Double.MAX_VALUE
        for (m in PilgrimContent.miqats) {
            val km = distanceMeters(lat, lng, m.lat, m.lng) / 1000.0
            if (km < minKm) {
                minKm = km
                bestName = m.name
            }
        }
        return bestName to minKm
    }
}

object CompassMath {
    const val QIBLA_ALIGNMENT_TOLERANCE_DEG = 5f
    const val MIN_VALID_MAGNETIC_UT = 15f
    const val MAX_VALID_MAGNETIC_UT = 85f

    /**
     * Exponential low-pass filter to smooth noisy accelerometer & magnetometer vectors.
     */
    fun lowPassFilter(input: FloatArray, output: FloatArray, alpha: Float): FloatArray {
        val len = minOf(input.size, output.size)
        for (i in 0 until len) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
        return output
    }

    fun normalizeDegrees(deg: Float): Float {
        val mod = deg % 360f
        return if (mod < 0f) mod + 360f else mod
    }

    /**
     * Shortest signed angular difference in [-180, +180) from [fromDeg] to [toDeg].
     * Prevents 359° -> 1° wrap-around animation glitches.
     */
    fun shortestAngleDelta(fromDeg: Float, toDeg: Float): Float {
        return ((toDeg - fromDeg) % 360f + 540f) % 360f - 180f
    }

    fun isAlignedWithQibla(
        azimuthDeg: Float,
        qiblaBearingDeg: Float,
        toleranceDeg: Float = QIBLA_ALIGNMENT_TOLERANCE_DEG
    ): Boolean {
        return abs(shortestAngleDelta(azimuthDeg, qiblaBearingDeg)) <= toleranceDeg
    }

    fun vectorMagnitude(v: FloatArray): Float {
        if (v.size < 3) return 0f
        return sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2])
    }

    fun hasMagneticInterference(magneticFieldMicroTesla: Float): Boolean {
        return magneticFieldMicroTesla > 0f &&
            (magneticFieldMicroTesla < MIN_VALID_MAGNETIC_UT || magneticFieldMicroTesla > MAX_VALID_MAGNETIC_UT)
    }

    fun cardinalDirectionCode(deg: Float): String {
        val norm = normalizeDegrees(deg)
        val dirs = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val idx = (((norm + 22.5f) % 360f) / 45f).toInt().coerceIn(0, 7)
        return dirs[idx]
    }

    fun magneticDeclinationDeg(lat: Double, lng: Double, timeMs: Long = System.currentTimeMillis()): Float {
        return try {
            GeomagneticField(lat.toFloat(), lng.toFloat(), 0f, timeMs).declination
        } catch (_: Exception) {
            0f
        }
    }

    fun computeOrientationFromSensors(
        gravity: FloatArray,
        geomagnetic: FloatArray,
        displayRotation: Int = Surface.ROTATION_0
    ): SensorOrientation? {
        val rMat = FloatArray(9)
        val iMat = FloatArray(9)
        if (!SensorManager.getRotationMatrix(rMat, iMat, gravity, geomagnetic)) {
            return null
        }

        val remappedR = FloatArray(9)
        when (displayRotation) {
            Surface.ROTATION_90 -> SensorManager.remapCoordinateSystem(
                rMat,
                SensorManager.AXIS_Y,
                SensorManager.AXIS_MINUS_X,
                remappedR
            )
            Surface.ROTATION_180 -> SensorManager.remapCoordinateSystem(
                rMat,
                SensorManager.AXIS_MINUS_X,
                SensorManager.AXIS_MINUS_Y,
                remappedR
            )
            Surface.ROTATION_270 -> SensorManager.remapCoordinateSystem(
                rMat,
                SensorManager.AXIS_MINUS_Y,
                SensorManager.AXIS_X,
                remappedR
            )
            else -> System.arraycopy(rMat, 0, remappedR, 0, 9)
        }

        val orient = FloatArray(3)
        SensorManager.getOrientation(remappedR, orient)
        val azimuthDeg = normalizeDegrees(Math.toDegrees(orient[0].toDouble()).toFloat())
        val pitchDeg = Math.toDegrees(orient[1].toDouble()).toFloat()
        val rollDeg = Math.toDegrees(orient[2].toDouble()).toFloat()
        return SensorOrientation(azimuthDeg, pitchDeg, rollDeg)
    }
}

class LocationAndSensorHelper(private val context: Context) {
    private val fusedClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): GeoPoint? {
        if (!hasLocationPermission()) return null
        return suspendCancellableCoroutine { cont ->
            val cts = CancellationTokenSource()
            fusedClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
                .addOnSuccessListener { loc: Location? ->
                    if (loc != null) {
                        cont.resume(GeoPoint(loc.latitude, loc.longitude))
                    } else {
                        fusedClient.lastLocation
                            .addOnSuccessListener { last ->
                                cont.resume(last?.let { GeoPoint(it.latitude, it.longitude) })
                            }
                            .addOnFailureListener { cont.resume(null) }
                    }
                }
                .addOnFailureListener {
                    cont.resume(null)
                }
            cont.invokeOnCancellation { cts.cancel() }
        }
    }

    private fun currentDisplayRotation(): Int {
        return try {
            val dm = context.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager
            dm?.getDisplay(Display.DEFAULT_DISPLAY)?.rotation ?: Surface.ROTATION_0
        } catch (_: Exception) {
            Surface.ROTATION_0
        }
    }

    /**
     * Streams real-time compass orientation computed directly from the device's
     * Magnetometer (TYPE_MAGNETIC_FIELD) and Accelerometer (TYPE_ACCELEROMETER)
     * sensors, with exponential low-pass filtering, screen-rotation coordinate
     * remapping, and GeomagneticField true-north declination correction.
     */
    fun compassReadingsFlow(
        coordinatesProvider: () -> Pair<Double, Double>? = { null }
    ): Flow<CompassReading> = callbackFlow {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        if (sm == null) {
            trySend(CompassReading(hasAccelerometer = false, hasMagnetometer = false))
            close()
            return@callbackFlow
        }

        val accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnet = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val hasAccel = accel != null
        val hasMagnet = magnet != null

        if (!hasAccel || !hasMagnet) {
            trySend(
                CompassReading(
                    hasAccelerometer = hasAccel,
                    hasMagnetometer = hasMagnet,
                    hasInitialReading = false
                )
            )
            awaitClose {}
            return@callbackFlow
        }

        val gravity = FloatArray(3)
        val geomagnetic = FloatArray(3)
        var hasGravitySample = false
        var hasMagnetSample = false
        var magAccuracy = CompassAccuracy.MEDIUM

        fun emitCurrentReading() {
            if (!hasGravitySample || !hasMagnetSample) return
            val orientation = CompassMath.computeOrientationFromSensors(
                gravity = gravity,
                geomagnetic = geomagnetic,
                displayRotation = currentDisplayRotation()
            ) ?: return

            val coords = coordinatesProvider()
            val declination = if (coords != null) {
                CompassMath.magneticDeclinationDeg(coords.first, coords.second)
            } else {
                0f
            }
            val magAzimuth = orientation.azimuthDeg
            val trueAzimuth = CompassMath.normalizeDegrees(magAzimuth + declination)
            val fieldUt = CompassMath.vectorMagnitude(geomagnetic)

            trySend(
                CompassReading(
                    magneticAzimuthDeg = magAzimuth,
                    trueAzimuthDeg = trueAzimuth,
                    pitchDeg = orientation.pitchDeg,
                    rollDeg = orientation.rollDeg,
                    magneticFieldMicroTesla = fieldUt,
                    declinationDeg = declination,
                    accuracy = magAccuracy,
                    hasAccelerometer = true,
                    hasMagnetometer = true,
                    hasInitialReading = true
                )
            )
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        if (!hasGravitySample) {
                            System.arraycopy(event.values, 0, gravity, 0, 3)
                            hasGravitySample = true
                        } else {
                            CompassMath.lowPassFilter(event.values, gravity, alpha = 0.14f)
                        }
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        if (!hasMagnetSample) {
                            System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                            hasMagnetSample = true
                        } else {
                            CompassMath.lowPassFilter(event.values, geomagnetic, alpha = 0.16f)
                        }
                        emitCurrentReading()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                if (sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    magAccuracy = when (accuracy) {
                        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> CompassAccuracy.HIGH
                        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> CompassAccuracy.MEDIUM
                        SensorManager.SENSOR_STATUS_ACCURACY_LOW -> CompassAccuracy.LOW
                        else -> CompassAccuracy.UNRELIABLE
                    }
                    emitCurrentReading()
                }
            }
        }

        sm.registerListener(listener, accel, SensorManager.SENSOR_DELAY_GAME)
        sm.registerListener(listener, magnet, SensorManager.SENSOR_DELAY_GAME)

        awaitClose {
            sm.unregisterListener(listener)
        }
    }

    fun azimuthDegreesFlow(): Flow<Float> = compassReadingsFlow().map { it.trueAzimuthDeg }
}
