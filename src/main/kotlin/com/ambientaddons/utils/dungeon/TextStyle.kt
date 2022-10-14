package com.ambientaddons.utils.dungeon

enum class TextStyle {
    Default, Shadow, Outline;

    companion object {
        fun fromInt(number: Int): TextStyle? = when (number) {
            0 -> Default
            1 -> Shadow
            2 -> Outline
            else -> null
        }
    }
}