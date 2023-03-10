package com.ambientaddons.commands

import AmbientAddons
import AmbientAddons.Companion.mc
import com.ambientaddons.config.Config
import com.ambientaddons.gui.MoveGui
import com.ambientaddons.utils.Chat
import com.ambientaddons.utils.Extensions.withModPrefix
import com.ambientaddons.utils.SBLocation
import gg.essential.universal.UChat
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class AmbientCommand : CommandBase() {
    override fun getCommandName() = "ambientaddons"

    override fun getCommandAliases() = listOf("aa", "ambient")

    override fun getCommandUsage(sender: ICommandSender?) = "/$commandName"

    override fun getRequiredPermissionLevel() = 0

    override fun processCommand(sender: ICommandSender?, args: Array<String>) {
        when (args.getOrNull(0)) {
            null -> AmbientAddons.currentGui = Config.gui()
            "location" -> UChat.chat(SBLocation.toString().withModPrefix())
            "buy" -> AutoBuyCommand.processCommand(args.drop(1))/////
            "salvage" -> SalvageCommand.processCommand(args.drop(1))
            "move" -> AmbientAddons.currentGui = MoveGui()

            else -> {
                UChat.chat("""
                    ${Chat.getChatBreak()}
                    §b§lUsage:
                     §a/ambient §eto access GUI settings.           
                     §a/ambient buy §eto edit autobuy list.
                     §a/ambient salvage §eto configure salvage features.
                     §a/ambient move §eto move display elements.
                     
                    §b§lAliases:
                     §a/aa 
                     §a/ambientaddons
                    ${Chat.getChatBreak()}
                """.trimIndent())
            }
        }
    }
}