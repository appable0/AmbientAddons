package com.ambientaddons.utils

import AmbientAddons.Companion.mc
import com.ambientaddons.utils.DungeonFloor.Companion.toDungeonFloor
import com.ambientaddons.utils.Extensions.cleanSB
import com.ambientaddons.utils.Extensions.stripControlCodes
import com.ambientaddons.utils.Extensions.substringBetween
import com.ambientaddons.utils.TabListUtils.fetchTabEntries
import net.minecraft.scoreboard.Score
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraftforge.client.event.ClientChatReceivedEvent
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
    val dungeonStarted get() = System.currentTimeMillis() - timestamp >= 1000
    private var timestamp = Long.MAX_VALUE

    private val entryMessages = listOf(
        "[BOSS] Bonzo: Gratz for making it this far, but I'm basically unbeatable.",
        "[BOSS] Scarf: This is where the journey ends for you, Adventurers.",
        "[BOSS] The Professor: I was burdened with terrible news recently...",
        "[BOSS] Thorn: Welcome Adventurers! I am Thorn, the Spirit! And host of the Vegan Trials!",
        "[BOSS] Livid: Welcome, you arrive right on time. I am Livid, the Master of Shadows.",
        "[BOSS] Sadan: So you made it all the way here... Now you wish to defy me? Sadan?!",
        "[BOSS] Maxor: WELL WELL WELL LOOK WHO'S HERE!"
    )

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        inSkyblock = false
        dungeonFloor = null
        area = null
        timestamp = Long.MAX_VALUE
    }

    @SubscribeEvent(receiveCanceled = true)
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (dungeonFloor == null) return
        if (entryMessages.any { it == event.message.unformattedText.stripControlCodes() }) {
            dungeonFloor?.enteredBoss = true
        }
        if (event.message.unformattedText.stripControlCodes() == "Dungeon starts in 1 second.") {
            timestamp = System.currentTimeMillis()
        }
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