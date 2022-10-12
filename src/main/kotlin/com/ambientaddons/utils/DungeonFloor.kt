package com.ambientaddons.utils

data class DungeonFloor(
    val mode: Mode,
    val floor: Int
) {
    override fun toString(): String {
        return when {
            floor == 0 -> "E"
            mode == Mode.Normal -> "F$floor"
            else -> "M$floor"
        }
    }

    companion object {
        fun String.toDungeonFloor(): DungeonFloor? {
            if (this.isEmpty()) return null
            if (this == "E") return DungeonFloor(Mode.Normal, 0)
            val floorInt = this.last().digitToIntOrNull() ?: return null
            if (floorInt !in 1..7) return null
            return when (this.first()) {
                'F' -> DungeonFloor(Mode.Normal, floorInt)
                'M' -> DungeonFloor(Mode.Master, floorInt)
                else -> null
            }
        }
    }

    enum class Mode { Normal, Master }
}



