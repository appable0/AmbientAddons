package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.keyBinds
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Extensions.skyblockID
import net.minecraft.client.settings.KeyBinding
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.random.Random

object Clicker {
    private var ticksElapsed = 0
    private var nextSalvation = 0

    private var nextClick = 0

    private var allowNextSwing = false
    private var allowedSwingInProgress = false

    private fun cpsToRandomizedTicks(cps: Int): Int {
        val delay = 20.0 / cps
        val integerPart = (floor(delay) + (Random.nextFloat() - 0.5) * 2).roundToInt().coerceAtLeast(0)
        val floatingPart = ((delay - integerPart) * 100)
        return integerPart + if (floatingPart > Random.nextInt(0, 100)) 1 else 0
    }

    @SubscribeEvent
    fun onMouseInput(event: MouseInputEvent) {
        val keyCode = Mouse.getEventButton() - 100
        if (Mouse.getEventButtonState() && mc.gameSettings.keyBindAttack.keyCode == keyCode) {
            allowNextSwing = true
            nextSalvation = ticksElapsed + Random.nextInt(5, 8)
        }
    }

    @SubscribeEvent
    fun onBlockInteract(event: PlayerInteractEvent) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            allowNextSwing = true
        }
    }


    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (mc.thePlayer == null) return

        // weird one tick delay
        if (allowNextSwing) {
            allowedSwingInProgress = true
            allowNextSwing = false
        } else if (allowedSwingInProgress && mc.thePlayer?.swingProgress == 0f) {
            allowedSwingInProgress = false
        }

        if (mc.currentScreen == null) {
            if (config.autoSalvation
                && mc.gameSettings.keyBindUseItem.isKeyDown
                && mc.objectMouseOver?.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK
                && mc.thePlayer?.inventory?.getCurrentItem()?.skyblockID == "TERMINATOR"
            ) {
                if (ticksElapsed >= nextSalvation) {
                    nextSalvation = ticksElapsed + Random.nextInt(5, 8)
                    KeyBinding.onTick(mc.gameSettings.keyBindAttack.keyCode)
                }
            } else {
                if (config.rightClickCps > 0 && keyBinds["acKey"]!!.isKeyDown) {
                    if (ticksElapsed >= nextClick) {
                        val delay = cpsToRandomizedTicks(config.rightClickCps)
                        nextClick = ticksElapsed + delay
                        KeyBinding.onTick(mc.gameSettings.keyBindUseItem.keyCode)
                    }
                } else if (config.leftClickCps > 0 && keyBinds["leftAcKey"]!!.isKeyDown) {
                    if (ticksElapsed >= nextClick) {
                        val delay = cpsToRandomizedTicks(config.leftClickCps)
                        nextClick = ticksElapsed + delay
                        KeyBinding.onTick(mc.gameSettings.keyBindAttack.keyCode)
                    }
                }
            }
        }
        ticksElapsed++
    }


    fun blockSwing(): Boolean {
        return config.disableTerminatorSwing
                && !allowedSwingInProgress
                && mc.thePlayer?.inventory?.getCurrentItem()?.skyblockID == "TERMINATOR"
    }

    enum class ClickerMode {
        LEFT_CLICK,
        RIGHT_CLICK
    }
}