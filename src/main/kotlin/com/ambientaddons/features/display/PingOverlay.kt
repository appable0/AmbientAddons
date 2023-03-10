package com.ambientaddons.features.display

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.events.ReceivePacketEvent
import com.ambientaddons.gui.GuiElement
import com.ambientaddons.gui.MoveGui
import com.ambientaddons.utils.Alignment
import com.ambientaddons.utils.Extensions.renderWidth
import com.ambientaddons.utils.Extensions.withModPrefix
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.dungeon.TextStyle
import com.ambientaddons.utils.render.OverlayUtils
import gg.essential.universal.UChat
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.network.play.client.C16PacketClientStatus
import net.minecraft.network.play.server.S01PacketJoinGame
import net.minecraft.network.play.server.S03PacketTimeUpdate
import net.minecraft.network.play.server.S37PacketStatistics
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import kotlin.math.max

// Modified from SkytilsMod/Skytils, under AGPL 3.0
object PingOverlay {
    val element = GuiElement("ping", 55, 20)
    private val alpha = 1 / 3.0
    private var averageTps: Double? = null
    private var tpsCount = 0
    private var lastTpsSample: Long? = null

    private var averagePing: Double? = null
    private var pingCount = 0
    private var pingStartTime: Long? = null

    private var ticks = 0
    private var isPinging = false
    private var chatNextPing = false

    @SubscribeEvent
    fun onPacketReceived(event: ReceivePacketEvent) {
        if (event.packet is S03PacketTimeUpdate) {
            val currentTime = System.currentTimeMillis()
            lastTpsSample?.let { lastTime ->
                val time = currentTime - lastTime
                val instantTps = (20000.0 / time).coerceIn(0.0, 20.0)
                tpsCount++
                averageTps = instantTps * alpha + (averageTps ?: instantTps) * (1 - alpha)
            }
            lastTpsSample = currentTime
        } else if (isPinging && event.packet is S37PacketStatistics) {
            isPinging = false
            pingStartTime?.let { startTime ->
                val instantPing = (System.nanoTime() - startTime) / 1e6
                pingCount++
                averagePing = instantPing * alpha + (averagePing ?: instantPing) * (1 - alpha)
            }
            if (chatNextPing) {
                printPing()
                chatNextPing = false
            }
        } else if (event.packet is S01PacketJoinGame) reset()
    }

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        reset()
    }

    private fun reset() {
        isPinging = false
        tpsCount = 0
        averageTps = null
        pingCount = 0
        averagePing = null
    }

    private fun printPing() {
        val pingValue = averagePing?.let { "${colorizePing(it) }%.1f".format(it) } ?: "§e?"
        val tpsValue = averageTps?.let { "${colorizeTps(it) }%.1f".format(it) } ?: "§e?"
        UChat.chat("$pingValue §7ms ($tpsValue §7tps)".withModPrefix())
    }

    fun sendPing(isFromCommand: Boolean) {
        if (config.shouldPing == 0) {
            UChat.chat("§cPing command disabled! If you are using another mod, enable override ping setting and restart.".withModPrefix())
            return
        } else if (!shouldPing()) {
            UChat.chat("§cPing command disabled in this location.".withModPrefix())
            return
        }
        if (isFromCommand) chatNextPing = true
        if (!isPinging) {
            isPinging = true
            mc.thePlayer.sendQueue.networkManager.sendPacket(C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS),
                {
                    pingStartTime = System.nanoTime()
                }
            )
        } else if (isFromCommand) {
            UChat.chat("§cAlready pinging!".withModPrefix())
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!shouldPing() || event.phase != TickEvent.Phase.START) return
        if (ticks % 40 == 0) {
            sendPing(false)
        }
        ticks++
    }

    private fun colorizeTps(tps: Double) = when {
        (tps > 19) -> "§a"
        (tps > 18) -> "§2"
        (tps > 17) -> "§e"
        (tps > 15) -> "§6"
        else -> "§c"
    }

    private fun colorizePing(ping: Double) = when {
        (ping < 50) -> "§a"
        (ping < 100) -> "§2"
        (ping < 150) -> "§e"
        (ping < 250) -> "§6"
        else -> "§c"
    }

    private fun shouldPing(): Boolean =
        mc.theWorld != null && (config.shouldPing == 3 || (config.shouldPing == 2 && SBLocation.onHypixel) || (config.shouldPing == 1 && SBLocation.inSkyblock))

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return
        val textStyle = TextStyle.fromInt(config.pingDisplay - 1) ?: TextStyle.Outline
        if (config.pingDisplay != 0) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(element.position.x, element.position.y, 500.0)
            GlStateManager.scale(element.position.scale, element.position.scale, 1.0)
            val tpsValue = averageTps?.let { "${colorizeTps(it) }%.1f".format(it) } ?: "§e?"
            if (shouldPing()) {
                val pingValue = averagePing?.let { "${colorizePing(it)}%.1f".format(it) } ?: "§e?"
                val valueWidth = max(pingValue.renderWidth(), tpsValue.renderWidth())
                OverlayUtils.drawString(0, 0, "§bPing:", textStyle, Alignment.Left)
                OverlayUtils.drawString(0, 10, "§bTPS:", textStyle, Alignment.Left)
                OverlayUtils.drawString(30 + valueWidth, 0, pingValue, textStyle, Alignment.Right)
                OverlayUtils.drawString(30 + valueWidth, 10, tpsValue, textStyle, Alignment.Right)
            } else {
                OverlayUtils.drawString(0, 0, "§bTPS:", textStyle, Alignment.Left)
                OverlayUtils.drawString(30 + tpsValue.renderWidth(), 0, tpsValue, textStyle, Alignment.Right)
            }
            GlStateManager.popMatrix()
        } else if (mc.currentScreen is MoveGui) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(element.position.x, element.position.y, 500.0)
            GlStateManager.scale(element.position.scale, element.position.scale, 1.0)
            val tpsValue = "§a19.8"
            val pingValue = "§e103.1"
            val valueWidth = max(pingValue.renderWidth(), tpsValue.renderWidth())
            OverlayUtils.drawString(0, 0, "§bPing:", textStyle, Alignment.Left)
            OverlayUtils.drawString(0, 10, "§bTPS:", textStyle, Alignment.Left)
            OverlayUtils.drawString(30 + valueWidth, 0, pingValue, textStyle, Alignment.Right)
            OverlayUtils.drawString(30 + valueWidth, 10, tpsValue, textStyle, Alignment.Right)
            GlStateManager.popMatrix()
        }
    }


}
