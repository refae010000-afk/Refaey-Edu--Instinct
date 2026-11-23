package com.example

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.GlobalStrings
import com.example.model.ActiveModal
import com.example.model.AppLanguage
import com.example.model.AppTab
import com.example.ui.PilgrimEvent
import com.example.ui.PilgrimUiState
import com.example.ui.PilgrimViewModel
import com.example.ui.components.PilgrimBrandEmblem
import com.example.ui.components.PilgrimModalHost
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MapAndQiblaScreen
import com.example.ui.screens.MoreAndTripScreen
import com.example.ui.screens.RitualsScreen
import com.example.ui.screens.Tour3DScreen
import com.example.ui.theme.DeepInk
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.PilgrimCompanionTheme
import com.example.ui.theme.SacredGold
import com.example.ui.theme.SacredGoldDark
import com.example.ui.theme.SacredGoldLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: PilgrimViewModel = viewModel()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            val context = LocalContext.current

            val locationLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                viewModel.onLocationPermissionResult(granted)
            }

            val notificationLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                viewModel.onNotificationPermissionResult(granted)
            }

            LaunchedEffect(Unit) {
                viewModel.events.collect { event ->
                    when (event) {
                        PilgrimEvent.RequestLocationPermission -> {
                            locationLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            )
                        }
                        PilgrimEvent.RequestNotificationPermission -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                viewModel.onNotificationPermissionResult(true)
                            }
                        }
                        is PilgrimEvent.ShareText -> {
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, event.text)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, GlobalStrings.forLang(state.language).shareLocationBtn))
                        }
                    }
                }
            }

            val layoutDir = if (state.language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            CompositionLocalProvider(LocalLayoutDirection provides layoutDir) {
                PilgrimCompanionTheme(
                    darkTheme = state.darkMode,
                    elderMode = state.elderMode
                ) {
                    PilgrimRootApp(state = state, viewModel = viewModel)
                }
            }
        }
    }
}

private data class NavDestination(
    val tab: AppTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
private fun PilgrimRootApp(
    state: PilgrimUiState,
    viewModel: PilgrimViewModel
) {
    val strings = GlobalStrings.forLang(state.language)

    if (state.currentTab != AppTab.HOME && state.activeModal == ActiveModal.NONE) {
        BackHandler { viewModel.selectTab(AppTab.HOME) }
    }

    val navItems = listOf(
        NavDestination(AppTab.HOME, strings.navHome, Icons.Filled.Home, Icons.Outlined.Home),
        NavDestination(AppTab.TOUR_3D, strings.nav3D, Icons.Filled.ViewInAr, Icons.Outlined.ViewInAr),
        NavDestination(AppTab.RITUALS, strings.navRituals, Icons.Filled.Autorenew, Icons.Outlined.Autorenew),
        NavDestination(AppTab.MAP, strings.navMap, Icons.Filled.Explore, Icons.Outlined.Explore),
        NavDestination(AppTab.MORE, strings.navMore, Icons.AutoMirrored.Filled.MenuBook, Icons.AutoMirrored.Outlined.MenuBook)
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 680.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                PilgrimTopHeader(
                    title = strings.appTitle,
                    subtitle = strings.appSubtitle,
                    currentLanguage = state.language,
                    darkMode = state.darkMode,
                    elderMode = state.elderMode,
                    onSelectLanguage = { viewModel.setLanguage(it) },
                    onToggleElder = { viewModel.toggleElderMode() },
                    onToggleDark = { viewModel.toggleDarkMode() },
                    onOpenAbout = { viewModel.openModal(ActiveModal.ABOUT) }
                )
            },
            bottomBar = {
                if (!useRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp
                    ) {
                        navItems.forEach { dest ->
                            val selected = state.currentTab == dest.tab
                            NavigationBarItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(dest.tab) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                        contentDescription = dest.label,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = dest.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("nav_${dest.tab.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                if (useRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        navItems.forEach { dest ->
                            val selected = state.currentTab == dest.tab
                            NavigationRailItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(dest.tab) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                        contentDescription = dest.label,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = dest.label,
                                        style = MaterialTheme.typography.labelSmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                                ),
                                modifier = Modifier.testTag("rail_${dest.tab.name.lowercase()}")
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Crossfade(
                        targetState = state.currentTab,
                        animationSpec = tween(220),
                        label = "tab_crossfade"
                    ) { tab ->
                        when (tab) {
                            AppTab.HOME -> HomeScreen(state = state, viewModel = viewModel)
                            AppTab.TOUR_3D -> Tour3DScreen(state = state, viewModel = viewModel)
                            AppTab.RITUALS -> RitualsScreen(state = state, viewModel = viewModel)
                            AppTab.MAP -> MapAndQiblaScreen(state = state, viewModel = viewModel)
                            AppTab.MORE -> MoreAndTripScreen(state = state, viewModel = viewModel)
                        }
                    }
                }
            }
        }

        // Toast Pill Overlay
        AnimatedVisibility(
            visible = state.toastMessage != null,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(220)),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 74.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = DeepInk,
                border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.55f)),
                shadowElevation = 6.dp
            ) {
                Text(
                    text = state.toastMessage.orEmpty(),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                )
            }
        }

        // Modals
        PilgrimModalHost(state = state, viewModel = viewModel)

        // Splash Screen Overlay
        AnimatedVisibility(
            visible = state.showSplash,
            exit = fadeOut(tween(450))
        ) {
            SplashOverlay(
                title = strings.splashTitle,
                badge = strings.splashBadge,
                devOrg = strings.splashDevOrg,
                devName = strings.splashDevName
            )
        }
    }
}

