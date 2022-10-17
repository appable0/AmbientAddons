package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SBLocation
import net.minecraft.init.Blocks
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object CancelInteractions {
    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!SBLocation.inSkyblock) return
        if (!config.cancelInteractions || SBLocation.area == Area.PrivateIsland) return
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (mc.theWorld?.getBlockState(event.pos)?.block == Blocks.hopper) {
                event.isCanceled = true
            }
        }
    }
}