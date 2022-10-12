package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.LocationUtils
import net.minecraft.init.Blocks
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object CancelInteractions {
    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!config.cancelInteractions || LocationUtils.location == "Private Island") return
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (mc.theWorld?.getBlockState(event.pos)?.block == Blocks.hopper) {
                event.isCanceled = true
            }
        }
    }
}