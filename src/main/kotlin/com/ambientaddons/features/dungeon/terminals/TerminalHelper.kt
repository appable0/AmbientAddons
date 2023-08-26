package com.ambientaddons.features.dungeon.terminals

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.events.GuiContainerEvent
import com.ambientaddons.utils.DungeonFloor
import com.ambientaddons.utils.Extensions.items
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraft.inventory.ContainerChest
import net.minecraft.item.EnumDyeColor
import net.minecraft.item.ItemBlock
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object TerminalHelper {

    private val colorPattern = Regex("Select all the ([\\w\\s]+) items!")
    private val letterPattern = Regex("What starts with: '(\\w)'\\?")

    @SubscribeEvent
    fun onGuiMouseClick(event: GuiContainerEvent.SlotClickEvent) {
        if (config.blockTerminalMisclicks == 0) return
        try {
            val chest = (event.container as? ContainerChest) ?: return
            val chestName = chest.lowerChestInventory.displayName.unformattedText.trim()
            val itemClicked = event.slot?.stack ?: return
            if (SBLocation.dungeonFloor?.floor != 7) return

            if (chestName == "Correct all the panes!") {
                // UChat.chat("Item clicked meta: $itemClicked.metadata")
                if (itemClicked.metadata == EnumDyeColor.LIME.metadata) {
                    cancelEvent(event)
                }
            }

            if (chestName == "Click in order!") {
                val items = chest.lowerChestInventory.items
                val clickedItems = items.count { it?.metadata == EnumDyeColor.LIME.metadata }
                val isRed = itemClicked.metadata == EnumDyeColor.RED.metadata
                val isRightStackSize = itemClicked.stackSize == clickedItems + 1
                // UChat.chat("clickedItems: $clickedItems, isRightStackSize: $isRightStackSize, isRed: $isRed")
                if (!isRed || !isRightStackSize) {
                    cancelEvent(event)
                }
                return
            }

            val letterMatch = letterPattern.matchEntire(chestName)
            if (letterMatch != null) {
                val targetLetter = letterMatch.groups[1]!!.value.lowercase()
                val clickedLetter = itemClicked.displayName.stripControlCodes()[0].lowercase()
                // UChat.chat("name: ${itemClicked.displayName.stripControlCodes()}, targetLetter: $targetLetter, clickedLetter: $clickedLetter")
                if (targetLetter != clickedLetter) {
                    cancelEvent(event)
                }
                return
            }

            val colorMatch = colorPattern.matchEntire(chestName)
            if (colorMatch != null) {
                val slotDamage = itemClicked.itemDamage
                val colorName = colorMatch.groups[1]!!.value
                val enumColorName = colorName.replace(" ", "_")
                val enumDyeColor = EnumDyeColor.valueOf(enumColorName)
                val color = if (itemClicked.item is ItemBlock) enumDyeColor.metadata else enumDyeColor.dyeDamage
                // UChat.chat("enumColorName: $enumColorName, color: $color, slotDamage: $slotDamage")
                if (slotDamage != color) {
                    cancelEvent(event)
                }
                return
            }
        } catch (e: Error) {
            UChat.chat(e.toString())
        }
    }

    private fun cancelEvent(event: GuiContainerEvent.SlotClickEvent) {
        event.isCanceled = true
        mc.thePlayer.playSound("random.pop", 1f, 0f)
    }

}