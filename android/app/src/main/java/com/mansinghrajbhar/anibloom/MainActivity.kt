package com.mansinghrajbhar.anibloom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.Brightness6
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AnibloomApp() }
    }
}

private val Bg = Color(0xFF09090B)
private val Card = Color(0xFF17171A)
private val Card2 = Color(0xFF202024)
private val Accent = Color(0xFFE7E7EA)
private val Muted = Color(0xFF9B9BA3)

@Composable
fun AnibloomApp() {
    var page by remember { mutableStateOf("Home") }
    var islandExpanded by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Bg) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))
                Island(expanded = islandExpanded, onClick = { islandExpanded = !islandExpanded })
                Spacer(Modifier.height(24.dp))

                AnimatedContent(targetState = page, label = "page") { current ->
                    when (current) {
                        "Home" -> HomePage()
                        "Music" -> MusicPage()
                        "Controls" -> ControlsPage()
                        "Calendar" -> CalendarPage()
                        else -> SettingsPage()
                    }
                }
                Spacer(Modifier.weight(1f))
                Dock(selected = page, onSelect = { page = it })
            }
        }
    }
}

@Composable
private fun Island(expanded: Boolean, onClick: () -> Unit) {
    val width by animateFloatAsState(if (expanded) 0.72f else 0.42f, spring(), label = "islandWidth")
    Box(
        modifier = Modifier.fillMaxWidth(width).height(if (expanded) 92.dp else 38.dp)
            .clip(RoundedCornerShape(28.dp)).background(Color.Black).clickable { onClick() }
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        if (expanded) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(50.dp).clip(RoundedCornerShape(14.dp)).background(Card2), contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.MusicNote, null, tint = Accent)
                }
                Column(Modifier.weight(1f)) {
                    Text("Now Playing", color = Accent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Anibloom · Media controls", color = Muted, fontSize = 11.sp)
                }
                Text("▶", color = Accent, fontSize = 18.sp)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.size(7.dp).clip(CircleShape).background(Accent))
                Text("Anibloom", color = Accent, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun HomePage() {
    Column(Modifier.fillMaxWidth()) {
        Text("Good evening", color = Muted, fontSize = 14.sp)
        Text("Your desktop, alive.", color = Accent, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(22.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoCard("Battery", "87%", Icons.Rounded.BatteryFull, Modifier.weight(1f))
            InfoCard("Weather", "24°", Icons.Rounded.Brightness6, Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        InfoCard("Today", "Focus · Pomodoro ready", Icons.Rounded.CalendarMonth, Modifier.fillMaxWidth())
    }
}

@Composable
private fun MusicPage() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Music", color = Muted, fontSize = 14.sp)
        Spacer(Modifier.height(12.dp))
        Box(Modifier.size(210.dp).clip(RoundedCornerShape(30.dp)).background(Card2), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.MusicNote, null, tint = Accent, modifier = Modifier.size(72.dp))
        }
        Spacer(Modifier.height(18.dp))
        Text("Nothing playing", color = Accent, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("MediaSession integration is ready for the next port phase", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun ControlsPage() {
    Column(Modifier.fillMaxWidth()) {
        Text("Command Center", color = Accent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Toggle("Wi-Fi", Icons.Rounded.Wifi, true, Modifier.weight(1f))
            Toggle("Bluetooth", Icons.Rounded.Bluetooth, false, Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Toggle("Flash", Icons.Rounded.FlashOn, false, Modifier.weight(1f))
            Toggle("Brightness", Icons.Rounded.Brightness6, true, Modifier.weight(1f))
        }
    }
}

@Composable
private fun CalendarPage() {
    Column(Modifier.fillMaxWidth()) {
        Text("Calendar", color = Accent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))
        InfoCard("Pomodoro", "25:00 · Ready to focus", Icons.Rounded.CalendarMonth, Modifier.fillMaxWidth())
    }
}

@Composable
private fun SettingsPage() {
    Column(Modifier.fillMaxWidth()) {
        Text("Settings", color = Accent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))
        InfoCard("Island", "Size, animation and behavior", Icons.Rounded.MusicNote, Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        InfoCard("Android permissions", "Overlay · notifications · media", Icons.Rounded.Settings, Modifier.fillMaxWidth())
    }
}

@Composable
private fun InfoCard(title: String, value: String, icon: ImageVector, modifier: Modifier) {
    Row(modifier.clip(RoundedCornerShape(22.dp)).background(Card).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).clip(CircleShape).background(Card2), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Accent, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.size(12.dp))
        Column {
            Text(title, color = Muted, fontSize = 12.sp)
            Text(value, color = Accent, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun Toggle(label: String, icon: ImageVector, enabled: Boolean, modifier: Modifier) {
    Column(modifier.clip(RoundedCornerShape(22.dp)).background(if (enabled) Card2 else Card).padding(16.dp)) {
        Icon(icon, null, tint = Accent, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(14.dp))
        Text(label, color = Accent, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Text(if (enabled) "On" else "Off", color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun Dock(selected: String, onSelect: (String) -> Unit) {
    val items = listOf(
        "Home" to Icons.Rounded.Home,
        "Music" to Icons.Rounded.MusicNote,
        "Controls" to Icons.Rounded.Brightness6,
        "Calendar" to Icons.Rounded.CalendarMonth,
        "Settings" to Icons.Rounded.Settings
    )
    Row(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(vertical = 12.dp)
            .clip(RoundedCornerShape(28.dp)).background(Card).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (name, icon) ->
            val active = selected == name
            Box(
                Modifier.size(48.dp).clip(CircleShape).background(if (active) Card2 else Color.Transparent)
                    .clickable { onSelect(name) }, contentAlignment = Alignment.Center
            ) { Icon(icon, name, tint = if (active) Accent else Muted) }
        }
    }
}
