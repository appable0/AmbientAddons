package com.ambientaddons.utils

import AmbientAddons.Companion.mc
import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.world.WorldSettings
import java.util.*

// From SkytilsMod/Skytils, under AGPL 3.0
val NetworkPlayerInfo.text: String
    get() = mc.ingameGUI.tabList.getPlayerName(this)

object TabListUtils {
    private val playerInfoOrdering = object : Ordering<NetworkPlayerInfo>() {
        override fun compare(p_compare_1_: NetworkPlayerInfo?, p_compare_2_: NetworkPlayerInfo?): Int {
            val scorePlayerTeam = p_compare_1_?.playerTeam
            val scorePlayerTeam1 = p_compare_2_?.playerTeam
            if (p_compare_1_ != null) {
                if (p_compare_2_ != null) {
                    return ComparisonChain.start().compareTrueFirst(
                        p_compare_1_.gameType != WorldSettings.GameType.SPECTATOR,
                        p_compare_2_.gameType != WorldSettings.GameType.SPECTATOR
                    ).compare(
                        if (scorePlayerTeam != null) scorePlayerTeam.registeredName else "",
                        if (scorePlayerTeam1 != null) scorePlayerTeam1.registeredName else ""
                    ).compare(p_compare_1_.gameProfile.name, p_compare_2_.gameProfile.name).result()
                }
                return 0
            }
            return -1
        }
    }

    fun fetchTabEntries(): List<NetworkPlayerInfo> =
        if (mc.thePlayer == null) emptyList() else playerInfoOrdering.sortedCopy(
            mc.thePlayer.sendQueue.playerInfoMap
        )

    fun uuidInTabList(uuid: String): Boolean {
        if (mc.isSingleplayer) { return false }
        return mc.netHandler.playerInfoMap.any { it.gameProfile.id.equals(UUID.fromString(uuid)) }
    }

    fun nameInTabList(name: String): Boolean {
        if (mc.isSingleplayer) { return false }
        return mc.netHandler.playerInfoMap.any { it.gameProfile.name.equals(name) }
    }
}