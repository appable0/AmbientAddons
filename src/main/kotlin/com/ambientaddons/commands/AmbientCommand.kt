package com.ambientaddons.commands

import AmbientAddons
import com.ambientaddons.config.Config
import com.ambientaddons.utils.Extensions.withModPrefix
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class AmbientCommand : CommandBase() {
    override fun getCommandName() = "ambientaddons"

    override fun getCommandAliases() = listOf("aa")

    override fun getCommandUsage(sender: ICommandSender?) = "/$commandName"

    override fun getRequiredPermissionLevel() = 0

    override fun processCommand(sender: ICommandSender?, args: Array<String>) {
        when (args.getOrNull(0)) {
            null -> AmbientAddons.currentGui = Config.gui()
            "location" -> UChat.chat(SBLocation.toString().withModPrefix())
            "buy" -> AutoBuyCommand.processCommand(args.drop(1))
            "salvage" -> SalvageCommand.processCommand(args.drop(1))
            else -> UChat.chat("§cUnknown argument!")
        }
    }
}