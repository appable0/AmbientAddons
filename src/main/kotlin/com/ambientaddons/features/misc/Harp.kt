package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.Extensions.chest
import com.ambientaddons.utils.Extensions.items
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object Harp {
    val locations = listOf(Area.Park, Area.PrivateIsland)
    var lastValues = listOf<Boolean>()

    @SubscribeEvent
    fun onGuiDraw(event: GuiScreenEvent.DrawScreenEvent) {
        if (!config.harp || !locations.contains(SBLocation.area)) return
        val chest = event.gui.chest ?: return
        if (!chest.lowerChestInventory.name.startsWith("Harp -")) return
        val blocks = chest.lowerChestInventory.items.map { it?.item as? ItemBlock }
        val values = blocks.map { it?.block == Blocks.wool }
        if (values == lastValues) return
        lastValues = values
        val note = blocks.indexOfFirst { it?.block == Blocks.quartz_block }
        if (note != -1) {
            UChat.chat("Playing note!")
            mc.playerController.windowClick(chest.windowId, note, 2, 3, mc.thePlayer)
        }
    }
}