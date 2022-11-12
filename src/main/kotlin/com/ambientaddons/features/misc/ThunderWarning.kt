package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Extensions.items
import com.ambientaddons.utils.Extensions.skyblockID
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object ThunderWarning {
    private var hadChargedThunderBottle = false
    private var ticks = 0

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        hadChargedThunderBottle = false
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!config.thunderWarning || !SBLocation.inSkyblock || event.phase != TickEvent.Phase.START) return
        if (ticks % 10 == 0) {
            if (hasChargedThunderBottle() && !hadChargedThunderBottle) {
                mc.ingameGUI.displayTitle("§dThunder charged!", null, 5, 40, 5)
                mc.ingameGUI.displayTitle(null, "", 5, 40, 5)
                mc.ingameGUI.displayTitle(null, null, 5, 40, 5)
                mc.thePlayer.playSound("random.orb", 1f, 0.5f)
            }
            hadChargedThunderBottle = hasChargedThunderBottle()
        }
        ticks++
    }

    private fun hasChargedThunderBottle(): Boolean =
        mc.thePlayer?.inventory?.items?.any { it?.skyblockID == "THUNDER_IN_A_BOTTLE" } == true

}