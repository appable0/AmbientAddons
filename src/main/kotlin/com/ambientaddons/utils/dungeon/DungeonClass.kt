package com.ambientaddons.utils.dungeon

enum class DungeonClass {
    Healer, Tank, Mage, Archer, Berserk;

    companion object {
        fun fromString(str: String?): DungeonClass? = when (str?.lowercase()) {
            "healer" -> Healer
            "tank" -> Tank
            "mage" -> Mage
            "archer" -> Archer
            "berserk" -> Berserk
            else -> null
        }
    }
}