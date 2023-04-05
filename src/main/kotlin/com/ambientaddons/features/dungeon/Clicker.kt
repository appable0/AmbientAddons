package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.keyBinds
import AmbientAddons.Companion.mc
import com.ambientaddons.mixin.AccessorMinecraft
import com.ambientaddons.utils.Extensions.skyblockID
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.roundToLong
import kotlin.random.Random

// credit Floppa

enum class ClickerMode {
    BOW,
    KEY
}
object Clicker {
    private var lastClickTime = System.currentTimeMillis()
    private val shortbows = listOf("TERMINATOR", "JUJU_SHORTBOW", "ITEM_SPIRIT_BOW")
    private var nextDelay = 1L

    private fun clickerMode(heldItem: ItemStack?): ClickerMode? {
        if (!SBLocation.inSkyblock || config.autoclick == 0 || config.terminatorCps == 0) return null
        val isHoldingBow = shortbows.contains(heldItem?.skyblockID)
        val isHoldingKey = keyBinds["acKey"]!!.isKeyDown
        val isHoldingRightClick = config.autoclick == 2 && mc.gameSettings.keyBindUseItem.isKeyDown
        if (isHoldingKey) {
            return if (isHoldingBow) ClickerMode.BOW else ClickerMode.KEY
        } else if (isHoldingBow) {
            return if (isHoldingRightClick) ClickerMode.BOW else null
        }
        return null
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        val heldItem = mc.thePlayer?.inventory?.getCurrentItem()
        val clickMode = clickerMode(heldItem) ?: return
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) >= nextDelay) {
            lastClickTime = currentTime - (currentTime - lastClickTime) % nextDelay
            nextDelay = (1000.0 / config.terminatorCps).let {
                Random.nextLong((it * 0.5).roundToLong(), (it * 1.5).roundToLong())
            }
            if (clickMode == ClickerMode.KEY) {
                (mc as AccessorMinecraft).callRightClickMouse()
            } else if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, heldItem)) {
                mc.entityRenderer.itemRenderer.resetEquippedProgress2()
            }
        }
    }
}