package com.example.data

import com.example.model.AppLanguage
import com.example.model.LandmarkCategory
import com.example.model.OfflineMapRegion
import com.example.model.OfflineMapStrings

internal object OfflineMapCatalogExtra {

    fun stringsFor(lang: AppLanguage): OfflineMapStrings = when (lang) {
        AppLanguage.UR -> urStrings
        AppLanguage.ID -> idStrings
        AppLanguage.TR -> trStrings
        else -> urStrings
    }

    fun textMapFor(lang: AppLanguage): Map<String, LandmarkTextSpec> = when (lang) {
        AppLanguage.UR -> urLandmarks
        AppLanguage.ID -> idLandmarks
        AppLanguage.TR -> trLandmarks
        else -> urLandmarks
    }

    private val urStrings = OfflineMapStrings(
        cardTitle = "مقاماتِ مقدسہ کا آف لائن نقشہ",
        cardSubtitle = "انٹرنیٹ کے بغیر 100% دستیاب • حرم، مشاعر اور مدینہ",
        offlineReadyBadge = "آف لائن دستیاب",
        regionLabels = mapOf(
            OfflineMapRegion.HARAM_PRECINCT to "🕋 مسجدِ حرام",
            OfflineMapRegion.MAKKAH_MASHAAER to "⛺ مشاعرِ مقدسہ",
            OfflineMapRegion.MADINAH_SANCTUARY to "🕌 مدینہ منورہ"
        ),
        categoryLabels = mapOf(
            LandmarkCategory.ALL to "سب",
            LandmarkCategory.SANCTUARY to "مساجدِ مقدسہ",
            LandmarkCategory.RITUAL to "مقاماتِ مناسک",
            LandmarkCategory.TRANSIT to "دروازے و سہولیات",
            LandmarkCategory.MIQAT to "میقات"
        ),
        resetZoomHint = "ری سیٹ",
        panZoomTip = "گھمانے کے لیے کھینچیں، زوم کریں اور کسی بھی مقام پر ٹیپ کریں",
        distFromHaramLabel = "مرکز سے فاصلہ",
        distFromYouLabel = "آپ سے فاصلہ",
        coordsLabel = "محلِ وقوع",
        tipPrefix = "رہنمائی:",
        open3DBtn = "3D منظر",
        listenBtn = "سنیں",
        railwayLegend = "مشاعر ٹرین",
        walkPathLegend = "پیدل راستہ",
        youMarkerLabel = "آپ",
        hotelMarkerLabel = "ہوٹل",
        kmUnit = "کلومیٹر",
        mUnit = "میٹر"
    )

    private val idStrings = OfflineMapStrings(
        cardTitle = "Peta Lokal Situs Suci (Offline)",
        cardSubtitle = "Berfungsi 100% tanpa internet • Masjidil Haram, Masyair & Madinah",
        offlineReadyBadge = "Siap Offline",
        regionLabels = mapOf(
            OfflineMapRegion.HARAM_PRECINCT to "🕋 Masjidil Haram",
            OfflineMapRegion.MAKKAH_MASHAAER to "⛺ Area Masyair",
            OfflineMapRegion.MADINAH_SANCTUARY to "🕌 Madinah"
        ),
        categoryLabels = mapOf(
            LandmarkCategory.ALL to "Semua",
            LandmarkCategory.SANCTUARY to "Masjid",
            LandmarkCategory.RITUAL to "Lokasi Manasik",
            LandmarkCategory.TRANSIT to "Pintu & Akses",
            LandmarkCategory.MIQAT to "Miqat"
        ),
        resetZoomHint = "Reset",
        panZoomTip = "Geser untuk mengarahkan, cubit untuk zoom, dan ketuk pin",
        distFromHaramLabel = "Dari Pusat",
        distFromYouLabel = "Dari Lokasi Anda",
        coordsLabel = "Koordinat",
        tipPrefix = "Tips Ibadah:",
        open3DBtn = "Lihat 3D",
        listenBtn = "Dengar",
        railwayLegend = "Kereta Masyair",
        walkPathLegend = "Jalur Pejalan",
        youMarkerLabel = "Anda",
        hotelMarkerLabel = "Hotel",
        kmUnit = "km",
        mUnit = "m"
    )

