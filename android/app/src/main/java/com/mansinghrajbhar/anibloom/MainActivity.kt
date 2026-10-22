package com.mansinghrajbhar.anibloom

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.os.BatteryManager
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.Brightness6
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AnibloomApp() }
    }
}

private val Bg = Color(0xFF09090B)
private val Card = Color(0xFF17171A)
private val Card2 = Color(0xFF27272B)
private val Accent = Color(0xFFF0F0F3)
private val Muted = Color(0xFF9B9BA3)

@Composable
fun AnibloomApp() {
    var page by remember { mutableStateOf("Home") }
    var islandExpanded by remember { mutableStateOf(false) }

    Surface(Modifier.fillMaxSize(), color = Bg) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxSize().padding(horizontal = 18.dp).padding(top = 92.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(targetState = page, label = "page") { current ->
                    when (current) {
                        "Home" -> HomePage { islandExpanded = true }
                        "Music" -> MusicPage()
                        "Controls" -> ControlsPage()
                        "Calendar" -> CalendarPage()
                        else -> SettingsPage()
                    }
                }
                Spacer(Modifier.weight(1f))
                Dock(page) { page = it }
            }

            Island(
                expanded = islandExpanded,
                onExpandedChange = { islandExpanded = it },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .windowInsetsPadding(WindowInsets.statusBars)
            )
        }
    }
}

