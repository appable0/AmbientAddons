package com.ambientaddons.gui

import AmbientAddons.Companion.guiElements
import AmbientAddons.Companion.persistentData
import com.ambientaddons.utils.Alignment
import com.ambientaddons.utils.dungeon.TextStyle
import com.ambientaddons.utils.render.OverlayUtils
import gg.essential.universal.UResolution
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display
import java.awt.Color

class MoveGui : GuiScreen() {
    private var currentElement: GuiElement? = null
    private var clickOffsetX = 0.0
    private var clickOffsetY = 0.0

    override fun drawScreen(x: Int, y: Int, partialTicks: Float) {
        super.drawScreen(x, y, partialTicks)
        val (mouseX, mouseY) = getMouseCoordinates()
        OverlayUtils.renderRect(
            0.0, 0.0, UResolution.scaledWidth.toDouble(), UResolution.scaledHeight.toDouble(), Color(0, 0, 0, 64)
        )
        GlStateManager.pushMatrix()
        GlStateManager.translate(UResolution.scaledWidth / 2.0, UResolution.scaledHeight / 2.0, 300.0)
        OverlayUtils.drawString(0, 10, "Drag to move GUI elements.", TextStyle.Outline, Alignment.Center)
        OverlayUtils.drawString(0, 20, "Scroll inside elements to scale.", TextStyle.Outline, Alignment.Center)
        GlStateManager.popMatrix()
        guiElements.forEach { it.draw(mouseX, mouseY) }
    }

    override fun mouseClicked(x: Int, y: Int, mouseButton: Int) {
        val (mouseX, mouseY) = getMouseCoordinates()
        currentElement = guiElements.find { it.isInsideElement(mouseX, mouseY) }?.apply {
            clickOffsetX = mouseX - position.x
            clickOffsetY = mouseY - position.y
        }
        super.mouseClicked(x, y, mouseButton)
    }

    override fun mouseClickMove(x: Int, y: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        currentElement?.apply {
            val (mouseX, mouseY) = getMouseCoordinates()
            position.x = mouseX - clickOffsetX
            position.y = mouseY - clickOffsetY
            coerceIntoScreen()
        }
        super.mouseClickMove(x, y, clickedMouseButton, timeSinceLastClick)
    }

    override fun handleMouseInput() {
        super.handleMouseInput()
        val (mouseX, mouseY) = getMouseCoordinates()
        currentElement = guiElements.find { it.isInsideElement(mouseX, mouseY) }?.apply {
            clickOffsetX = mouseX - position.x
            clickOffsetY = mouseY - position.y
            val scrollAmount = Mouse.getEventDWheel()
            val oldScale = position.scale
            val newScale = (position.scale + scrollAmount / 7200.0).coerceAtLeast(0.1)
            position.x = mouseX + (newScale / oldScale) * (position.x - mouseX)
            position.y = mouseY + (newScale / oldScale) * (position.y - mouseY)
            position.scale = newScale
            coerceIntoScreen()
        }
    }
    
    private fun getMouseCoordinates(): Pair<Double, Double> {
        val mouseX = Mouse.getX() / UResolution.scaleFactor
        val mouseY = (Display.getHeight() - Mouse.getY()) / UResolution.scaleFactor
        return Pair(mouseX, mouseY)
    }

    override fun onGuiClosed() = persistentData.save()

}