package com.ambientaddons.commands

import com.ambientaddons.config.Config
import com.ambientaddons.features.display.PingOverlay
import com.ambientaddons.utils.Extensions.withModPrefix
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class PingCommand : CommandBase() {
    override fun getCommandName() = "ping"

    override fun getCommandAliases() = listOf("amping")

    override fun getCommandUsage(sender: ICommandSender?) = "/$commandName"

    override fun getRequiredPermissionLevel() = 0

    override fun processCommand(sender: ICommandSender?, args: Array<String>) {
        PingOverlay.sendPing(true)
    }
}