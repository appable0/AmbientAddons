package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import com.ambientaddons.utils.SBLocation

object IgnoreCarpet {
    fun shouldIgnoreCarpet(): Boolean {
        if (!SBLocation.inSkyblock) return false
        return if (AmbientAddons.isInitialized()) config.ignoreCarpet else false
    }
}