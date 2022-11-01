package com.ambientaddons.features.kuudra

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.events.BossStatusEvent
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.SBLocation
import net.minecraft.entity.boss.BossStatus
import net.minecraft.entity.monster.EntityGhast
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object KuudraFeatures {
    private val dropships = mutableSetOf<EntityGhast>()

    private fun colorizeHealth(healthFraction: Float) = when {
        healthFraction > 0.75F -> "§a"
        healthFraction > 0.50F -> "§e"
        healthFraction > 0.25F -> "§6"
        else -> "§e"
    }

    @SubscribeEvent
    fun onBossBarSet(event: BossStatusEvent) {
        if (!config.kuudraHp || SBLocation.area != Area.Kuudra) return
        val bossData = event.displayData
        if (bossData.displayName.unformattedText.stripControlCodes().contains("Kuudra")) {
            val healthFraction = bossData.health / bossData.maxHealth
            BossStatus.healthScale = healthFraction
            BossStatus.statusBarTime = 100
            val percentString = String.format("%.1f", healthFraction * 100)
            BossStatus.bossName =
                "${bossData.displayName.formattedText} §r§8 - §r${colorizeHealth(healthFraction)} ${percentString}%"
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        dropships.clear()
    }

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        if (!config.kuudraAlert || SBLocation.area != Area.Kuudra) return
        mc.theWorld.loadedEntityList.forEach {
            if (it !is EntityGhast || dropships.contains(it)) return@forEach
            if (it.positionVector.squareDistanceTo(Vec3(-101.0, 100.0, -106.0)) < 225) {
                mc.ingameGUI.displayTitle("§cDropship!", null, 5, 40, 5)
                mc.thePlayer.playSound("random.orb", 1f, 0.5f)
                dropships.add(it)
            }
        }
    }
}