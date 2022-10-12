package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config

object IgnoreCarpet {
    fun shouldIgnoreCarpet(): Boolean = if (AmbientAddons.isInitialized()) {
        config.ignoreCarpet
    } else false
}