@Composable
private fun Island(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val width by animateDpAsState(
        targetValue = if (expanded) 336.dp else 156.dp,
        animationSpec = spring(dampingRatio = 0.68f, stiffness = 380f),
        label = "islandWidth"
    )
    val height by animateDpAsState(
        targetValue = if (expanded) 108.dp else 42.dp,
        animationSpec = spring(dampingRatio = 0.68f, stiffness = 380f),
        label = "islandHeight"
    )

    Box(
        modifier = modifier
            .padding(top = 4.dp)
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.Black)
            .clickable { onExpandedChange(!expanded) }
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount > 10f) onExpandedChange(true)
                    if (dragAmount < -10f) onExpandedChange(false)
                }
            }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (expanded) {
            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(44.dp).clip(RoundedCornerShape(13.dp)).background(Card2), Alignment.Center) {
                        Icon(Icons.Rounded.MusicNote, null, tint = Accent)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Anibloom Island", color = Accent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Now ready · tap to collapse", color = Muted, fontSize = 11.sp)
                    }
                    Text("×", color = Accent, fontSize = 24.sp, modifier = Modifier.clickable { onExpandedChange(false) })
                }
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IslandButton("⏮") { mediaKey(context, KeyEvent.KEYCODE_MEDIA_PREVIOUS) }
                        IslandButton("▶") { mediaKey(context, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) }
                        IslandButton("⏭") { mediaKey(context, KeyEvent.KEYCODE_MEDIA_NEXT) }
                    }
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                Box(Modifier.size(7.dp).clip(CircleShape).background(Accent))
                Text("Anibloom", color = Accent, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                Text("⌄", color = Muted, fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun RowScope.IslandButton(text: String, onClick: () -> Unit) {
    Box(
        Modifier.weight(1f).height(30.dp).clip(RoundedCornerShape(12.dp)).background(Card2).clickable { onClick() },
        Alignment.Center
    ) { Text(text, color = Accent, fontSize = 13.sp) }
}

@Composable
private fun HomePage(onIsland: () -> Unit) {
    val context = LocalContext.current
    val battery = rememberBattery(context)
    Column(Modifier.fillMaxWidth()) {
        Text("Good evening", color = Muted, fontSize = 14.sp)
        Text("Your Android, alive.", color = Accent, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(22.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActionCard("Battery", "$battery%", Icons.Rounded.BatteryFull, Modifier.weight(1f)) { }
            ActionCard("Display", "Punch-hole", Icons.Rounded.Brightness6, Modifier.weight(1f)) { open(context, Settings.ACTION_DISPLAY_SETTINGS) }
        }
        Spacer(Modifier.height(12.dp))
        ActionCard("Anibloom Island", "Tap here or swipe down to expand", Icons.Rounded.AutoAwesome, Modifier.fillMaxWidth(), onIsland)
    }
}

@Composable
private fun MusicPage() {
    val context = LocalContext.current
    var playing by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Music", color = Muted, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))
        Box(Modifier.size(210.dp).clip(RoundedCornerShape(30.dp)).background(Card2), Alignment.Center) {
            Icon(Icons.Rounded.MusicNote, null, tint = Accent, modifier = Modifier.size(72.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text("Android Media", color = Accent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Control the active music player", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BigButton("⏮") { mediaKey(context, KeyEvent.KEYCODE_MEDIA_PREVIOUS) }
            BigButton(if (playing) "⏸" else "▶") { mediaKey(context, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE); playing = !playing }
            BigButton("⏭") { mediaKey(context, KeyEvent.KEYCODE_MEDIA_NEXT) }
        }
    }
}

@Composable
private fun ControlsPage() {
    val context = LocalContext.current
    var flash by remember { mutableStateOf(false) }
    var bright by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Text("Command Center", color = Accent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Android controls", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionTile("Wi-Fi", Icons.Rounded.Wifi, Modifier.weight(1f)) { open(context, Settings.Panel.ACTION_WIFI) }
            ActionTile("Bluetooth", Icons.Rounded.Bluetooth, Modifier.weight(1f)) { open(context, Settings.ACTION_BLUETOOTH_SETTINGS) }
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionTile("Flashlight", Icons.Rounded.FlashOn, Modifier.weight(1f), flash) { flash = !flash; torch(context, flash) }
            ActionTile("Brightness", Icons.Rounded.Brightness6, Modifier.weight(1f), bright) {
                if (Settings.System.canWrite(context)) {
                    bright = !bright
                    Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, if (bright) 255 else 70)
                } else open(context, Settings.ACTION_MANAGE_WRITE_SETTINGS, "package:${context.packageName}")
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ActionTile("Do Not Disturb", Icons.Rounded.Notifications, Modifier.weight(1f)) { open(context, Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS) }
            ActionTile("Volume", Icons.Rounded.Brightness6, Modifier.weight(1f)) { open(context, Settings.ACTION_SOUND_SETTINGS) }
        }
    }
}

@Composable
private fun CalendarPage() {
    var seconds by remember { mutableStateOf(1500) }
    var running by remember { mutableStateOf(false) }
    LaunchedEffect(running) {
        while (running && seconds > 0) { delay(1000); seconds-- }
        if (seconds == 0) running = false
    }
    Column(Modifier.fillMaxWidth()) {
        Text("Pomodoro", color = Accent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Focus timer", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(14.dp))
        ActionCard("Focus", "%02d:%02d".format(seconds / 60, seconds % 60), Icons.Rounded.Timer, Modifier.fillMaxWidth()) { running = !running }
        Spacer(Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BigButton(if (running) "Pause" else "Start") { running = !running }
            BigButton("Reset") { running = false; seconds = 1500 }
        }
    }
}

@Composable
private fun SettingsPage() {
    val context = LocalContext.current
    Column(Modifier.fillMaxWidth()) {
        Text("Settings", color = Accent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Private Android configuration", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(14.dp))
        ActionCard("Display", "Punch-hole optimized", Icons.Rounded.AutoAwesome, Modifier.fillMaxWidth()) { open(context, Settings.ACTION_DISPLAY_SETTINGS) }
        Spacer(Modifier.height(10.dp))
        ActionCard("Overlay", "Allow Island over other apps", Icons.Rounded.AutoAwesome, Modifier.fillMaxWidth()) { open(context, Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "package:${context.packageName}") }
        Spacer(Modifier.height(10.dp))
        ActionCard("Notifications", "Android notification settings", Icons.Rounded.Notifications, Modifier.fillMaxWidth()) { open(context, Settings.ACTION_APP_NOTIFICATION_SETTINGS) }
        Spacer(Modifier.height(10.dp))
        ActionCard("Privacy", "No analytics or tracking SDKs", Icons.Rounded.Settings, Modifier.fillMaxWidth()) { open(context, Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:${context.packageName}") }
    }
}

@Composable
private fun ActionCard(title: String, value: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Row(modifier.clip(RoundedCornerShape(22.dp)).background(Card).clickable { onClick() }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).clip(CircleShape).background(Card2), Alignment.Center) { Icon(icon, null, tint = Accent, modifier = Modifier.size(20.dp)) }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = Muted, fontSize = 12.sp)
            Text(value, color = Accent, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ActionTile(label: String, icon: ImageVector, modifier: Modifier, active: Boolean = false, onClick: () -> Unit) {
    Column(modifier.clip(RoundedCornerShape(22.dp)).background(if (active) Card2 else Card).clickable { onClick() }.padding(16.dp)) {
        Icon(icon, null, tint = Accent, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(14.dp))
        Text(label, color = Accent, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Text(if (active) "On" else "Tap", color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun BigButton(text: String, onClick: () -> Unit) {
    Box(Modifier.clip(RoundedCornerShape(18.dp)).background(Card2).clickable { onClick() }.padding(horizontal = 22.dp, vertical = 14.dp), Alignment.Center) {
        Text(text, color = Accent, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Dock(selected: String, onSelect: (String) -> Unit) {
    val items = listOf("Home" to Icons.Rounded.Home, "Music" to Icons.Rounded.MusicNote, "Controls" to Icons.Rounded.Brightness6, "Calendar" to Icons.Rounded.CalendarMonth, "Settings" to Icons.Rounded.Settings)
    Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(vertical = 12.dp).clip(RoundedCornerShape(28.dp)).background(Card).padding(8.dp), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
        items.forEach { (name, icon) ->
            val active = selected == name
            Box(Modifier.size(48.dp).clip(CircleShape).background(if (active) Card2 else Color.Transparent).clickable { onSelect(name) }, Alignment.Center) { Icon(icon, name, tint = if (active) Accent else Muted) }
        }
    }
}

@Composable
private fun rememberBattery(context: Context): Int {
    var level by remember { mutableStateOf(0) }
    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context, intent: Intent) {
                val value = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100).coerceAtLeast(1)
                level = (value * 100 / scale).coerceIn(0, 100)
            }
        }
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        onDispose { context.unregisterReceiver(receiver) }
    }
    return level
}

private fun open(context: Context, action: String, data: String? = null) {
    try {
        val intent = Intent(action)
        if (data != null) intent.data = android.net.Uri.parse(data)
        context.startActivity(intent)
    } catch (_: Exception) {
        if (context is Activity) context.startActivity(Intent(Settings.ACTION_SETTINGS))
    }
}

private fun mediaKey(context: Context, code: Int) {
    val audio = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audio.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, code))
    audio.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_UP, code))
}

private fun torch(context: Context, enabled: Boolean) {
    try {
        val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val id = manager.cameraIdList.firstOrNull { manager.getCameraCharacteristics(it).get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true }
        if (id != null) manager.setTorchMode(id, enabled)
    } catch (_: Exception) { }
}
