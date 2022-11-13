package com.ambientaddons.commands

import AmbientAddons.Companion.persistentData
import com.ambientaddons.features.misc.Salvage
import com.ambientaddons.utils.Chat
import com.ambientaddons.utils.Extensions.withModPrefix
import com.ambientaddons.utils.SalvageStrategy
import gg.essential.universal.UChat

object SalvageCommand {

    private val armorSuffixes = listOf("_HELMET", "_CHESTPLATE", "_LEGGINGS", "_BOOTS")

    fun processCommand(args: List<String>) {
        when (args.getOrNull(0)) {
            "auto" -> {
                val item = args[1].uppercase()
                addItem(item, SalvageStrategy.Always)
            }
            "allow" -> {
                val item = args[1].uppercase()
                addItem(item, SalvageStrategy.Allow)
            }
            "block" -> {
                val item = args[1].uppercase()
                addItem(item, SalvageStrategy.Block)
            }
            "remove" -> {
                val item = args[1].uppercase()
                if (item.endsWith("_ARMOR")) {
                    val itemWithoutSuffix = item.substringBeforeLast("_")
                    val removedAny = armorSuffixes.map {
                        persistentData.salvageMap.remove(itemWithoutSuffix + it)
                    }.any { it != null }
                    persistentData.save()
                    UChat.chat((if (removedAny) "§aRemoved armor set §a§l${item}." else "§cSet not in list.").withModPrefix())
                } else {
                    val removed = persistentData.salvageMap.remove(item) != null
                    UChat.chat((if (removed) "§aRemoved item §a§l${item}." else "§cItem not in list.").withModPrefix())
                }
            }
            "list" -> {
                val sortedMap = persistentData.salvageMap.let { salvageMap ->
                    val armorSets = salvageMap.filter { entry ->
                        if (entry.key.endsWith(armorSuffixes.first())) {
                            val itemWithoutSuffix = entry.key.substringBeforeLast("_")
                            armorSuffixes.all { salvageMap[itemWithoutSuffix + it] == entry.value }
                        } else false
                    }.map { (it.key.substringBeforeLast("_") + "_ARMOR") to it.value }.toMap()
                    (salvageMap.filter {
                        !armorSets.contains(it.key.substringBeforeLast("_") + "_ARMOR")
                    } + armorSets).toSortedMap()
                }
                UChat.chat(Chat.getChatBreak())
                UChat.chat("§b§lAlways salvage")
                UChat.chat(sortedMap.entries.filter {
                    it.value == SalvageStrategy.Always
                }.joinToString("\n") { " §a${it.key}" })
                UChat.chat("§b§lAllow salvaging")
                UChat.chat(sortedMap.entries.filter {
                    it.value == SalvageStrategy.Allow
                }.joinToString("\n") { " §e${it.key}" })
                UChat.chat("§b§lBlock salvaging")
                UChat.chat(sortedMap.entries.filter {
                    it.value == SalvageStrategy.Block
                }.joinToString("\n") { " §c${it.key}" })
                UChat.chat(Chat.getChatBreak())
            }
            else -> {
                UChat.chat(
                    """
                    ${Chat.getChatBreak()}
                    §b§lUsage:
                     §a/ambient salvage auto <Skyblock ID> §eto always salvage.          
                     §a/ambient salvage allow <Skyblock ID> §eto allow salvaging.
                     §a/ambient salvage block <Skyblock ID> §eto block salvaging.
                     §a/ambient salvage remove <Skyblock ID> §eto remove item.
                     §a/ambient salvage list §eto view current salvage list.
                     
                    §b§lNotes:
                     §eSome items are explicitly blocked due to past item quality bugs.
                     §eIf not blocked in list, this will salvage all dungeon mob drops unless starred.
                     §eArmor set ids can be combined into a single name ending with _ARMOR.
                    ${Chat.getChatBreak()}
                """.trimIndent()
                )
            }
        }
    }

    private fun addItem(item: String, strategy: SalvageStrategy) {
        if (item.endsWith("_ARMOR")) {
            val itemWithoutSuffix = item.substringBeforeLast("_")
            val addedAny = armorSuffixes.map {
                persistentData.salvageMap.put(itemWithoutSuffix + it, strategy)
            }.any { it != strategy }
            persistentData.save()
            UChat.chat((if (addedAny) "§aAdded armor set §a§l${item}." else "§cSet already added.").withModPrefix())
        } else {
            val added = persistentData.salvageMap.put(item, strategy) != strategy
            persistentData.save()
            UChat.chat((if (added) "§aAdded item §a§l${item}." else "§cItem already added.").withModPrefix())
        }
    }
}