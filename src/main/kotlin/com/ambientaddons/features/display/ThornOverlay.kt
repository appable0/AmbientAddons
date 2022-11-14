package com.ambientaddons.features.display

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.gui.GuiElement
import com.ambientaddons.gui.MoveGui
import com.ambientaddons.utils.Alignment
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.dungeon.TextStyle
import com.ambientaddons.utils.render.OverlayUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.roundToInt

object ThornOverlay {
    val element = GuiElement("thorn", 50, 10)
    private var lastPickedUpBow: Long = -1
    private const val bowPickedUpString = "You picked up the Spirit Bow! Use it to attack Thorn!"

    private val timeUntilBreak: Double
        get() = 20.0 - ((System.currentTimeMillis() - lastPickedUpBow) / 1000.0)

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (SBLocation.dungeonFloor?.floor != 4) return
        if (event.message.unformattedText.stripControlCodes() == bowPickedUpString && timeUntilBreak < 0) {
            lastPickedUpBow = System.currentTimeMillis()
        }
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        lastPickedUpBow = -1
    }

    private fun colorizeTime(time: Double) = when {
        (time < 3) -> "§c"
        (time < 5) -> "§e"
        else -> "§a"
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return
        val textStyle = TextStyle.fromInt(config.spiritBowTimer - 1) ?: TextStyle.Outline
        if (config.spiritBowTimer != 0 && SBLocation.dungeonFloor?.floor == 4) {
            if (timeUntilBreak > 0) {
                val timeString = "${colorizeTime(timeUntilBreak)}%.2f".format(timeUntilBreak)
                GlStateManager.pushMatrix()
                GlStateManager.translate(element.position.x, element.position.y, 500.0)
                GlStateManager.scale(element.position.scale, element.position.scale, 1.0)
                OverlayUtils.drawString(0, 0, "§bBow:", textStyle, Alignment.Left)
                OverlayUtils.drawString(50, 0, timeString, textStyle, Alignment.Right)
                GlStateManager.popMatrix()
            }
        } else if (mc.currentScreen is MoveGui) {
            val timeString = "§e4.98"
            GlStateManager.pushMatrix()
            GlStateManager.translate(element.position.x, element.position.y, 500.0)
            GlStateManager.scale(element.position.scale, element.position.scale, 1.0)
            OverlayUtils.drawString(0, 0, "§bBow:", textStyle, Alignment.Left)
            OverlayUtils.drawString(50, 0, timeString, textStyle, Alignment.Right)
            GlStateManager.popMatrix()
        }
    }
}