    private val trStrings = OfflineMapStrings(
        cardTitle = "Çevrimdışı Kutsal Mekanlar Haritası",
        cardSubtitle = "İnternet olmadan %100 çalışır • Harem, Meşair ve Medine",
        offlineReadyBadge = "Çevrimdışı Hazır",
        regionLabels = mapOf(
            OfflineMapRegion.HARAM_PRECINCT to "🕋 Mescid-i Haram",
            OfflineMapRegion.MAKKAH_MASHAAER to "⛺ Meşair Bölgesi",
            OfflineMapRegion.MADINAH_SANCTUARY to "🕌 Medine"
        ),
        categoryLabels = mapOf(
            LandmarkCategory.ALL to "Tümü",
            LandmarkCategory.SANCTUARY to "Mescitler",
            LandmarkCategory.RITUAL to "Menasik Noktaları",
            LandmarkCategory.TRANSIT to "Kapılar ve Erişim",
            LandmarkCategory.MIQAT to "Mikatlar"
        ),
        resetZoomHint = "Sıfırla",
        panZoomTip = "Kaydırın, yakınlaştırın ve bir işarete dokunun",
        distFromHaramLabel = "Merkeze Uzaklık",
        distFromYouLabel = "Konumunuzdan",
        coordsLabel = "Koordinatlar",
        tipPrefix = "İpucu:",
        open3DBtn = "3D Görünüm",
        listenBtn = "Dinle",
        railwayLegend = "Meşair Treni",
        walkPathLegend = "Yürüyüş Yolu",
        youMarkerLabel = "Siz",
        hotelMarkerLabel = "Otel",
        kmUnit = "km",
        mUnit = "m"
    )

