package com.ambientaddons.events

import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

// credit Harry282/Skyblock-Client, under AGPL 3.0
@Cancelable
class ReceivePacketEvent(val packet: Packet<*>) : Event()