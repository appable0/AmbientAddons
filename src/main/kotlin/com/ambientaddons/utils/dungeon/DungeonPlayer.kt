package com.ambientaddons.utils.dungeon

data class DungeonPlayer(val name: String) {
    var dungeonClass: DungeonClass? = null
    var isAlive: Boolean = true
}
