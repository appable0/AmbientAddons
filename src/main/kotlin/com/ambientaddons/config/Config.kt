package com.ambientaddons.config

import gg.essential.vigilance.Vigilant
import java.awt.Color
import java.io.File


object Config : Vigilant(
    File(AmbientAddons.configDirectory, "config.toml"), AmbientAddons.metadata.name
) {

    var kuudraReady = false

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

    var witherShieldDisplay = 0

    var terminatorCps = 0
    var cancelInteractions = false
    var closeSecretChests = false
    var ignoreCarpet = false

    var melodyBlockMisclicks = false
    var melodyAnnouncement = "Meowlody on me!"
    var throttledAnnouncement = "Hi! This is Hypixel Support. We noticed that your runs are actually efficient so we’re throttling this menu. Enjoy slower runs, dipshit. Hope Goldor kills you. Meow."

    init {
        category("Misc") {
            switch(
                ::kuudraReady,
                name = "Automatically ready in Kuudra",
                description = "Automatically clicks the ready pane when the ready GUI is opened."
            )
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
            selector(
                ::witherShieldDisplay,
                name = "Wither shield display",
                description = "Displays remaining wither shield duration",
                options = listOf("Off", "Default", "Shadow", "Outline")
            )
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