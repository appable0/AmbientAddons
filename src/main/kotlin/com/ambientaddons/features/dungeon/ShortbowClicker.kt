package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Extensions.skyblockID
import com.ambientaddons.utils.SkyBlock
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.roundToLong

// credit Floppa
object ShortbowClicker {
    private var lastClickTime = System.currentTimeMillis()
    private val shortbows = listOf("TERMINATOR", "JUJU_SHORTBOW", "ITEM_SPIRIT_BOW")

    @SubscribeEvent
    fun onRender(event: RenderWorldLastEvent) {
        if (!SkyBlock.inSkyblock) return
        if (config.terminatorCps == 0) return
        if (!mc.gameSettings.keyBindUseItem.isKeyDown) return
        val itemStack = mc.thePlayer?.inventory?.getCurrentItem()
        if (itemStack?.skyblockID?.let { shortbows.contains(it) } != true) return
        val delay = (1000.0 / config.terminatorCps).roundToLong()
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) >= delay) {
            lastClickTime = currentTime - (currentTime - lastClickTime) % delay
            if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, itemStack)) {
                mc.entityRenderer.itemRenderer.resetEquippedProgress2()
            }
        }
    }
}