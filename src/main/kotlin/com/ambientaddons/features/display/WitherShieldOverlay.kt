package com.ambientaddons.features.display

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Alignment
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.render.OverlayUtils
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.dungeon.TextStyle
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.ceil
import kotlin.math.roundToInt

object WitherShieldOverlay {
    private var witherImpactEndTime = 0L

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return
        if (!SBLocation.inSkyblock || config.witherShieldDisplay == 0) return
        if (config.trapperCooldown != 0 && SBLocation.area == Area.FarmingIslands) return
        val diff = ((witherImpactEndTime - System.currentTimeMillis()) / 1000.0).takeIf { it >= 0 } ?: return
        val display = ceil(diff).roundToInt().toString()
        val resolution = ScaledResolution(mc)
        val x = resolution.scaledWidth / 2 + 1
        val y = resolution.scaledHeight / 2 + 10
        val style = TextStyle.fromInt(config.witherShieldDisplay - 1) ?: return
        OverlayUtils.drawString(x, y, display, style, Alignment.Center)
    }

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!SBLocation.inSkyblock || config.witherShieldDisplay == 0) return
        if (event.type == 2.toByte() && event.message.unformattedText.contains("Wither Impact")) {
            if (((witherImpactEndTime - System.currentTimeMillis()) / 1000.0) < 0) {
                witherImpactEndTime = System.currentTimeMillis() + 5000
            }
        }
    }
}