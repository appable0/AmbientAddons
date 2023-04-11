package com.ambientaddons.features.keybinds

import AmbientAddons.Companion.keyBinds
import AmbientAddons.Companion.mc
import AmbientAddons.Companion.config
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent

object PerspectiveKeybind {
    @SubscribeEvent
    fun onKey(event: KeyInputEvent) {
        val settings = mc.gameSettings
        if (keyBinds["thirdPersonKey"]!!.isPressed) {
            settings.thirdPersonView = if (settings.thirdPersonView == 0) 1 else 0
        } else if (keyBinds["secondPersonKey"]!!.isPressed) {
            settings.thirdPersonView = if (settings.thirdPersonView == 0) 2 else 0
        } else if (keyBinds["sensitivityKey"]!!.isPressed) {
            if (settings.mouseSensitivity != 0F) {
                config.mouseSensitivity = settings.mouseSensitivity
                settings.mouseSensitivity = 0F
            }
            else settings.mouseSensitivity = config.mouseSensitivity
        } else if (keyBinds["fovKey"]!!.isPressed) {
            if (settings.fovSetting != 110F) {
                config.fovSetting = settings.fovSetting
                settings.fovSetting = 110F
            }
            else settings.fovSetting = config.fovSetting
        }
    }
}
