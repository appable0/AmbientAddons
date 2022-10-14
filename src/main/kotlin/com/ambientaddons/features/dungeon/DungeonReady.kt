package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Extensions.chest
import com.ambientaddons.utils.Extensions.items
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SkyBlock
import com.ambientaddons.utils.dungeon.DungeonPlayers
import gg.essential.universal.UChat
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object DungeonReady {
    private const val START_SLOT_INDEX = 13
    private val READY_SLOTS = listOf(2, 3, 4, 5, 6)

    private var hasClickedReady = false
    private var hasClickedStart = false

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        hasClickedReady = false
        hasClickedStart = false
    }

    @SubscribeEvent
    fun onGuiDraw(event: GuiScreenEvent.DrawScreenEvent) {
        if (config.autoReady == 0 || SkyBlock.area != Area.Dungeon) return
        val chest = event.gui?.chest ?: return
        val chestName = chest.lowerChestInventory.name
        if (chestName == "Start Dungeon?" && !hasClickedStart) {
            if (config.autoReady == 1 && DungeonPlayers.playerCount != 5) return
            hasClickedStart = true
            mc.playerController.windowClick(chest.windowId, START_SLOT_INDEX, 2, 3, mc.thePlayer)
        } else if (chestName.startsWith("Catacombs - ") && !hasClickedReady) {
            val username = mc.thePlayer.name
            val clickIndex = chest.lowerChestInventory.items.takeIf { it.last() != null }?.indexOfFirst {
                username == it?.displayName?.stripControlCodes()?.substringAfter(" ")
            }.takeIf { it != -1 } ?: return
            hasClickedReady = true
            mc.playerController.windowClick(chest.windowId, clickIndex + 9, 2, 3, mc.thePlayer)
        }
    }
}