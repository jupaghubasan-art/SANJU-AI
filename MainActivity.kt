package com.sanju.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF090D16)
private val Panel = Color(0xFF121A29)
private val Panel2 = Color(0xFF182338)
private val Accent = Color(0xFF6D8CFF)
private val Mint = Color(0xFF65E6C2)
private val Muted = Color(0xFF9AA8BD)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SanjuApp() }
    }
}

@Composable
fun SanjuApp() {
    var prompt by remember { mutableStateOf("") }
    var selectedModel by remember { mutableStateOf("Gemini") }
    var mode by remember { mutableStateOf("One-click") }
    var page by remember { mutableStateOf("Studio") }
    var notice by remember { mutableStateOf("Ready when you are.") }
    val words = remember(prompt) { prompt.trim().let { if (it.isEmpty()) 0 else it.split(Regex("\\s+")).size } }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Bg, surface = Panel, primary = Accent,
            onBackground = Color.White, onSurface = Color.White
        )
    ) {
        Scaffold(
            containerColor = Bg,
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF0E1522), contentColor = Color.White) {
                    listOf("Studio", "Projects", "Settings").forEach { item ->
                        NavigationBarItem(
                            selected = page == item,
                            onClick = { page = item },
                            icon = { Text(when(item) { "Studio" -> "✦"; "Projects" -> "▤"; else -> "⚙" }, fontSize = 18.sp) },
                            label = { Text(item) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Mint, selectedTextColor = Mint,
                                indicatorColor = Panel2, unselectedIconColor = Muted,
                                unselectedTextColor = Muted
                            )
                        )
                    }
                }
            },
            snackbarHost = {}
        ) { padding ->
            when (page) {
                "Studio" -> StudioScreen(
                    prompt, { prompt = it }, words, selectedModel, { selectedModel = it },
                    mode, { mode = it }, notice,
                    onNew = { prompt = ""; notice = "New project draft created." },
                    onGenerate = {
                        notice = if (prompt.isBlank()) "Add a master prompt first."
                        else "Stage 1 UI is ready. Story generation will be connected in Stage 3."
                    },
                    modifier = Modifier.padding(padding)
                )
                "Projects" -> SimplePage(
                    "Your projects", "Your saved stories will appear here once project storage is added in Stage 7.",
                    "No projects yet", Modifier.padding(padding)
                )
                else -> SimplePage(
                    "Settings", "SANJU AI • Version 0.1.0\n\nAPI keys, provider connections, export settings, and privacy controls will be added in later stages.",
                    "Configuration not connected yet", Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun StudioScreen(
    prompt: String, onPrompt: (String) -> Unit, words: Int,
    model: String, onModel: (String) -> Unit, mode: String, onMode: (String) -> Unit,
    notice: String, onNew: () -> Unit, onGenerate: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(44.dp).background(Brush.linearGradient(listOf(Accent, Mint)), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) { Text("S", color = Bg, fontSize = 25.sp, fontWeight = FontWeight.Black) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("SANJU AI", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.5.sp)
                Text("STORY VIDEO STUDIO", color = Muted, fontSize = 10.sp, letterSpacing = 2.sp)
            }
            Surface(color = Color(0xFF17352F), shape = CircleShape) {
                Text("● ONLINE", color = Mint, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp))
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Turn your story into a world.", fontSize = 27.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
            Text("Build classic Roblox-style stories, one scene at a time.", color = Muted, fontSize = 14.sp)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onNew, modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent),
                shape = RoundedCornerShape(14.dp)
            ) { Text("+  New project", fontWeight = FontWeight.Bold) }
            OutlinedButton(
                onClick = { onPrompt("In this town, nobody remembered the new student...") },
                modifier = Modifier.height(48.dp), shape = RoundedCornerShape(14.dp),
                border = ButtonDefaults.outlinedButtonBorder
            ) { Text("Try sample", color = Color.White) }
        }

        SectionHeader("MASTER PROMPT", "Paste your complete instructions")
        Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(18.dp)).border(1.dp, Color(0xFF26344A), RoundedCornerShape(18.dp)).padding(14.dp)) {
            OutlinedTextField(
                value = prompt, onValueChange = onPrompt,
                modifier = Modifier.fillMaxWidth().heightIn(min = 190.dp),
                placeholder = { Text("Paste your story prompt here...\n\nExample: Create an emotional Roblox story that begins with “In this town...”", color = Muted) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Accent, unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Panel, unfocusedContainerColor = Panel,
                    cursorColor = Mint, focusedTextColor = Color.White, unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(9.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$words words  •  ${prompt.length} characters", color = Muted, fontSize = 11.sp, modifier = Modifier.weight(1f))
                TextButton(onClick = { onPrompt("") }) { Text("Clear", color = Mint) }
            }
        }

        SectionHeader("AI STORY MODEL", "Choose your writing engine")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Gemini", "Claude", "GPT").forEach { item ->
                SelectCard(item, model == item, Modifier.weight(1f)) { onModel(item) }
            }
        }
        Text(
            when (model) {
                "Gemini" -> "Gemini connection will be configured in Stage 2. Free-tier access depends on Google's current limits."
                "Claude" -> "Requires your own Anthropic API key. Paid usage may apply."
                else -> "Requires your own OpenAI API key. Paid usage may apply."
            },
            color = Muted, fontSize = 11.sp
        )

        SectionHeader("GENERATION MODE", "Choose how to build your video")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SelectCard("One-click", mode == "One-click", Modifier.weight(1f), "Full workflow") { onMode("One-click") }
            SelectCard("Scene-by-scene", mode == "Scene-by-scene", Modifier.weight(1f), "Review every scene") { onMode("Scene-by-scene") }
        }

        Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(16.dp)).padding(14.dp)) {
            Text("VIDEO OUTPUT", fontSize = 11.sp, color = Muted, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("9:16  •  Vertical", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Text("1080 × 1920 target", color = Muted, fontSize = 11.sp)
            }
            Text("Actual resolution depends on the selected video provider and source clips.", color = Muted, fontSize = 10.sp)
        }

        Button(
            onClick = onGenerate, modifier = Modifier.fillMaxWidth().height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Mint, contentColor = Bg),
            shape = RoundedCornerShape(16.dp)
        ) { Text("✦   Generate story", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp) }
        Text(notice, color = Muted, fontSize = 11.sp)
        SectionHeader("RECENT PROJECTS", "Your latest work")
        Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(16.dp)).padding(16.dp)) {
            Text("No recent projects", fontWeight = FontWeight.SemiBold)
            Text("Start a new project to begin your storytelling studio.", color = Muted, fontSize = 12.sp)
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(title, color = Mint, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.4.sp)
        Text(subtitle, color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun SelectCard(title: String, selected: Boolean, modifier: Modifier = Modifier, subtitle: String? = null, onClick: () -> Unit) {
    Column(
        modifier.fillMaxWidth().background(if (selected) Color(0xFF1B2B45) else Panel, RoundedCornerShape(14.dp))
            .border(1.dp, if (selected) Accent else Color(0xFF26344A), RoundedCornerShape(14.dp))
            .clickable { onClick() }.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (selected) Text("●", color = Mint, fontSize = 10.sp)
        }
        if (subtitle != null) Text(subtitle, color = Muted, fontSize = 10.sp)
    }
}

@Composable
private fun SimplePage(title: String, body: String, status: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("SANJU AI", color = Mint, fontSize = 11.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
        Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(body, color = Muted, lineHeight = 22.sp)
        Surface(color = Panel, shape = RoundedCornerShape(16.dp)) {
            Text(status, modifier = Modifier.padding(16.dp), color = Muted, fontSize = 12.sp)
        }
    }
}
