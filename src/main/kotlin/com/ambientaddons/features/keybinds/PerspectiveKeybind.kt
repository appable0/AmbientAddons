package com.ambientaddons.features.keybinds

import AmbientAddons.Companion.keyBinds
import AmbientAddons.Companion.mc
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
        }
    }
}
