package com.ambientaddons.commands

import AmbientAddons.Companion.persistentData
import com.ambientaddons.utils.Chat
import com.ambientaddons.utils.Extensions.withModPrefix
import gg.essential.universal.UChat

object AutoBuyCommand {
    fun processCommand(args: List<String>) {
        when (args.getOrNull(0)) {
            "add" -> {
                val item = args[1]
                val newPrice = args.getOrNull(2)?.toIntOrNull()
                persistentData.autoBuyItems[item] = newPrice
                persistentData.save()
                UChat.chat("§aAdded item §a§l$item §awith ${if (newPrice == null) "no maximum price." else "maximum price §a§l$newPrice"}".withModPrefix())
            }
            "remove" -> {
                val item = args[1]
                if (persistentData.autoBuyItems.contains(item)) {
                    persistentData.autoBuyItems.remove(item)
                    persistentData.save()
                    UChat.chat("§aRemoved item §a§l$item§a.".withModPrefix())
                } else UChat.chat("§cItem §c§l$item §cdoes not exist!".withModPrefix())
            }
            "list" -> {
                UChat.chat(Chat.getChatBreak())
                UChat.chat("§b§lAutobuy List")
                persistentData.autoBuyItems.forEach {
                    if (it.value == null) {
                        UChat.chat(" §a${it.key}")
                    } else {
                        UChat.chat(" §a${it.key} §e(maximum price §a§l${it.value} §ecoins)")
                    }
                }
                UChat.chat(Chat.getChatBreak())
            }
            else -> {
                UChat.chat("""
                    ${Chat.getChatBreak()}
                    §b§lUsage:
                     §a/ambient buy add <Skyblock ID> [max price] §eto add an item.           
                     §a/ambient buy remove <Skyblock ID> §eto remove an item.
                     §a/ambient buy list §eto view current autobuy list
                    ${Chat.getChatBreak()}
                """.trimIndent())
            }
        }
    }
}