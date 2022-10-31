package com.ambientaddons.features.kuudra

import AmbientAddons.Companion.config
import com.ambientaddons.events.BossStatusEvent
import com.ambientaddons.gui.GuiElement
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.SBLocation
import net.minecraft.entity.boss.BossStatus
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object KuudraHp {

    fun onBossBarSet(event: BossStatusEvent) {
        if (!config.kuudraHp || SBLocation.area != Area.Kuudra) return
        val bossData = event.displayData
        if (bossData.displayName.unformattedText.stripControlCodes().contains("Kuudra")) {
            BossStatus.healthScale = bossData.health / bossData.maxHealth
            BossStatus.statusBarTime = 100
            BossStatus.bossName = "${bossData.displayName.formattedText} §r§8 - §r§d ${S}"
        }
    }
}