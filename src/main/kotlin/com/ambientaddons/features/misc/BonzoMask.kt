package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.events.ItemOverlayEvent
import com.ambientaddons.utils.Extensions.skyblockID
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.render.OverlayUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object BonzoMask {
    private var spiritMaskProc = 0L
    private var bonzoMaskProc = 0L
    private var fraggedBonzoMaskProc = 0L

    private const val secondWindString = "Second Wind Activated! Your Spirit Mask saved your life!"
    private const val bonzoString = "Your Bonzo's Mask saved your life!"
    private const val fraggedBonzoString = "Your ⚚ Bonzo's Mask saved your life!"

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!SBLocation.inSkyblock) return
        val didMaskProc = when (event.message.unformattedText.stripControlCodes()) {
            secondWindString -> {
                spiritMaskProc = System.currentTimeMillis()
                true
            }
            bonzoString -> {
                bonzoMaskProc = System.currentTimeMillis()
                true
            }
            fraggedBonzoString -> {
                fraggedBonzoMaskProc = System.currentTimeMillis()
                true
            }
            else -> false
        }
        if (config.maskWarning && didMaskProc) {
            mc.ingameGUI.displayTitle("§cMask!", null, 5, 20, 5)
        }
    }

    @SubscribeEvent
    fun onRenderItemOverlay(event: ItemOverlayEvent) {
        if (!SBLocation.inSkyblock) return
        val durability = when (event.item?.skyblockID) {
            "BONZO_MASK" -> (System.currentTimeMillis() - bonzoMaskProc) / 180000.0
            "STARRED_BONZO_MASK" -> (System.currentTimeMillis() - fraggedBonzoMaskProc) / 180000.0
            "SPIRIT_MASK" -> (System.currentTimeMillis() - spiritMaskProc) / 30000.0
            else -> 1.0
        }
        if (durability < 1.0) {
            OverlayUtils.renderDurabilityBar(event.x, event.y, durability)
        }
    }


}