    private val urLandmarks: Map<String, LandmarkTextSpec> = mapOf(
        "h_haram" to LandmarkTextSpec("مسجدِ حرام اور خانہ کعبہ", "خانہ کعبہ", "مطاف اور قبلہ کا مرکز", "روئے زمین کا مقدس ترین مقام جہاں کعبۃ اللہ، حجرِ اسود اور مطاف واقع ہیں۔", "حجرِ اسود کے مقابل آ کر طواف شروع کریں اور کعبہ کو بائیں طرف رکھیں۔"),
        "h_safa" to LandmarkTextSpec("کوہِ صفا (سعی کی ابتدا)", "صفا", "سعی کا نقطہ آغاز", "مسعیٰ کے جنوبی سرے پر واقع پہاڑی جہاں سے سات چکروں کا آغاز ہوتا ہے۔", "صفا پر کعبہ کی طرف رخ کر کے دعا کریں۔"),
        "h_marwah" to LandmarkTextSpec("کوہِ مروہ (سعی کا اختتام)", "مروہ", "ساتویں چکر کا اختتام", "مسعیٰ کے شمالی سرے پر واقع جہاں طاق چکر اور ساتواں چکر مکمل ہوتا ہے۔", "ساتواں چکر پورا کر کے حلق یا قصر کروائیں۔"),
        "h_maqam" to LandmarkTextSpec("مقامِ ابراہیم اور آبِ زمزم", "مقامِ ابراہیم", "طواف کی دو رکعت نماز", "طواف مکمل کرنے کے بعد مقامِ ابراہیم کے پیچھے دو رکعت سنت پڑھیں۔", "رش کی صورت میں مسجدِ حرام میں کہیں بھی نماز پڑھی جا سکتی ہے۔"),
        "h_king_fahd" to LandmarkTextSpec("باب الملک فہد", "باب الملک فہد", "مغربی مرکزی دروازہ", "بڑا مغربی دروازہ جو براہِ راست صحنِ مطاف اور بالائی منزلوں تک لے جاتا ہے۔", "گروپ سے ملنے کے لیے گیٹ نمبر یاد رکھیں۔"),
        "h_abdulaziz" to LandmarkTextSpec("باب الملک عبدالعزیز", "باب عبدالعزیز", "جنوبی گیٹ و کلاک ٹاور", "ابراج البیت کلاک ٹاور اور بس اسٹیشن کے سامنے جنوبی مرکزی دروازہ۔", "جنوبی ہوٹلوں اور طبی مراکز کے لیے آسان راستہ۔"),
        "h_carts" to LandmarkTextSpec("الیکٹرک کارٹ پلیٹ فارم (مروہ)", "الیکٹرک کارٹس", "بزرگوں کے لیے سواری", "معمر اور معذور حجاج کے لیے طواف و سعی کی وہیل چیئر اور کارٹ کی منزل۔", "مروہ یا اجیاد کی لفٹ استعمال کریں۔"),

        "m_haram" to LandmarkTextSpec("مسجدِ حرام (مکہ مکرمہ)", "مسجدِ حرام", "آغاز و اختتام کا مرکز", "عمرہ، طوافِ قدوم، طوافِ افاضہ اور طوافِ وداع کا مرکزی مقام۔", "نکلنے سے پہلے ہوٹل کی لوکیشن آف لائن محفوظ کریں۔"),
        "m_jamarat" to LandmarkTextSpec("جمرات پل (تینوں جمرات)", "جمرات", "صغریٰ • وسطیٰ • کبریٰ", "منیٰ میں کثیر منزلہ پل جہاں شیطان کو کنکریاں ماری جاتی ہیں۔", "مقررہ راستوں اور اوقاتِ تفویج کی پابندی کریں۔"),
        "m_khaif" to LandmarkTextSpec("مسجدِ خیف (منیٰ)", "مسجدِ خیف", "منیٰ کی تاریخی مسجد", "منیٰ کے جنوبی دامن میں جمرات کے قریب واقع عظیم تاریخی مسجد۔", "منیٰ کی خیمہ بستی میں سمت شناسی کے لیے نمایاں علامت۔"),
        "m_mina" to LandmarkTextSpec("مشعر منیٰ (خیموں کا شہر)", "منیٰ", "8 ذوالحجہ و ایامِ تشریق", "مسجدِ حرام سے ~5 کلومیٹر مشرق میں سفید خیموں کی وادی۔", "پہنچتے ہی اپنے خیمے کا بلاک اور گلی نمبر نوٹ کریں۔"),
        "m_muzdalifah" to LandmarkTextSpec("مشعر مزدلفہ", "مزدلفہ", "کھلے آسمان تلے شبِ عاشور", "عرفات و منیٰ کے درمیان میدان جہاں مغرب و عشاء جمع کر کے رات گزاری جاتی ہے۔", "جمرہ عقبہ کے لیے چنے کے برابر 7 کنکریاں چنیں۔"),
        "m_namirah" to LandmarkTextSpec("مسجدِ نمرہ (عرفات)", "مسجدِ نمرہ", "خطبۂ حجۃ الوداع کی جگہ", "عرفات کی مغربی سرحد پر عظیم مسجد جہاں خطبہ عرفہ اور ظہر و عصر جمع ہوتی ہیں۔", "غروب تک حدودِ عرفات کے اندر رہنا یقینی بنائیں۔"),
        "m_arafat" to LandmarkTextSpec("میدانِ عرفات و جبلِ رحمت", "عرفات", "حج کا رکنِ اعظم", "مکہ سے ~20 کلومیٹر مشرق میں جہاں 9 ذوالحجہ کو وقوف ہوتا ہے۔", "ظہر سے غروبِ آفتاب تک دعا اور استغفار میں مشغول رہیں۔"),
        "m_taneem" to LandmarkTextSpec("مسجدِ عائشہ (تنعیم)", "تنعیم", "مکہ کا قریب ترین میقات", "مکہ کے اندر مقیم افراد کے لیے عمرہ کا احرام باندھنے کا میقات۔", "وضو اور احرام کی سہولت چوبیس گھنٹے موجود ہے۔"),

        "d_nabawi" to LandmarkTextSpec("مسجدِ نبوی شریف", "مسجدِ نبوی", "گنبدِ خضرا و حرمِ مدنی", "رسول اللہ ﷺ کی مسجد؛ یہاں ایک نماز ہزار نمازوں سے بہتر ہے۔", "ادب سے دایاں قدم رکھ کر داخل ہوں اور درود پڑھیں۔"),
        "d_rawdah" to LandmarkTextSpec("ریاض الجنہ", "ریاض الجنہ", "جنت کا باغیچہ", "منبرِ رسول اور حجرۂ مبارکہ کے درمیان بابرکت جگہ۔", "داخلے کے لیے نسک ایپ کا پرمٹ ساتھ رکھیں۔"),
        "d_baqi" to LandmarkTextSpec("جنت البقیع", "بقیعِ غرقد", "صحابہ کرام کا مدفن", "مسجدِ نبوی کے مشرق میں تاریخی قبرستان۔", "فجر و عصر کے بعد باہر سے سلام اور دعا کریں۔"),
        "d_quba" to LandmarkTextSpec("مسجدِ قبا", "مسجدِ قبا", "اسلام کی پہلی مسجد", "مسجدِ نبوی سے پیدل جادۂ قبا کے ذریعے منسلک؛ دو رکعت کا ثواب عمرہ کے برابر۔", "قیام گاہ سے وضو کر کے قبا جائیں۔"),
        "d_uhud" to LandmarkTextSpec("جبلِ احد و شہداء", "جبلِ احد", "مدینہ کے شمال میں تاریخی پہاڑ", "غزوۂ احد کا میدان اور سید الشہداء حضرت حمزہؓ کا مزار۔", "شہدائے احد کو سلام عرض کر کے دعا کریں۔"),
        "d_hulayfah" to LandmarkTextSpec("میقات ذوالحلیفہ (آبارِ علی)", "ذوالحلیفہ", "اہلِ مدینہ کا میقات", "مدینہ سے مکہ جانے والوں کا میقات (~430 کلومیٹر دور)۔", "روانگی سے قبل احرام پہن کر یہاں نیت کریں۔")
    )

