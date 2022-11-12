package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object CustomEndInfo {
    private var ticks = 0
    private var showExtraStatsTime = -1
    private var hasShownExtraStats = false

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        hasShownExtraStats = false
    }

    @SubscribeEvent(receiveCanceled = true)
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (SBLocation.area != Area.Dungeon || config.customEndInfo == 0) return
        val stripped = event.message.unformattedText.stripControlCodes().trim().replace(",", "")
        if (!hasShownExtraStats && listOf("Master Mode Catacombs - ", "The Catacombs - ").any { stripped.startsWith(it) }) {
            showExtraStatsTime = ticks + 10
            hasShownExtraStats = true
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (ticks == showExtraStatsTime) {
            mc.thePlayer?.sendChatMessage("/showextrastats")
        }
        ticks++
    }


}