package com.ambientaddons.commands

import AmbientAddons.Companion.persistentData
import com.ambientaddons.utils.Chat
import com.ambientaddons.utils.Extensions.withModPrefix
import com.ambientaddons.utils.SalvageStrategy
import gg.essential.universal.UChat

object SalvageCommand {
    fun processCommand(args: List<String>) {
        when (args.getOrNull(0)) {
            "auto" -> {
                val item = args[1]
                val added = persistentData.salvageMap.put(item, SalvageStrategy.Always) != SalvageStrategy.Always
                persistentData.save()
                UChat.chat((if (added) "§aAdded item §a§l${item}." else "§cItem already added.").withModPrefix())
            }
            "allow" -> {
                val item = args[1]
                val allowed = persistentData.salvageMap.put(item, SalvageStrategy.Allow) != SalvageStrategy.Allow
                persistentData.save()
                UChat.chat((if (allowed) "§aAllowing item §a§l${item}." else "§cItem already allowed.").withModPrefix())
            }
            "block" -> {
                val item = args[1]
                val blocked = persistentData.salvageMap.put(item, SalvageStrategy.Block) != SalvageStrategy.Block
                persistentData.save()
                UChat.chat((if (blocked) "§aBlocking item §a§l${item}." else "§cItem already blocked.").withModPrefix())
            }
            "remove" -> {
                val item = args[1]
                val removed = persistentData.salvageMap.remove(item) != null
                persistentData.save()
                UChat.chat((if (removed) "§aRemoved item §a§l${item}." else "§cItem not in list.").withModPrefix())
            }
            "list" -> {
                UChat.chat("§2§lItems §7(§aalways salvage, §eallow salvaging, §cblock salvaging§7)".withModPrefix())
                persistentData.salvageMap.forEach {
                    when (it.value) {
                        SalvageStrategy.Always -> UChat.chat(" §a${it.key}")
                        SalvageStrategy.Allow -> UChat.chat(" §e${it.key}")
                        else -> UChat.chat(" §c${it.key}")
                    }
                }
            }
            else -> {
                UChat.chat("""
                    ${Chat.getChatBreak()}
                    §b§lUsage:
                     §a/ambient salvage auto <Skyblock ID> §eto always salvage item.          
                     §a/ambient salvage allow <Skyblock ID> §eto allow salvaging item.
                     §a/ambient salvage block <Skyblock ID> §eto block salvaging item.
                     §a/ambient salvage remove <Skyblock ID> §eto removet item.
                     §a/ambient salvage list §eto view current salvage list.
                     
                    §b§lNotes:
                     §eSome items are explicitly blocked due to past item quality bugs.
                     §eIf not blocked in list, this will salvage all dungeon mob drops unless starred.
                    ${Chat.getChatBreak()}
                """.trimIndent())
            }
        }
    }
}