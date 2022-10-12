package com.ambientaddons.config

import gg.essential.vigilance.Vigilant
import java.awt.Color
import java.io.File


object Config : Vigilant(
    File(AmbientAddons.configDirectory, "config.toml"),
    AmbientAddons.metadata.name
) {
    var blockLowReroll = false
    var autoBuyChest = 0

    var cancelInteractions = false
    var closeSecretChests = false

    init {
         category("Pre/Post Dungeon") {
            subcategory("Chest QOL") {
                switch (
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
        }

        category("Dungeon") {
            subcategory("Miscellaneous QOL") {
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
            }
        }
    }
}