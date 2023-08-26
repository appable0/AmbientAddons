package com.ambientaddons.features.keybinds

import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SBLocation

object SensitivityHook {
    private var rotationDisabled = false

    fun allowRotation() {
        rotationDisabled = false
    }

    fun toggleRotation() {
        rotationDisabled = !rotationDisabled
    }

    fun shouldBlockRotate(): Boolean {
        return SBLocation.area == Area.Garden && rotationDisabled
    }
}