package com.ambientaddons.commands

import AmbientAddons.Companion.persistentData
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
                UChat.chat("§aAdded item §a§l$item §awith ${if (newPrice == null) "no minimum price." else " minimum price §a§l$newPrice"}".withModPrefix())
            }
            "remove" -> {
                val item = args[1]
                if (persistentData.autoBuyItems.contains(item)) {
                    persistentData.autoBuyItems.remove(item)
                    persistentData.save()
                    UChat.chat("§aRemoved item §a§l$item.".withModPrefix())
                } else UChat.chat("§cItem §a§l$item does not exist!".withModPrefix())
            }
            "list" -> {
                UChat.chat("§2§lItems".withModPrefix())
                persistentData.autoBuyItems.forEach {
                    if (it.value == null) {
                        UChat.chat(" §b${it.key}")
                    } else {
                        UChat.chat(" §b${it.key} §7(max price §a${it.value} §7coins)")
                    }
                }
            }
            else -> {
                UChat.chat("§2§lUsage".withModPrefix())
                UChat.chat(" §aAdd item: §b/ambient buy add <Skyblock ID> [max allowable price]")
                UChat.chat(" §aRemove item: §b/ambient buy remove <Skyblock ID>")
                UChat.chat(" §aList: §b/ambient buy list")
            }
        }
    }
}