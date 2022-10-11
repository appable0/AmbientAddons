package com.examplemod.commands

import ExampleMod
import com.examplemod.config.Config
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

class ExampleCommand : CommandBase() {
    override fun getCommandName() = "examplemod"

    override fun getCommandAliases() = listOf("example")

    override fun getCommandUsage(sender: ICommandSender?) = "/$commandName"

    override fun getRequiredPermissionLevel() = 0

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        sender?.addChatMessage(ChatComponentText("Example command run!"))
        ExampleMod.currentGui = Config.gui()
    }
}