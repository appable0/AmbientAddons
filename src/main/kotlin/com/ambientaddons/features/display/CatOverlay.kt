package com.ambientaddons.features.display

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.gui.GuiElement
import com.ambientaddons.utils.render.OverlayUtils
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.roundToInt

object CatOverlay {
    private val cat = ResourceLocation("ambientaddons", "kittycatmodule.png")
    val element = GuiElement("cat", 100, 100)

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent) {
        if (!config.cat || event.type != RenderGameOverlayEvent.ElementType.TEXT) return
        GlStateManager.pushMatrix()
        GlStateManager.enableAlpha()
        GlStateManager.color(255f, 255f, 255f, 255f)
        GlStateManager.translate(element.position.x, element.position.y, 500.0)
        mc.textureManager.bindTexture(cat)
        val renderSize = (100 * element.position.scale).roundToInt()
        OverlayUtils.drawTexturedModalRect(0, 0, renderSize, renderSize)
        GlStateManager.popMatrix()
    }
}