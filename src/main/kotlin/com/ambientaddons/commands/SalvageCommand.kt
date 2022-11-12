package com.ambientaddons.commands

import AmbientAddons.Companion.persistentData
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
            "remove" -> {
                val item = args[1]
                val removed = persistentData.salvageMap.remove(item) != null
                persistentData.save()
                UChat.chat((if (removed) "§aRemoving item §a§l${item}." else "§cItem not in list.").withModPrefix())
            }
            "list" -> {
                UChat.chat("§2§lItems §7(§aalways salvage, §callow salvaging§7)".withModPrefix())
                persistentData.salvageMap.forEach {
                    if (it.value == SalvageStrategy.Always) {
                        UChat.chat(" §a${it.key}")
                    } else {
                        UChat.chat(" §e${it.key}")
                    }
                }
            }
            else -> {
                UChat.chat("§2§lUsage".withModPrefix())
                UChat.chat(" §aAlways salvage item: §b/ambient salvage auto <Skyblock ID>")
                UChat.chat(" §aAllow salvaging item: §b/ambient salvage allow <Skyblock ID>")
                UChat.chat(" §aBlock salvaging item: §b/ambient remove <Skyblock ID>")
            }
        }
    }
}