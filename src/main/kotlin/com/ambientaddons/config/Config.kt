package com.ambientaddons.config

import AmbientAddons.Companion.currentGui
import com.ambientaddons.gui.MoveGui
import gg.essential.vigilance.Vigilant
import java.awt.Color
import java.io.File

object Config : Vigilant(
    File(AmbientAddons.configDirectory, "config.toml"), AmbientAddons.metadata.name
) {

    var antiblind = false

    var treasureChestHighlight = 0
    var treasureChestColor = Color.CYAN

    var farmingBlockMisclicks = false
    var npcDialogue = false
    var tictactoeDelay = 200
    var salvageMode = 0
    var topQualityStrategy = false
    var hacking = false
    var harp = false
    var recombStrategy = false
    var blockTerminalMisclicks = 0

    var mouseSensitivity = 0.5F
    var fovSetting = 70F

    var kuudraReady = false
    var kuudraHp = false
    var kuudraAlert = false
    var autoTrapper = false
    var trapperEsp = false
    var trapperCooldown = 0
    var finneganActive = false

    var crimsonNotify = false
    var crimsonColor: Color = Color.CYAN
    var crimsonHighlight = 0

    var batHighlight = 0
    var batColor: Color = Color.CYAN
    var saHighlight = 0
    var saColor: Color = Color.CYAN
    var starredHighlight = 0
    var starredColor: Color = Color.CYAN
    var bestiaryHighlight = 0
    var bestiaryColor: Color = Color.MAGENTA

    var blockLowReroll = false
    var autoBuyChest = 0
    var autoReady = 0
    var customEndInfo = 0

    var maskWarning = false
    var thunderWarning = false
    var cat = true
    var witherShieldDisplay = 0
    var spiritBowTimer = 0

    var overridePing = false
    var shouldPing = 0
    var pingDisplay = 0

    var autoclick = false
    var autoSalvation = false
    var disableTerminatorSwing = true
    var rightClickCps = 0
    var leftClickCps = 0
    var cancelInteractions = false
    var closeSecretChests = false
    var ignoreCarpet = false

    var melodyBlockMisclicks = false
    var melodyAnnouncement = "Meowlody on me!"
    var throttledAnnouncement =
        "Hi! This is Hypixel Support. We noticed that your runs are actually efficient so we’re throttling this menu. Enjoy slower runs, dipshit. Hope Goldor kills you. Meow."

    init {
        category("Misc") {
            switch(
                ::antiblind,
                name = "Remove blindness and nausea",
                description = "Removes the blindness and nausea status effects."
            )
            switch(
                ::farmingBlockMisclicks,
                name = "Block crop misclicks",
                description = "Intelligent crop misclick prevention for stems, tall crops, crops without replenish, and mushrooms (using the two common mushroom layouts). Bypass with fist."
            )
            switch(
                ::npcDialogue,
                name = "NPC Dialogue clicker",
                description = "Automatically click the next option in NPC dialogue."
            )
            subcategory("Salvaging") {
                selector(
                    ::salvageMode,
                    name = "Salvaging features",
                    description = "Various modes to improve salvaging. §c/ambient salvage §7to configure.",
                    options = listOf("Off", "Highlight", "Block misclicks", "Legit autosalvage", "Unlegit autosalvage")
                )
                switch(
                    ::topQualityStrategy,
                    name = "Should salvage top quality",
                    description = "If selected, automatically salvages top-quality items. Otherwise, allows them to be salvaged but does not automatically."
                )
                switch(
                    ::recombStrategy,
                    name = "Should salvage recombed",
                    description = "If selected, automatically salvages auto-recombed dungeon drops. Otherwise, allows them to be salvaged but does not automatically."
                )
            }
            subcategory("Solvers") {
                switch(
                    ::hacking,
                    name = "Hacking for Dummies",
                    description = "Does the Hacking terminals for you. This is legit because it tells you to learn to program if you fail."
                )
                switch(
                    ::harp,
                    name = "Melody Harp",
                    description = "Does the Melody Harp for you."
                )
            }

            subcategory("Kuudra") {
                switch(
                    ::kuudraReady,
                    name = "Automatically ready in Kuudra",
                    description = "Automatically clicks the ready pane when the ready GUI is opened."
                )
                switch(
                    ::kuudraHp,
                    name = "Show Kuudra health",
                    description = "Shows Kuudra health percentage in boss bar.",
                )
                switch(
                    ::kuudraAlert,
                    name = "Kuudra dropship alert",
                    description = "Shows an alert when a dropship is approaching.",
                )
            }
            subcategory("Trevor the Trapper") {
                switch(
                    ::autoTrapper,
                    name = "Automatically start trapper quests",
                    description = "Automatically performs the useless chat message click. This is analogous to SBE's crystal hollows renew feature."
                )
                switch(
                    ::trapperEsp,
                    name = "Highlight trapper animals",
                    description = "Highlights trapper quests with a beacon beam and box. Legal, as Hypixel uses the glowing status effect for clients that support it."
                )
                switch(
                    ::finneganActive,
                    name = "Finnegan active",
                    description = "Toggle whether Finnegan is active (30 second cooldown). This will be replaced with an API check in the future."
                )
                selector(
                    ::trapperCooldown,
                    name = "Show trapper cooldown",
                    description = "Shows trapper cooldown below crosshair, replacing wither shield display on the Farming Islands. Also adds a simple alarm at end of cooldown.",
                    options = listOf("Off", "Default", "Shadow", "Outline")
                )
            }
            subcategory("Fishing features") {
                switch(
                    ::crimsonNotify,
                    name = "Crimson sea creature alert",
                    description = "Sends a message in chat when a rare sea creature is fished (within render distance).",
                )
                selector(
                    ::crimsonHighlight,
                    name = "Crimson highlight",
                    description = "Highlight rare Crimson sea creatures and Thunder sparks.",
                    options = listOf("Off", "Highlight", "ESP")
                )
                color(
                    ::crimsonColor,
                    name = "Crimson highlight color",
                    description = "Color of rare Crimson fishing highlight.",
                )
            }
        }

        category("Highlights") {
            subcategory("Bat highlight") {
                color(
                    ::batColor,
                    name = "Bat highlight color",
                    description = "Color of bat secrets",
                )
                selector(
                    ::batHighlight,
                    name = "Bat highlight",
                    description = "Show bat secrets",
                    options = listOf("Off", "Highlight", "ESP")
                )

            }
            subcategory("Shadow assassin highlight") {
                color(
                    ::saColor,
                    name = "Shadow assassin highlight color",
                    description = "Color of shadow assassins",
                )
                selector(
                    ::saHighlight,
                    name = "Shadow assassin highlight",
                    description = "Show shadow assassins (without this, they will not be highlighted even when starred.)",
                    options = listOf("Off", "Highlight", "ESP")
                )

            }
            subcategory("Starred mob highlight") {
                color(
                    ::starredColor,
                    name = "Starred mob highlight color",
                    description = "Color of starred mobs",
                )
                selector(
                    ::starredHighlight,
                    name = "Starred mob highlight",
                    description = "Show bat secrets",
                    options = listOf("Off", "Highlight", "ESP")
                )

            }
            subcategory("Bestiary highlight") {
                color(
                    ::bestiaryColor,
                    name = "Bestiary highlight color",
                    description = "Color of bestiary mobs.",
                )
                selector(
                    ::bestiaryHighlight,
                    name = "Bestiary highlight",
                    description = "Show cave spiders and snipers.",
                    options = listOf("Off", "Highlight", "ESP")
                )
            }
        }

        category("Pre/Post Dungeon") {
            subcategory("Chest QOL") {
                switch(
                    ::blockLowReroll,
                    name = "Block rerolling low chests",
                    description = "Prevents rerolling non-Bedrock chests (or Obsidian on M4)."
                )
                selector(
                    ::autoBuyChest,
                    name = "Dungeon Reward Chests",
                    description = "Either blocks rerolls or automatically buys dungeon reward chests containing certain items. §c/ambient buy §7to configure.",
                    options = listOf("Off", "Block Reroll", "Autobuy")
                )
            }
            selector(
                ::autoReady,
                name = "Ready at the start of a dungeon",
                description = "Can be enabled always or only when 5 players are in the dungeon.",
                options = listOf("Off", "5 Players", "All")
            )
        }

        category("Notifications") {
            switch(
                ::maskWarning,
                name = "Mask proc warning",
                description = "Displays a title when a spirit mask or bonzo mask procs."
            )
            switch(
                ::thunderWarning,
                name = "Thunder bottle warning",
                description = "Displays a title when a thunder bottle is fully charged. Re-activates on entering new worlds."
            )
        }
        category("Displays") {
            button(
                name = "Move GUI elements",
                description = "Opens a GUI to edit locations of all GUI elements. §c/ambient move §7to access anywhere.",
            ) {
                currentGui = MoveGui()
            }
            switch(
                ::cat,
                name = "Cat",
                description = "Show catplague's awesome cat upgrade! Disabling is a crime.",
            )
            selector(
                ::witherShieldDisplay,
                name = "Wither shield display",
                description = "Displays remaining wither shield duration",
                options = listOf("Off", "Default", "Shadow", "Outline")
            )
            selector(
                ::spiritBowTimer,
                name = "Spirit bow break timer",
                description = "Displays time until spirit bow breaks",
                options = listOf("Off", "Default", "Shadow", "Outline")
            )
            subcategory("Ping and TPS") {
                switch(
                    ::overridePing,
                    name = "Override ping",
                    description = "Use Ambient's ping command instead of other mods' ping commands (if any exist). §cRequires restart to apply!§7"
                )
                selector(
                    ::shouldPing,
                    name = "Enable ping",
                    description = "Enables ping in command and display. This requires sending packets to the server.",
                    options = listOf("Off", "In Skyblock", "On Hypixel", "Always")
                )
                selector(
                    ::pingDisplay,
                    name = "Ping and TPS display",
                    description = "Displays current ping and TPS.",
                    options = listOf("Off", "Default", "Shadow", "Outline")
                )
            }

        }

        category("Mining") {
            subcategory("Powder Mining") {
                selector(
                    ::treasureChestHighlight,
                    name = "Treasure chest highlight",
                    description = "Highlight powder chests in the Crystal Hollows. ESP highlights all chests. Highlight mode only highlights chests that have been visible to the player at some point.",
                    options = listOf("Off", "Highlight", "ESP")
                )
                color(
                    ::treasureChestColor,
                    name = "Treasure chest highlight color",
                    description = "Color for treasure chests.",
                )
            }
        }

        category("Clicker") {
            subcategory("Terminator") {
                switch(
                    ::autoSalvation,
                    name = "Auto salvation",
                    description = "Automatically uses the salvation ability when Terminator is used. Always set at 4 CPS"
                )
                switch(
                    ::disableTerminatorSwing,
                    name = "Hide terminator swing animation",
                    description = "Disables the swing animation on terminator"
                )
            }
            subcategory("Keybind") {
                slider(
                    ::leftClickCps,
                    name = "Set left click CPS",
                    description = "Sets the average left click CPS. Actual CPS will vary due to randomization. Edit the keybind in the vanilla Minecraft controls.",
                    min = 0,
                    max = 20
                )
                slider(
                    ::rightClickCps,
                    name = "Set right click CPS",
                    description = "Sets the average right click CPS. Actual CPS will vary due to randomization. Edit the keybind in the vanilla Minecraft controls.",
                    min = 0,
                    max = 20
                )
            }

        }

        category("Dungeon") {
            selector(
                ::customEndInfo,
                name = "Custom end info",
                description = "Work-in-progress; currently will only show extra stats at the end of a run.",
                options = listOf("Off", "Extra Stats", "Custom End Info")
            )
            subcategory("Miscellaneous QOL") {
                switch(
                    ::cancelInteractions,
                    name = "Cancel block interactions",
                    description = "Cancels interactions with hoppers that prevent using item abilities."
                )
                switch(
                    ::closeSecretChests,
                    name = "Block opening secret chests",
                    description = "Cancels opening secret chest GUIs."
                )
                switch(
                    ::ignoreCarpet, name = "Ignore carpet hitboxes", description = "Removes all carpet hitboxes"
                )
            }
            subcategory("Terminals") {
                selector(
                    ::blockTerminalMisclicks,
                    name = "Block misclicks on terminals",
                    description = "Prevents clicking terminals when not correct.",
                    options = listOf("Off", "All except Melody", "All")
                )
                text(
                    ::melodyAnnouncement,
                    name = "Melody terminal announcement",
                    description = "Announces that Melody terminal was opened in party chat; leave empty to disable."
                )
                text(
                    ::throttledAnnouncement,
                    name = "Throttled terminal announcement",
                    description = "Announces that a terminal was throttled in party chat; leave empty to disable."
                )
            }
        }

    }
}