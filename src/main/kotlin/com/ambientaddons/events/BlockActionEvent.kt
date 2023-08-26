package com.ambientaddons.events

import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class BlockActionEvent(val pos: BlockPos, val id: Int, val param: Int): Event()

