package com.ambientaddons.config

import gg.essential.vigilance.Vigilant
import java.awt.Color
import java.io.File


object Config : Vigilant(
    File(AmbientAddons.configDirectory, "config.toml"), AmbientAddons.metadata.name
) {
    var blockLowReroll = false
    var autoBuyChest = 0
    var autoReady = 0

    var maskWarning = false

    var terminatorCps = 0
    var cancelInteractions = false
    var closeSecretChests = false
    var ignoreCarpet = false


    init {
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

        category("Displays") {
            switch(
                ::maskWarning,
                name = "Mask proc warning",
                description = "Displays a title when a spirit mask or bonzo mask procs."
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
            }
        }
    }
}