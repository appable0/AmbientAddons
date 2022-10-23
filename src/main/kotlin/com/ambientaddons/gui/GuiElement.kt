package com.ambientaddons.gui

import AmbientAddons.Companion.guiElements
import AmbientAddons.Companion.persistentData
import com.ambientaddons.utils.render.OverlayUtils
import gg.essential.universal.UResolution
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color


class GuiElement(val name: String, private val width: Int, private val height: Int) {
    var position = persistentData.positions[name] ?: run {
        persistentData.positions[name] = GuiPosition(0.0, 0.0, 1.0)
        persistentData.positions[name]!!
    }

    fun isInsideElement(mouseX: Double, mouseY: Double): Boolean {
        val renderWidth = width * position.scale
        val renderHeight = height * position.scale
        val xInside = mouseX in (position.x - padding * renderWidth)..(position.x + renderWidth * (1 + padding))
        val yInside = mouseY in (position.y - padding * renderHeight)..(position.y + renderHeight * (1 + padding))
        return xInside && yInside
    }

    fun coerceIntoScreen() {
        val xRange = 0.0..(UResolution.scaledWidth - width * position.scale)
        val yRange = 0.0..(UResolution.scaledHeight - height * position.scale)
        position.x = if (xRange.isEmpty()) 0.0 else position.x.coerceIn(xRange)
        position.y = if (yRange.isEmpty()) 0.0 else position.y.coerceIn(yRange)
    }

    fun draw(mouseX: Double, mouseY: Double) {
        GlStateManager.pushMatrix()
        val renderWidth = width * position.scale
        val renderHeight = height * position.scale
        GlStateManager.translate(position.x - padding * renderWidth, position.y - padding * renderWidth, 400.0)
        val color = if (guiElements.find { it.isInsideElement(mouseX, mouseY) } == this)
            Color(255, 255, 255, 128) else Color(128, 128, 128, 128)
        OverlayUtils.renderRect(0.0, 0.0, renderWidth * (1 + padding * 2), renderHeight * (1 + padding * 2), color)
        GlStateManager.popMatrix()
    }

    companion object {
        private const val padding = 0.05
    }
}