package com.ambientaddons.utils

import AmbientAddons.Companion.mc
import com.ambientaddons.utils.DungeonFloor.Companion.toDungeonFloor
import com.ambientaddons.utils.Extensions.cleanSB
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.Extensions.substringBetween
import com.ambientaddons.utils.TabListUtils.fetchTabEntries
import net.minecraft.scoreboard.Score
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent

object SBLocation {
    private var areaRegex = Regex("^(?:Area|Dungeon): ([\\w ].+)\$")
    var onHypixel = false
    var inSkyblock = false
    var area: Area? = null
    private var areaString: String? = null
    var dungeonFloor: DungeonFloor? = null
    var ticks = 0

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        inSkyblock = false
        dungeonFloor = null
        area = null
    }

    @SubscribeEvent
    fun onConnect(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        onHypixel = mc.runCatching {
            !event.isLocal && ((thePlayer?.clientBrand?.lowercase()?.contains("hypixel")
                ?: currentServerData?.serverIP?.lowercase()?.contains("hypixel")) == true)
        }.getOrDefault(false)
    }

    @SubscribeEvent
    fun onDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        onHypixel = false
    }

    // from Skytils, under AGPL 3.0
    fun fetchScoreboardLines(): List<String> {
        val scoreboard = mc.theWorld?.scoreboard ?: return emptyList()
        val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return emptyList()
        val scores = scoreboard.getSortedScores(objective).filter { input: Score? ->
            input != null && input.playerName != null && !input.playerName
                .startsWith("#")
        }.take(15)
        return scores.map {
            ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(it.playerName), it.playerName).cleanSB()
        }.asReversed()
    }

    // modified from Harry282/Skyblock-Client, under AGPL 3.0
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!onHypixel || event.phase != TickEvent.Phase.START) return
        if (ticks % 10 == 0) {
            val title = mc.theWorld?.scoreboard?.getObjectiveInDisplaySlot(1)?.displayName?.cleanSB()
            if (!inSkyblock) {
                inSkyblock = title?.contains("SKYBLOCK") == true
            }
            if (inSkyblock) {
                if (areaString == null) {
                    val tab = fetchTabEntries()
                    val areaString = tab.firstNotNullOfOrNull { areaRegex.find(it.text.stripControlCodes()) }?.let {
                        it.groupValues.getOrNull(1)
                    }
                    area = Area.fromString(areaString)
                }
                if (area == Area.Dungeon && dungeonFloor == null) {
                    val dungeonLine = fetchScoreboardLines().find {
                        it.run { contains("The Catacombs (") && !contains("Queue") }
                    }
                    dungeonFloor = dungeonLine?.substringBetween("(", ")")?.toDungeonFloor()
                }
            }
        }
        ticks++
    }

    override fun toString(): String =
        "onHypixel: $onHypixel, inSkyblock: $inSkyblock, location: $area, floor: $dungeonFloor"

}