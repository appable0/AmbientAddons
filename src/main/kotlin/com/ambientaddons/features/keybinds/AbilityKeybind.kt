package com.ambientaddons.features.keybinds

import AmbientAddons.Companion.keyBinds
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.SBLocation
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent

object AbilityKeybind {
    @SubscribeEvent
    fun onKey(event: InputEvent.KeyInputEvent) {
        if (!SBLocation.dungeonStarted) return
        if (keyBinds["abilityKey"]!!.isPressed) {
            mc.thePlayer.dropOneItem(true)
        }
    }
}