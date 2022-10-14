package com.ambientaddons.utils.dungeon

import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SkyBlock
import com.ambientaddons.utils.TabListUtils
import com.ambientaddons.utils.text
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object DungeonPlayers {
    private val playerRegex = Regex("^\\[\\d{1,3}] (?<name>[\\w]{3,16}) (?:.*)*\\((?:(?<class>Healer|Tank|Berserk|Mage|Archer) (?<level>[XVIL0]+)|(?<status>DEAD|EMPTY))\\)")
    var playerCount = 0
    private val playerSlots = listOf(5, 9, 13, 17, 1)
    private var ticks = 0

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (SkyBlock.area != Area.Dungeon) return
        if (ticks % 10 == 0) {
            val rawPlayers = TabListUtils.fetchTabEntries().let { tabEntries ->
                playerSlots.map { tabEntries[it].text.stripControlCodes() }
            }
            playerCount = rawPlayers.filter { it.isNotBlank() }.size
        }
        ticks++
    }
}