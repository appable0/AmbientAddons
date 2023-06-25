package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.SBLocation
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object NPCDialogue {
    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!config.npcDialogue || event.message == null || event.type == 2.toByte()) return
        if (!event.message.unformattedText.stripControlCodes().startsWith("Select an option: ")) return
        val command = event.message.siblings.getOrNull(0)?.chatStyle?.chatClickEvent?.value ?: return
        mc.thePlayer.sendChatMessage(command)
    }
}