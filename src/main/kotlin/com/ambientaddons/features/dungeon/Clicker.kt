package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.keyBinds
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Extensions.skyblockID
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.roundToLong
import kotlin.random.Random

// credit Floppa
object Clicker {
    private var lastClickTime = System.currentTimeMillis()
    private val shortbows = listOf("TERMINATOR", "JUJU_SHORTBOW", "ITEM_SPIRIT_BOW")
    private var nextDelay = 1L

    private fun shouldClick(heldItem: ItemStack?): Boolean {
        if (!SBLocation.inSkyblock || config.autoclick == 0 || config.terminatorCps == 0) return false
        if (keyBinds["acKey"]!!.isKeyDown) return true
        return config.autoclick == 2
                && shortbows.contains(heldItem?.skyblockID)
                && mc.gameSettings.keyBindUseItem.isKeyDown
    }

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        val heldItem = mc.thePlayer?.inventory?.getCurrentItem()
        if (!shouldClick(heldItem)) return
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) >= nextDelay) {
            lastClickTime = currentTime - (currentTime - lastClickTime) % nextDelay
            nextDelay = (1000.0 / config.terminatorCps).let {
                Random.nextLong((it * 0.5).roundToLong(), (it * 1.5).roundToLong())
            }
            if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, heldItem)) {
                mc.entityRenderer.itemRenderer.resetEquippedProgress2()
            }
        }
    }
}