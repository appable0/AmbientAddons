package com.ambientaddons.features.keybinds

import AmbientAddons.Companion.keyBinds
import AmbientAddons.Companion.mc
import com.ambientaddons.events.MessageSentEvent
import com.ambientaddons.utils.SBLocation
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent

object SendLastMessageKeybind {
    var lastMessage: String? = null

    @SubscribeEvent
    fun onSendChat(event: MessageSentEvent) {
        if (!SBLocation.onHypixel) return
        if (event.message.startsWith("/pc", ignoreCase = true)) {
            lastMessage = event.message.runCatching {
                substring(4 until event.message.length)
            }.getOrNull()
        } else if (!event.message.startsWith("/")) {
            lastMessage = event.message
        }
    }

    @SubscribeEvent
    fun onKey(event: InputEvent.KeyInputEvent) {
        if (!SBLocation.onHypixel) return
        if (keyBinds["spamKey"]!!.isPressed && lastMessage != null) {
            mc.thePlayer.sendChatMessage("/pc $lastMessage")
        }
    }
}