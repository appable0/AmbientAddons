package com.ambientaddons.features.misc

import AmbientAddons.Companion.mc
import AmbientAddons.Companion.persistentData
import com.ambientaddons.utils.Chat
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object Welcome {

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!persistentData.isFirstLoad || event.phase != TickEvent.Phase.START || !SBLocation.inSkyblock) return
        persistentData.isFirstLoad = false
        persistentData.save()
        UChat.chat("""
            ${Chat.getChatBreak()}
            §b§lThanks for installing AmbientAddons Forge!
            
             §eUse §a§l/ambient §r§eto access GUI settings.
             §eAliases: §a/aa §eor §a/ambientaddons
            
             §eTo configure auto-buy, use §a/ambient buy§e.
             §eTo configure salvage features, use §a/ambient salvage§e.
            ${Chat.getChatBreak()}
        """.trimIndent())
    }
}