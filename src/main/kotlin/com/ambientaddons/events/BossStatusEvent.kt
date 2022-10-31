package com.ambientaddons.events

import net.minecraft.entity.boss.IBossDisplayData
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class BossStatusEvent(val displayData: IBossDisplayData, val hasColorModifierIn: Boolean) : Event()