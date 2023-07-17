package com.ambientaddons.features.dungeon.terminals

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.events.GuiContainerEvent
import com.ambientaddons.utils.Extensions.chest
import com.ambientaddons.utils.Extensions.items
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraft.item.EnumDyeColor
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object MelodyHelper {
    private val completedStageRegex = Regex("^[\\w]{2,16} (?:completed|activated) a (?:lever|terminal|device)! \\((?:[07]/7|[08]/8)\\)")
    private var hasSaidMeowlody = false
    private var hasSaidThrottled = false
    private var isThrottled = false

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        hasSaidMeowlody = false
        hasSaidThrottled = false
        isThrottled = false
    }

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (SBLocation.dungeonFloor?.floor != 7) return
        val unformatted = event.message.unformattedText.stripControlCodes()
        if (completedStageRegex.matches(unformatted)) {
            hasSaidMeowlody = false
            hasSaidThrottled = false
            isThrottled = false
        } else if (unformatted.startsWith("This menu has been throttled!")) {
            isThrottled = true
            if (!hasSaidThrottled && config.throttledAnnouncement.isNotBlank()) {
                mc.thePlayer.sendChatMessage("/pc ${config.throttledAnnouncement}")
                hasSaidThrottled = true
            }
        }
    }

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        if (SBLocation.dungeonFloor?.floor != 7) return
        if (event.gui == null) return
        if (event.gui.chest?.lowerChestInventory?.name == "Click the button on time!") {
            if (!hasSaidMeowlody && config.melodyAnnouncement.isNotBlank()) {
                mc.thePlayer.sendChatMessage("/pc ${config.melodyAnnouncement}")
                hasSaidMeowlody = true
            }
        }
    }

    @SubscribeEvent
    fun onSlotClick(event: GuiContainerEvent.SlotClickEvent) {
        if (!config.blockMisclicks) return
        if (event.button != 0) return  // Only block left clicks (bypass with right click)
        if (SBLocation.dungeonFloor?.floor != 7) return
        val chest = event.gui.chest?.lowerChestInventory
        if (chest?.name != "Click the button on time!" || isThrottled) return
        val colors = chest.items.map { it?.itemDamage }
        val targetPaneCol = colors.indexOf(EnumDyeColor.MAGENTA.metadata)
        val movingPaneIndex = colors.indexOf(EnumDyeColor.LIME.metadata)
        val movingPaneCol = movingPaneIndex % 9
        val clickSlot = (movingPaneIndex / 9) * 9 + 7
        if (targetPaneCol != movingPaneCol) {
            event.isCanceled = true
            mc.thePlayer.playSound("random.pop", 1f, 0f)
        } else if (clickSlot != event.slot?.slotIndex) {
            event.isCanceled = true
            mc.thePlayer.playSound("random.pop", 1f, 10f)
        }
    }
}