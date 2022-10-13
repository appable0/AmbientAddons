package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import com.ambientaddons.utils.SkyBlock

object IgnoreCarpet {
    fun shouldIgnoreCarpet(): Boolean {
        if (!SkyBlock.inSkyblock) return false
        return if (AmbientAddons.isInitialized()) config.ignoreCarpet else false
    }
}