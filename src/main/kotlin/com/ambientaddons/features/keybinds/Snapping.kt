package com.ambientaddons.features.keybinds

import AmbientAddons.Companion.keyBinds
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SBLocation
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.tan

object Snapping {
    private var locked = false
    private var ticksDown = -1
    private var editMode = false
    private val keyBind get() = keyBinds["sensitivityKey"]!!

    @SubscribeEvent
    fun onKey(event: KeyInputEvent) {
        if (keyBind.isPressed) {
            toggleRotation()
            editMode = false
            ticksDown = 0
        }
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (ticksDown == -1) return
        if (keyBind.isKeyDown) ticksDown++
        if (ticksDown > 10) {
            editMode = true
            ticksDown = -1
        }
    }

    fun allowRotation() {
        locked = false
    }

    fun toggleRotation() {
        locked = !locked
    }

    fun shouldBlockRotate(): Boolean {
        return SBLocation.area == Area.Garden && locked
    }
}