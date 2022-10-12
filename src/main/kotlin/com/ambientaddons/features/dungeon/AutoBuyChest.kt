package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import AmbientAddons.Companion.persistentData
import com.ambientaddons.events.GuiContainerEvent
import com.ambientaddons.utils.Extensions.chest
import com.ambientaddons.utils.Extensions.enchants
import com.ambientaddons.utils.Extensions.items
import com.ambientaddons.utils.Extensions.lore
import com.ambientaddons.utils.Extensions.skyblockID
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.Extensions.withModPrefix
import com.ambientaddons.utils.LocationUtils
import gg.essential.universal.UChat
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object AutoBuyChest {
    private const val BUY_SLOT_INDEX = 31
    private const val KISMET_SLOT_INDEX = 50
    private var rewardChest: RewardChest? = null
    private var hasOpenedChest = false
    private var hasLookedAtChest = false

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        hasOpenedChest = false
    }

    @SubscribeEvent
    fun onSlotClick(event: GuiContainerEvent.SlotClickEvent) {
        UChat.chat(event.slotId)
        if (rewardChest == null) return
        if (event.slotId == BUY_SLOT_INDEX) {
            hasOpenedChest = true
        } else if (event.slotId == KISMET_SLOT_INDEX) {
            if (config.blockLowReroll && rewardChest != RewardChest.Bedrock && (rewardChest != RewardChest.Obsidian || LocationUtils.dungeonFloor.toString() != "M4")) {
                UChat.chat("§cBlocked reroll! This low-tier chest should not be rerolled.".withModPrefix())
                event.isCanceled = true
                return
            }
            if (config.autoBuyChest == 1) {
                val items = event.gui.chest?.lowerChestInventory?.items ?: return
                if (getShouldOpen(items)) {
                    UChat.chat("§cBlocked reroll! Profitable content in chest.".withModPrefix())
                    event.isCanceled = true
                }
            }
        }
    }

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        if (event.gui == null) return
        val chest = event.gui.chest?.lowerChestInventory?.name

        rewardChest = when (chest) {
            "Wood Chest" -> RewardChest.Wood
            "Gold Chest" -> RewardChest.Gold
            "Emerald Chest" -> RewardChest.Emerald
            "Diamond Chest" -> RewardChest.Diamond
            "Obsidian Chest" -> RewardChest.Obsidian
            "Bedrock Chest" -> RewardChest.Bedrock
            else -> null
        }

        hasLookedAtChest = false
    }

    @SubscribeEvent
    fun onGuiDraw(event: GuiScreenEvent.DrawScreenEvent) {
        if (config.autoBuyChest != 2 || rewardChest == null || hasLookedAtChest) return
        if (rewardChest == RewardChest.Wood) return
        val chest = event.gui?.chest ?: return
        val items = chest.lowerChestInventory.items
        if (items.last() != null) {
            hasLookedAtChest = true
            if (getShouldOpen(items)) {
                mc.playerController.windowClick(chest.windowId, BUY_SLOT_INDEX, 0, 0, mc.thePlayer)
                hasOpenedChest = true
                mc.thePlayer.closeScreen()
            }
        }
    }

    private fun getShouldOpen(items: List<ItemStack?>): Boolean {
        val chestPrice =
            items[BUY_SLOT_INDEX]?.lore?.getOrNull(6)?.stripControlCodes()?.filter { it.isDigit() }?.toIntOrNull() ?: 0

        val lootItems = items.subList(9, 18).mapNotNull { itemStack ->
            if (itemStack?.skyblockID == "ENCHANTED_BOOK") {
                val enchants = itemStack.enchants
                enchants?.entries?.singleOrNull()?.let {
                    "${it.key.uppercase()}_${it.value}"
                }
            } else itemStack?.skyblockID
        }

        val maxPrice = lootItems.sumOf {
            persistentData.autoBuyItems.getOrDefault(it, 0) ?: 100000000
        }

        return chestPrice <= maxPrice
    }

    enum class RewardChest {
        Wood, Gold, Emerald, Diamond, Obsidian, Bedrock
    }

}