    private val idLandmarks: Map<String, LandmarkTextSpec> = mapOf(
        "h_haram" to LandmarkTextSpec("Masjidil Haram & Ka'bah", "Ka'bah Suci", "Pusat Mataf & Kiblat", "Masjid tersuci yang menaungi Ka'bah, Hajar Aswad, dan pelataran Mataf.", "Mulai Tawaf sejajar Hajar Aswad dengan posisi Ka'bah di sisi kiri."),
        "h_safa" to LandmarkTextSpec("Bukit Safa (Awal Sa'i)", "Safa", "Titik Mulai Sa'i", "Ujung selatan koridor Mas'a tempat memulai 7 putaran Sa'i.", "Menghadap Ka'bah di atas Safa lalu berdoa dengan khusyuk."),
        "h_marwah" to LandmarkTextSpec("Bukit Marwah (Akhir Sa'i)", "Marwah", "Akhir Putaran ke-7", "Ujung utara Mas'a tempat putaran ke-7 Sa'i selesai.", "Setelah putaran ke-7, lanjutkan Tahallul (cukur/potong rambut)."),
        "h_maqam" to LandmarkTextSpec("Maqam Ibrahim & Zamzam", "Maqam Ibrahim", "Tempat Shalat Sunnah Tawaf", "Shalat dua rakaat sunnah Tawaf di belakang Maqam Ibrahim.", "Jika padat, shalat dua rakaat sah di bagian mana pun Masjidil Haram."),
        "h_king_fahd" to LandmarkTextSpec("Pintu Raja Fahd", "Pintu Raja Fahd", "Gerbang Utama Barat", "Gerbang barat utama menuju pelataran Mataf dan lantai atas.", "Gunakan nomor pintu sebagai titik temu rombongan offline."),
        "h_abdulaziz" to LandmarkTextSpec("Pintu Raja Abdulaziz", "Pintu Abdulaziz", "Gerbang Selatan & Menara Jam", "Gerbang utama selatan menghadap Zamzam Tower dan terminal bus.", "Akses terdekat menuju hotel area selatan dan pos medis."),
        "h_carts" to LandmarkTextSpec("Area Skuter Listrik (Marwah)", "Skuter & Kursi", "Lantai Mobilitas", "Zona layanan skuter listrik dan kursi roda untuk Tawaf dan Sa'i.", "Gunakan lift sisi Marwah atau Ajyad untuk akses langsung."),

        "m_haram" to LandmarkTextSpec("Masjidil Haram (Makkah)", "Masjidil Haram", "Pusat Awal & Akhir Manasik", "Pusat pelaksanaan Tawaf Umrah, Qudum, Ifadah, dan Wada.", "Simpan koordinat hotel secara offline sebelum ke Masyair."),
        "m_jamarat" to LandmarkTextSpec("Jembatan Jamarat", "Jamarat", "Ula • Wustha • Aqabah", "Kompleks bertingkat di Mina untuk melontar jumrah.", "Ikuti jalur resmi dan jadwal lontar agar aman dari kepadatan."),
        "m_khaif" to LandmarkTextSpec("Masjid Al-Khaif (Mina)", "Al-Khaif", "Masjid Bersejarah Mina", "Masjid besar di lereng selatan Mina dekat kawasan Jamarat.", "Penanda arah utama di tengah area perkemahan Mina."),
        "m_mina" to LandmarkTextSpec("Kota Tenda Mina", "Mina", "Tarwiyah & Hari Tasyriq", "Lembah tenda ~5 km di timur Masjidil Haram tempat mabit jemaah haji.", "Catat nomor maktab dan blok tenda segera setelah tiba."),
        "m_muzdalifah" to LandmarkTextSpec("Muzdalifah (Masy'aril Haram)", "Muzdalifah", "Mabit Malam 10 Dzulhijjah", "Area terbuka antara Arafah dan Mina untuk jamak Maghrib-Isya & ambil kerikil.", "Kumpulkan 7 kerikil seukuran kacang untuk Jumrah Aqabah."),
        "m_namirah" to LandmarkTextSpec("Masjid Namirah (Arafah)", "Masjid Namirah", "Masjid Khutbah Arafah", "Masjid besar di perbatasan barat Arafah tempat khutbah 9 Dzulhijjah.", "Pastikan posisi berada di dalam batas resmi Arafah."),
        "m_arafat" to LandmarkTextSpec("Padang Arafah & Jabal Rahmah", "Jabal Rahmah", "Rukun Terbesar Haji", "Berjarak ~20 km dari Makkah untuk wukuf pada 9 Dzulhijjah.", "Perbanyak doa dan dzikir dari Zhuhur hingga terbenam matahari."),
        "m_taneem" to LandmarkTextSpec("Masjid Aisyah (Tan'im)", "Tan'im", "Miqat Terdekat dari Makkah", "Miqat di utara Makkah (~7,5 km) untuk ihram umrah tambahan.", "Tersedia tempat wudhu dan ruang ganti ihram 24 jam."),

        "d_nabawi" to LandmarkTextSpec("Masjid Nabawi", "Masjid Nabawi", "Kubah Hijau & Haram Madinah", "Masjid Rasulullah ﷺ; satu shalat setara seribu shalat di tempat lain.", "Masuk dengan kaki kanan dan perbanyak shalawat."),
        "d_rawdah" to LandmarkTextSpec("Raudhah Asy-Syarifah", "Raudhah", "Taman Surga", "Area mulia antara mimbar Nabi dan Kamar Suci di Masjid Nabawi.", "Siapkan izin/tasreh Nusuk di gerbang masuk Raudhah."),
        "d_baqi" to LandmarkTextSpec("Pemakaman Baqi", "Al-Baqi", "Makam Para Sahabat", "Pemakaman bersejarah di sebelah timur Masjid Nabawi.", "Dikunjungi dari pagar luar setelah Subuh dan Ashar."),
        "d_quba" to LandmarkTextSpec("Masjid Quba", "Masjid Quba", "Masjid Pertama dalam Islam", "Terhubung jalur pedestrian Jaddat Quba; shalat 2 rakaat bernilai pahala umrah.", "Berwudhu dari hotel sebelum berjalan kaki ke Quba."),
        "d_uhud" to LandmarkTextSpec("Gunung Uhud & Syuhada", "Gunung Uhud", "Situs Perang Uhud", "Gunung bersejarah di utara Madinah beserta makam para syuhada.", "Ucapkan salam dan doakan para syuhada Uhud."),
        "d_hulayfah" to LandmarkTextSpec("Miqat Dzulhulaifah (Bir Ali)", "Dzulhulaifah", "Miqat Jemaah dari Madinah", "Miqat utama (~430 km dari Makkah) bagi jemaah bertolak dari Madinah.", "Kenakan kain ihram di hotel lalu niat saat tiba di miqat.")
    )

