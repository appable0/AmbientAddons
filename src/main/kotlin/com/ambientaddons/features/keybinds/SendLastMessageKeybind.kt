package com.ambientaddons.features.keybinds

import AmbientAddons.Companion.keyBinds
import AmbientAddons.Companion.mc
import com.ambientaddons.events.MessageSentEvent
import com.ambientaddons.utils.SkyBlock
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent

object SendLastMessageKeybind {
    var lastMessage: String? = null

    @SubscribeEvent
    fun onSendChat(event: MessageSentEvent) {
        if (!SkyBlock.onHypixel) return
        if (event.message.startsWith("/pc", ignoreCase = true)) {
            lastMessage = event.message.substring(4 until event.message.length)
        } else if (!event.message.startsWith("/")) {
            lastMessage = event.message
        }
    }

    @SubscribeEvent
    fun onKey(event: InputEvent.KeyInputEvent) {
        if (!SkyBlock.onHypixel) return
        if (keyBinds["spamKey"]!!.isPressed && lastMessage != null) {
            mc.thePlayer.sendChatMessage("/pc $lastMessage")
        }
    }
}