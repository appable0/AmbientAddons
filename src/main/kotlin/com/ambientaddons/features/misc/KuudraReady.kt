package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.Extensions.chest
import com.ambientaddons.utils.Extensions.items
import com.ambientaddons.utils.Extensions.lore
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.SBLocation
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object KuudraReady {
    private var hasClickedReady = false

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        hasClickedReady = false
    }

    @SubscribeEvent
    fun onGuiDraw(event: GuiScreenEvent.DrawScreenEvent) {
        if (!config.kuudraReady || SBLocation.area != Area.Kuudra) return
        val chest = event.gui?.chest ?: return
        val chestName = chest.lowerChestInventory.name
        if (chestName == "Ready Up" && !hasClickedReady) {
            val clickIndex = chest.lowerChestInventory.items.takeIf { it.last() != null }?.indexOfFirst {
                it?.lore?.getOrNull(0)?.stripControlCodes()?.startsWith("Click to mark yourself") == true
            } ?: return
            hasClickedReady = true
            mc.playerController.windowClick(chest.windowId, clickIndex, 2, 3, mc.thePlayer)
        }
    }
}