    private val trLandmarks: Map<String, LandmarkTextSpec> = mapOf(
        "h_haram" to LandmarkTextSpec("Mescid-i Haram ve Kabe", "Kabe-i Muazzama", "Metaf ve Kıble Merkezi", "Yeryüzündeki en kutsal mescit; Kabe, Hacerü'l-Esved ve Metaf alanı.", "Hacerü'l-Esved hizasından, Kabe solunuzda kalacak şekilde Tavafa başlayın."),
        "h_safa" to LandmarkTextSpec("Safa Tepesi (Sa'y Başlangıcı)", "Safa", "Sa'y Başlangıç Noktası", "Sa'y koridorunun güney ucunda yer alır ve 7 şavt buradan başlar.", "Safa'da Kabe'ye yönelip tekbir ve dualarla başlayın."),
        "h_marwah" to LandmarkTextSpec("Merve Tepesi (Sa'y Bitişi)", "Merve", "7. Şavtın Bitişi", "Mes'a koridorunun kuzey ucudur; 7. şavt burada tamamlanır.", "7. şavt sonrası burada tıraş olup ihramdan çıkılır (Tehallül)."),
        "h_maqam" to LandmarkTextSpec("Makam-ı İbrahim ve Zemzem", "Makam-ı İbrahim", "Tavaf Namazı Yeri", "Tavaf bitiminde arkasında iki rekât tavaf namazı kılınır.", "Kalabalıkta mescidin herhangi bir yerinde kılınması da caizdir."),
        "h_king_fahd" to LandmarkTextSpec("Kral Fehd Kapısı", "Fehd Kapısı", "Ana Batı Girişi", "Doğrudan Metaf avlusuna ve üst katlara açılan büyük batı kapısı.", "Grubunuzla çevrimdışı buluşma noktası olarak kapı numarasını kaydedin."),
        "h_abdulaziz" to LandmarkTextSpec("Kral Abdülaziz Kapısı", "Abdülaziz Kapısı", "Güney Kapısı & Saat Kulesi", "Saat Kulesi ve otobüs terminallerinin karşısındaki ana güney kapısı.", "Güneydeki otellere ve sağlık noktalarına en yakın çıkıştır."),
        "h_carts" to LandmarkTextSpec("Elektrikli Araç Katı (Merve)", "Araç Katı", "Engelli & Yaşlı Erişimi", "Tavaf ve Sa'y için akülü araç ve tekerlekli sandalye kiralama alanı.", "Merve veya Ecyad asansörleri ile doğrudan ulaşabilirsiniz."),

        "m_haram" to LandmarkTextSpec("Mescid-i Haram (Mekke)", "Mescid-i Haram", "Ana Başlangıç Merkezi", "Umre, Kudüm, İfaza ve Veda tavaflarının eda edildiği merkez.", "Meşair bölgesine çıkmadan otel GPS konumunuzu çevrimdışı kaydedin."),
        "m_jamarat" to LandmarkTextSpec("Cemerat Köprüsü", "Cemerat", "Küçük • Orta • Büyük", "Mina'da şeytan taşlamanın yapıldığı çok katlı köprü kompleksi.", "Belirlenen geçiş saatlerine ve yürüyüş koridorlarına uyun."),
        "m_khaif" to LandmarkTextSpec("Hayf Mescidi (Mina)", "Hayf Mescidi", "Mina Tarihi Mescidi", "Mina'nın güney yamacında Cemerat yakınındaki büyük tarihi mescit.", "Mina çadır kentinde yön bulmak için temel referans noktasıdır."),
        "m_mina" to LandmarkTextSpec("Mina Çadır Kenti", "Mina", "Terviye ve Teşrik Günleri", "Mescid-i Haram'ın ~5 km doğusunda hacıların konakladığı beyaz çadır vadisi.", "Varışta çadır kamp numaranızı ve cadde numaranızı not edin."),
        "m_muzdalifah" to LandmarkTextSpec("Müzdelife (Meş'ar-i Haram)", "Müzdelife", "Arefe Gecesi Vakfesi", "Arafat ve Mina arasında akşam/yatsının cem edilip taş toplandığı alan.", "Bayramın 1. günü Büyük Şeytan için nohut büyüklüğünde 7 taş toplayın."),
        "m_namirah" to LandmarkTextSpec("Nemire Mescidi (Arafat)", "Nemire", "Arefe Hutbesi Mescidi", "Arafat batı sınırında Arefe hutbesinin okunduğu büyük mescit.", "Gün batımına kadar resmi Arafat sınırları içinde kaldığınızdan emin olun."),
        "m_arafat" to LandmarkTextSpec("Arafat Meydanı ve Cebel-i Rahme", "Arafat", "Haccın En Büyük Rükünü", "Mekke'nin ~20 km doğusunda 9 Zilhicce'de vakfe yapılan kutsal alan.", "Öğleden gün batımına kadar vakti dua ve istiğfarla değerlendirin."),
        "m_taneem" to LandmarkTextSpec("Hz. Aişe Mescidi (Ten'im)", "Ten'im", "Mekke'ye En Yakın Mikat", "Mekke içindeki ziyaretçilerin ek umre için ihrama girdiği mikat.", "Abdest ve ihram giyinme alanları 7/24 açıktır."),

        "d_nabawi" to LandmarkTextSpec("Mescid-i Nebevi", "Mescid-i Nebevi", "Yeşil Kubbe & Harem-i Şerif", "Peygamber Efendimiz'in mescidi; burada bir namaz bin namazdan hayırlıdır.", "Sağ ayakla edep içinde girin ve bolca salavat getirin."),
        "d_rawdah" to LandmarkTextSpec("Ravza-i Mutahhara", "Ravza", "Cennet Bahçesi", "Peygamber Efendimiz'in minberi ile Hücre-i Saadet arasındaki mübarek alan.", "Ravza giriş kapısında Nusuk izninizi hazır bulundurun."),
        "d_baqi" to LandmarkTextSpec("Cennetü'l-Baki Kabristanı", "Baki Kabristanı", "Sahabeler Kabristanı", "Mescid-i Nebevi'nin doğusunda binlerce sahabenin medfun olduğu kabristan.", "Sabah ve ikindi sonrası dış yürüyüş yolundan ziyaret edilip dua edilir."),
        "d_quba" to LandmarkTextSpec("Kuba Mescidi", "Kuba Mescidi", "İslam'ın İlk Mescidi", "Yürüyüş yoluyla bağlıdır; orada kılınan 2 rekât bir umre sevabına denktir.", "Otelinizde abdest alıp Kuba'ya doğru yola çıkın."),
        "d_uhud" to LandmarkTextSpec("Uhud Dağı ve Şehitliği", "Uhud Dağı", "Tarihi Şehitlik", "Medine kuzeyinde Uhud Gazvesi ve Hz. Hamza'nın bulunduğu mübarek alan.", "Uhud şehitlerine selam verip dua edin."),
        "d_hulayfah" to LandmarkTextSpec("Zülhuleyfe Mikatı (Ebyar-ı Ali)", "Zülhuleyfe", "Medine Mikatı", "Medine'den Mekke'ye gidenlerin ihrama girdiği mikat (~430 km).", "Otelden ihram giyip mikatta niyet ve telbiye getirin.")
    )
}
