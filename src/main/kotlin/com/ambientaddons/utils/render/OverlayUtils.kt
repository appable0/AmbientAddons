package com.ambientaddons.utils.render

import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Alignment
import com.ambientaddons.utils.dungeon.TextStyle
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11.GL_QUADS
import java.awt.Color
import kotlin.math.roundToInt

object OverlayUtils {

    private val removeColorCodesRegex = Regex("§[0-9a-f]")

    // from Mojang
    fun renderDurabilityBar(x: Int, y: Int, percentFilled: Double) {
        val percent = percentFilled.coerceIn(0.0, 1.0)
        if (percent == 0.0) return
        val barWidth = (percentFilled * 13.0).roundToInt()
        val barColorIndex = (percentFilled * 255.0).roundToInt()
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
        GlStateManager.disableTexture2D()
        GlStateManager.disableAlpha()
        GlStateManager.disableBlend()
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        draw(worldrenderer, x + 2, y + 13, 13, 2, 0, 0, 0, 255)
        draw(worldrenderer, x + 2, y + 13, 12, 1, (255 - barColorIndex) / 4, 64, 0, 255)
        draw(worldrenderer, x + 2, y + 13, barWidth, 1, 255 - barColorIndex, barColorIndex, 0, 255)
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
    }

    private fun draw(
        renderer: WorldRenderer, x: Int, y: Int, width: Int, height: Int, red: Int, green: Int, blue: Int, alpha: Int
    ) {
        renderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        renderer.pos((x + 0).toDouble(), (y + 0).toDouble(), 0.0).color(red, green, blue, alpha).endVertex()
        renderer.pos((x + 0).toDouble(), (y + height).toDouble(), 0.0).color(red, green, blue, alpha).endVertex()
        renderer.pos((x + width).toDouble(), (y + height).toDouble(), 0.0).color(red, green, blue, alpha).endVertex()
        renderer.pos((x + width).toDouble(), (y + 0).toDouble(), 0.0).color(red, green, blue, alpha).endVertex()
        Tessellator.getInstance().draw()
    }

    fun drawString(x: Int, y: Int, str: String, textStyle: TextStyle, alignment: Alignment) {
        val text = "§r$str"
        val startX = when (alignment) {
            Alignment.Left -> x
            Alignment.Center -> x - mc.fontRendererObj.getStringWidth(str) / 2
            Alignment.Right -> x - mc.fontRendererObj.getStringWidth(str)
        }
        when (textStyle) {
            TextStyle.Default -> mc.fontRendererObj.drawString(
                text, startX.toFloat(), y.toFloat(), -1, false
            )
            TextStyle.Shadow -> mc.fontRendererObj.drawString(
                text, startX.toFloat(), y.toFloat(), -1, true
            )
            TextStyle.Outline -> {
                val rawString = text.replace(removeColorCodesRegex, "")
                mc.fontRendererObj.drawString(rawString, startX - 1, y, -16777216)
                mc.fontRendererObj.drawString(rawString, startX + 1, y, -16777216)
                mc.fontRendererObj.drawString(rawString, startX, y - 1, -16777216)
                mc.fontRendererObj.drawString(rawString, startX, y + 1, -16777216)
                mc.fontRendererObj.drawString(text, startX, y, -1)
            }
        }
    }


    fun renderRect(x: Double, y: Double, w: Double, h: Double, color: Color) {
        if (color.alpha == 0) return
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.enableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)

        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(x, y + h, 0.0).endVertex()
        worldRenderer.pos(x + w, y + h, 0.0).endVertex()
        worldRenderer.pos(x + w, y, 0.0).endVertex()
        worldRenderer.pos(x, y, 0.0).endVertex()
        tessellator.draw()

        GlStateManager.disableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    private val tessellator: Tessellator = Tessellator.getInstance()
    private val worldRenderer: WorldRenderer = tessellator.worldRenderer

    fun drawTexturedModalRect(x: Int, y: Int, width: Int, height: Int) {
        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX)
        worldRenderer.pos(x.toDouble(), (y + height).toDouble(), 0.0).tex(0.0, 1.0).endVertex()
        worldRenderer.pos((x + width).toDouble(), (y + height).toDouble(), 0.0).tex(1.0, 1.0).endVertex()
        worldRenderer.pos((x + width).toDouble(), y.toDouble(), 0.0).tex(1.0, 0.0).endVertex()
        worldRenderer.pos(x.toDouble(), y.toDouble(), 0.0).tex(0.0, 0.0).endVertex()
        tessellator.draw()
    }

}