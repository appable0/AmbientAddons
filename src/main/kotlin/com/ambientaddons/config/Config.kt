package com.ambientaddons.config

import AmbientAddons.Companion.currentGui
import com.ambientaddons.gui.MoveGui
import gg.essential.vigilance.Vigilant
import java.awt.Color
import java.io.File


object Config : Vigilant(
    File(AmbientAddons.configDirectory, "config.toml"), AmbientAddons.metadata.name
) {


    var kuudraReady = false
    var autoTrapper = false
    var trapperEsp = false

    var crimsonNotify = false
    var crimsonColor = Color.CYAN
    var crimsonHighlight = 0

    var batHighlight = 0
    var batColor = Color.CYAN
    var saHighlight = 0
    var saColor = Color.CYAN
    var starredHighlight = 0
    var starredColor = Color.CYAN
    var bestiaryHighlight = 0
    var bestiaryColor = Color.MAGENTA

    var blockLowReroll = false
    var autoBuyChest = 0
    var autoReady = 0

    var maskWarning = false
    var cat = true
    var witherShieldDisplay = 0
    var spiritBowTimer = 0

    var shouldPing = 0
    var pingDisplay = 0

    var terminatorCps = 0
    var cancelInteractions = false
    var closeSecretChests = false
    var ignoreCarpet = false

    var melodyBlockMisclicks = false
    var melodyAnnouncement = "Meowlody on me!"
    var throttledAnnouncement = "Hi! This is Hypixel Support. We noticed that your runs are actually efficient so we’re throttling this menu. Enjoy slower runs, dipshit. Hope Goldor kills you. Meow."

    init {
        category("Misc") {
            subcategory("Kuudra") {
                switch(
                    ::kuudraReady,
                    name = "Automatically ready in Kuudra",
                    description = "Automatically clicks the ready pane when the ready GUI is opened."
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
                selector(
                    ::batHighlight,
                    name = "Bat highlight",
                    description = "Show bat secrets",
                    options = listOf("Off", "Highlight", "ESP")
                )
                color(
                    ::batColor,
                    name = "Bat highlight color",
                    description = "Color of bat secrets",
                )
            }
            subcategory("Shadow assassin highlight") {
                selector(
                    ::saHighlight,
                    name = "Shadow assassin highlight",
                    description = "Show shadow assassins (without this, they will not be highlighted even when starred.)",
                    options = listOf("Off", "Highlight", "ESP")
                )
                color(
                    ::saColor,
                    name = "Shadow assassin highlight color",
                    description = "Color of shadow assassins",
                )
            }
            subcategory("Starred mob highlight") {
                selector(
                    ::starredHighlight,
                    name = "Starred mob highlight",
                    description = "Show bat secrets",
                    options = listOf("Off", "Highlight", "ESP")
                )
                color(
                    ::starredColor,
                    name = "Starred mob highlight color",
                    description = "Color of starred mobs",
                )
            }
            subcategory("Bestiary highlight") {
                selector(
                    ::bestiaryHighlight,
                    name = "Bestiary highlight",
                    description = "Show cave spiders and snipers. Disabled automatically when idkmansry is nearby.",
                    options = listOf("Off", "Highlight", "ESP")
                )
                color(
                    ::bestiaryColor,
                    name = "Bestiary highlight color",
                    description = "Color of bestiary mobs.",
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
                    description = "Either blocks rerolls or automatically buys dungeon reward chests containing certain items.",
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
        }

        category("Displays") {
            button(
                name = "Move GUI elements",
                description = "Opens a GUI to edit locations of all GUI elements.",
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
                selector(
                    ::shouldPing,
                    name = "Enable ping",
                    description = "Enables ping in command and display. This requires sending packets to the server.",
                    options = listOf("Off", "In Skyblock", "On Hypixel", "Always")
                    )
                selector(
                    ::pingDisplay,
                    name = "Ping and TPS display",
                    description = "Displays current ping and TPS. Ping requires ",
                    options = listOf("Off", "Default", "Shadow", "Outline")
                )
            }

        }

        category("Dungeon") {
            subcategory("Miscellaneous QOL") {
                slider(
                    ::terminatorCps,
                    name = "Set terminator autoclick CPS",
                    description = "Set to 0 to disable the feature",
                    min = 0,
                    max = 50
                )
                switch(
                    ::cancelInteractions,
                    name = "Cancel block interactions",
                    description = "Cancels interactions with hoppers that prevent using item abilities."
                )
                switch(
                    ::closeSecretChests,
                    name = "Block opening secret chests",
                    description = "Cancels opening chests containing secrets."
                )
                switch(
                    ::ignoreCarpet, name = "Ignore carpet hitboxes", description = "Removes all carpet hitboxes"
                )
                switch(
                    ::melodyBlockMisclicks,
                    name = "Block misclicks on Melody terminal",
                    description = "Prevents clicking Melody terminal when not aligned."
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