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
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.DungeonFloor
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraft.inventory.ContainerChest
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
        if (SBLocation.area != Area.Dungeon || rewardChest == null) return
        if (event.slotId == BUY_SLOT_INDEX) {
            if (rewardChest == RewardChest.Wood && hasOpenedChest) {
                UChat.chat("§cBlocked purchase! You already opened a chest this run.".withModPrefix())
                event.isCanceled = true
            } else {
                hasOpenedChest = true
            }
        } else if (event.slotId == KISMET_SLOT_INDEX) {
            val isM4 = SBLocation.dungeonFloor?.floor == 4 && SBLocation.dungeonFloor?.mode == DungeonFloor.Mode.Master
            if (config.blockLowReroll && rewardChest != RewardChest.Bedrock && (rewardChest != RewardChest.Obsidian || isM4)) {
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
        if (SBLocation.area != Area.Dungeon) return
        if (event.gui == null) return
        val chest = event.gui.chest
        val chestName = chest?.lowerChestInventory?.name

        rewardChest = when (chestName) {
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
        if (SBLocation.area != Area.Dungeon || config.autoBuyChest != 2 || rewardChest == null || hasLookedAtChest) return
        val chest = event.gui?.chest ?: return
        if (rewardChest == RewardChest.Wood) {
            if (!hasOpenedChest) openChest(chest)
        } else {
            val items = chest.lowerChestInventory.items
            if (items.last() != null) {
                hasLookedAtChest = true
                if (getShouldOpen(items)) {
                    openChest(chest)
                }
            }
        }
    }

    private fun getShouldOpen(items: List<ItemStack?>): Boolean {
        val chestPrice = items[BUY_SLOT_INDEX]
            ?.lore
            ?.getOrNull(6)
            ?.stripControlCodes()
            ?.filter { it.isDigit() }
            ?.toIntOrNull()
            ?: 0

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

    fun openChest(chest: ContainerChest) {
        hasLookedAtChest = true
        mc.playerController.windowClick(chest.windowId, BUY_SLOT_INDEX, 0, 0, mc.thePlayer)
        hasOpenedChest = true
        mc.thePlayer.closeScreen()
    }

    enum class RewardChest {
        Wood, Gold, Emerald, Diamond, Obsidian, Bedrock
    }

}