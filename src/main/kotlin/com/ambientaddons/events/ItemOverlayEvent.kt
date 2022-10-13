package com.ambientaddons.events

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.eventhandler.Event

class ItemOverlayEvent(val item: ItemStack?, val x: Int, val y: Int) : Event()