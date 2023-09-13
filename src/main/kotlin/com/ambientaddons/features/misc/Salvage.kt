package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.events.GuiContainerEvent
import com.ambientaddons.utils.Extensions.chest
import com.ambientaddons.utils.Extensions.itemQuality
import com.ambientaddons.utils.Extensions.items
import com.ambientaddons.utils.Extensions.skyblockID
import com.ambientaddons.utils.Extensions.stars
import com.ambientaddons.utils.Extensions.recomb
import com.ambientaddons.utils.Extensions.substringBetween
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.SalvageStrategy
import com.ambientaddons.utils.render.OverlayUtils
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object Salvage {

    /*
    private var status: SalvageStatus = SalvageStatus.Idle
    private const val salvageSlot = 31
    private const val clickDelay = 300
    private const val topQualityDelay = 1000
    private var nextClickTime = System.currentTimeMillis()

    private val canClick: Boolean
        get() = (System.currentTimeMillis() - nextClickTime) >= 0


    @SubscribeEvent
    fun onGuiDraw(event: GuiScreenEvent.DrawScreenEvent) {
        if (!SBLocation.inSkyblock) return
        val chest = event.gui?.chest ?: return
        if (config.salvageMode < 3 || chest.lowerChestInventory.name != "Salvage Item") return
        val color = chest.lowerChestInventory.items.last()?.itemDamage
        if (status == SalvageStatus.Waiting && color == 5) {
            mc.playerController.windowClick(
                chest.windowId, salvageSlot, 0, 0, mc.thePlayer
            )
            status = SalvageStatus.Clicked
        } else if (status == SalvageStatus.Clicked && chest.lowerChestInventory.getStackInSlot(salvageSlot)?.itemDamage == 5) {
            status = SalvageStatus.Confirming
            nextClickTime = System.currentTimeMillis() + topQualityDelay
        } else if ((status == SalvageStatus.Clicked || status == SalvageStatus.Confirmed) && color == 14) {
            status = SalvageStatus.Idle
            nextClickTime = System.currentTimeMillis() + clickDelay
        } else if (status == SalvageStatus.Confirming && canClick) {
            mc.playerController.windowClick(
                chest.windowId, salvageSlot, 0, 0, mc.thePlayer
            )
            status = SalvageStatus.Confirmed
            nextClickTime = System.currentTimeMillis() + clickDelay
        } else if (config.salvageMode == 4 && status == SalvageStatus.Idle && canClick) {
            val salvageableItemIndex = chest.inventory.slice(54 until 90).indexOfFirst {
                if (it == null) false else getSalvageStrategy(it) == SalvageStrategy.Always
            }
            if (salvageableItemIndex != -1) {
                mc.playerController.windowClick(
                    chest.windowId, salvageableItemIndex + 54, 0, 1, mc.thePlayer
                )
                status = SalvageStatus.Waiting
            }
        }
    }

    @SubscribeEvent
    fun onContainerOpen(event: GuiOpenEvent) {
        if (!SBLocation.inSkyblock) return
        if (event.gui?.chest == null) return
        status = SalvageStatus.Idle
    }

    @SubscribeEvent
    fun onSlotClick(event: GuiContainerEvent.SlotClickEvent) {
        if (!SBLocation.inSkyblock) return
        if (config.salvageMode < 2 || event.slot == null) return
        if (!isSlotInInventory(event.gui, event.slot)) return
        if (status != SalvageStatus.Idle
            || getSalvageStrategy(event.slot.stack ?: return) == SalvageStrategy.Block
            || !canClick
        ) {
            event.isCanceled = true
            mc.thePlayer.playSound("random.pop", 1f, 0f)
        } else {
            event.isCanceled = true
            mc.playerController.windowClick(
                event.container.windowId, event.slotId, 0, 1, mc.thePlayer
            )
            status = SalvageStatus.Waiting
        }
    }


    @SubscribeEvent
    fun onDrawSlot(event: GuiContainerEvent.DrawSlotEvent) {
        if (!SBLocation.inSkyblock) return
        if (config.salvageMode == 0 || !isSlotInInventory(event.gui, event.slot)) return
        val color = when (getSalvageStrategy(event.slot.stack ?: return)) {
            SalvageStrategy.Always -> Color.GREEN
            SalvageStrategy.Allow -> Color.YELLOW
            else -> return
        }
        OverlayUtils.renderRect(
            event.slot.xDisplayPosition.toDouble(), event.slot.yDisplayPosition.toDouble(), 16.0, 16.0, color
        )
    }

    private enum class SalvageStatus {
        Idle, Waiting, Clicked, Confirming, Confirmed
    }

    private fun getSalvageStrategy(item: ItemStack): SalvageStrategy {
        val skyblockId = item.skyblockID ?: return SalvageStrategy.Block
        AmbientAddons.persistentData.salvageMap[skyblockId]?.let { return it }
        return when {
            item.stars != null -> SalvageStrategy.Block
            item.itemQuality == 50 -> if (config.topQualityStrategy) SalvageStrategy.Always else SalvageStrategy.Allow
            (item.recomb == 1) && (item.itemQuality != null) -> if (config.recombStrategy) SalvageStrategy.Always else SalvageStrategy.Allow
            item.itemQuality != null -> SalvageStrategy.Always
            else -> SalvageStrategy.Block
        }
    }

    private fun isSlotInInventory(gui: GuiContainer, slot: Slot): Boolean =
        gui.chest?.lowerChestInventory?.let { it.name == "Salvage Item" && it != slot.inventory } ?: false


     */
}