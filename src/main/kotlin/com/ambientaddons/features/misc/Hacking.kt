package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.Extensions.chest
import com.ambientaddons.utils.Extensions.items
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object Hacking {
    val slots = listOf(11, 21, 31, 41, 51)
    var lastValues = emptyList<Int>()

    @SubscribeEvent
    fun onGuiDraw(event: GuiScreenEvent.DrawScreenEvent) {
        if (!config.hacking || SBLocation.area != Area.Rift) return
        val chest = event.gui.chest ?: return
        if (!chest.lowerChestInventory.name.startsWith("Hacking")) return
        val items = chest.lowerChestInventory.items
        val values = slots.map { items[it]?.stackSize ?: 0 }
        if (values == lastValues) return
        lastValues = values
        val slot = items.subList(2, 7).withIndex().indexOfFirst {
            it.value?.displayName?.startsWith("§c") ?: false && it.value?.stackSize == values[it.index]
        }
        if (slot != -1) {
            mc.playerController.windowClick(chest.windowId, slots[slot], 2, 3, mc.thePlayer)
        }
    }

}