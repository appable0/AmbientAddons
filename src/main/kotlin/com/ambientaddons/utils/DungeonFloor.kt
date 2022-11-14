package com.ambientaddons.utils

data class DungeonFloor(
    val mode: Mode,
    val floor: Int,
    var enteredBoss: Boolean
) {
    override fun toString(): String {
        val floorString = when {
            floor == 0 -> "E"
            mode == Mode.Normal -> "F$floor"
            else -> "M$floor"
        }
        return if (enteredBoss) "$floorString [BOSS]" else floorString
    }

    companion object {
        fun String.toDungeonFloor(): DungeonFloor? {
            if (this.isEmpty()) return null
            if (this == "E") return DungeonFloor(Mode.Normal, 0, false)
            val floorInt = this.last().digitToIntOrNull() ?: return null
            if (floorInt !in 1..7) return null
            return when (this.first()) {
                'F' -> DungeonFloor(Mode.Normal, floorInt, false)
                'M' -> DungeonFloor(Mode.Master, floorInt, false)
                else -> null
            }
        }
    }

    enum class Mode { Normal, Master }
}