@Composable
private fun PilgrimTopHeader(
    title: String,
    subtitle: String,
    currentLanguage: AppLanguage,
    darkMode: Boolean,
    elderMode: Boolean,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggleElder: () -> Unit,
    onToggleDark: () -> Unit,
    onOpenAbout: () -> Unit
) {
    var showLangMenu by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(EmeraldLight, EmeraldDark)))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    PilgrimBrandEmblem(modifier = Modifier.size(38.dp))
                    Column {
                        Text(
                            text = title,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = subtitle,
                            color = SacredGold,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box {
                        Surface(
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("header_lang_btn")
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { showLangMenu = true },
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.18f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                        ) {
                            Text(
                                text = currentLanguage.code.uppercase(),
                                color = Color.White,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = showLangMenu,
                            onDismissRequest = { showLangMenu = false }
                        ) {
                            AppLanguage.entries.forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = lang.label,
                                            fontWeight = if (lang == currentLanguage) FontWeight.ExtraBold else FontWeight.Normal,
                                            color = if (lang == currentLanguage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        showLangMenu = false
                                        onSelectLanguage(lang)
                                    }
                                )
                            }
                        }
                    }

                    HeaderIconPill(
                        icon = Icons.Filled.FormatSize,
                        contentDescription = "Elder Text Size",
                        active = elderMode,
                        tag = "header_elder_btn",
                        onClick = onToggleElder
                    )
                    HeaderIconPill(
                        icon = if (darkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = "Toggle Theme",
                        active = darkMode,
                        tag = "header_dark_btn",
                        onClick = onToggleDark
                    )
                    HeaderIconPill(
                        icon = Icons.Outlined.Info,
                        contentDescription = "About",
                        active = false,
                        tag = "header_about_btn",
                        onClick = onOpenAbout
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(SacredGoldDark)
            )
        }
    }
}

@Composable
private fun HeaderIconPill(
    icon: ImageVector,
    contentDescription: String,
    active: Boolean,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag(tag)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (active) SacredGold.copy(alpha = 0.28f) else Color.White.copy(alpha = 0.16f),
        border = BorderStroke(1.dp, if (active) SacredGold else Color.White.copy(alpha = 0.22f))
    ) {
        Box(
            modifier = Modifier.padding(7.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (active) SacredGoldLight else Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SplashOverlay(
    title: String,
    badge: String,
    devOrg: String,
    devName: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash_float")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -10f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "float_y"
    )
    val rotY by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "rot_y"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(EmeraldLight, Color(0xFF062118))
                )
            )
            .navigationBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .graphicsLayer {
                        translationY = offsetY
                        rotationY = rotY
                    }
                    .clip(RoundedCornerShape(26.dp))
                    .background(Brush.linearGradient(listOf(SacredGoldLight, Color(0xFF8D6B2A))))
                    .padding(6.dp)
            ) {
                PilgrimBrandEmblem(modifier = Modifier.fillMaxSize())
            }

            Text(
                text = title,
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, SacredGold.copy(alpha = 0.5f))
            ) {
                Text(
                    text = badge,
                    color = SacredGoldLight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = devOrg,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = devName,
                color = SacredGoldLight,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
