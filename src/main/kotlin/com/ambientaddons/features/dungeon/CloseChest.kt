package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.events.ReceivePacketEvent
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SBLocation
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.server.S2DPacketOpenWindow
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object CloseChest {
    @SubscribeEvent
    fun onOpenWindow(event: ReceivePacketEvent) {
        if (!config.closeSecretChests || SBLocation.area != Area.Dungeon) return
        if (event.packet !is S2DPacketOpenWindow) return
        if (event.packet.windowTitle.unformattedText == "Chest") {
            mc.netHandler.networkManager.sendPacket(C0DPacketCloseWindow(event.packet.windowId))
            event.isCanceled = true
        }
    }
}
