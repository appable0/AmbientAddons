package com.ambientaddons.events

import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraftforge.fml.common.eventhandler.Event

class HitBlockEvent(val blockPos: BlockPos, val face: EnumFacing) : Event()