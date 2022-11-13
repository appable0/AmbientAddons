package com.ambientaddons.utils

import AmbientAddons.Companion.mc

object Chat {
    fun getChatBreak(): String = mc.ingameGUI?.chatGUI?.chatWidth?.let {
        "§9§m" + "-".repeat(it / mc.fontRendererObj.getStringWidth("-"))
    } ?: ""
}
