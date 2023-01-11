package com.ambientaddons.events

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

// credit Harry282/Skyblock-Client, under AGPL 3.0
open class GuiContainerEvent(val container: Container, val gui: GuiContainer) : Event() {
    @Cancelable
    class DrawSlotEvent(container: Container, gui: GuiContainer, val slot: Slot) : GuiContainerEvent(container, gui)

    @Cancelable
    class SlotClickEvent(container: Container, gui: GuiContainer, val slot: Slot?, val slotId: Int, val button: Int) :
        GuiContainerEvent(container, gui)
}