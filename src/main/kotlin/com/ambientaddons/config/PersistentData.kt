package com.ambientaddons.config

import com.ambientaddons.gui.GuiPosition
import com.ambientaddons.utils.SalvageStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

@Serializable
data class PersistentData(
    var autoBuyItems: MutableMap<String, Int?> = mutableMapOf(
        "RECOMBOBULATOR_3000" to 5000000,
        "FIRST_MASTER_STAR" to null,
        "SECOND_MASTER_STAR" to null,
        "THIRD_MASTER_STAR" to null,
        "FOURTH_MASTER_STAR" to null,
        "FIFTH_MASTER_STAR" to null,
        "SPIRIT_WING" to null,
        "SPIRIT_STONE" to 1000000,
        "SHADOW_ASSASSIN_CHESTPLATE" to null,
        "GIANTS_SWORD" to null,
        "DARK_CLAYMORE" to null,
        "THUNDERLORD_7" to null,
        "WITHER_CHESTPLATE" to null,
        "ULTIMATE_ONE_FOR_ALL_1" to null
    ),
    var positions: MutableMap<String, GuiPosition> = mutableMapOf(),
    var salvageMap: MutableMap<String, SalvageStrategy> = mutableMapOf(
        "SLUG_BOOTS" to SalvageStrategy.Always,
        "MOOGMA_LEGGINGS" to SalvageStrategy.Always,
        "FLAMING_CHESTPLATE" to SalvageStrategy.Always,
        "TAURUS_HELMET" to SalvageStrategy.Always,
        "BLADE_OF_THE_VOLCANO" to SalvageStrategy.Always,
        "STAFF_OF_THE_VOLCANO" to SalvageStrategy.Always,
        "RAMPART_HELMET" to SalvageStrategy.Always,
        "RAMPART_CHESTPLATE" to SalvageStrategy.Always,
        "RAMPART_LEGGINGS" to SalvageStrategy.Always,
        "RAMPART_BOOTS" to SalvageStrategy.Always,
        "SWORD_OF_BAD_HEALTH" to SalvageStrategy.Always,
        "ARACHNE_HELMET" to SalvageStrategy.Always,
        "ARACHNE_CHESTPLATE" to SalvageStrategy.Always,
        "ARACHNE_LEGGINGS" to SalvageStrategy.Always,
        "ARACHNE_BOOTS" to SalvageStrategy.Always,
        "WITHER_CLOAK_SWORD" to SalvageStrategy.Block,
        "DARK_CLAYMORE" to SalvageStrategy.Block,
        "GIANTS_SWORD" to SalvageStrategy.Block,
        "WITHER_HELMET" to SalvageStrategy.Block,
        "WITHER_CHESTPLATE" to SalvageStrategy.Block,
        "WITHER_LEGGINGS" to SalvageStrategy.Block,
        "WITHER_BOOTS" to SalvageStrategy.Block,
        "ICE_SPRAY_WAND" to SalvageStrategy.Block,
        "SHADOW_ASSASSIN_HELMET" to SalvageStrategy.Block,
        "SHADOW_ASSASSIN_CHESTPLATE" to SalvageStrategy.Block,
        "SHADOW_ASSASSIN_LEGGINGS" to SalvageStrategy.Block,
        "SHADOW_ASSASSIN_BOOTS" to SalvageStrategy.Block,
    )
) {

    fun save() {
        configFile.writeText(Json.encodeToString(this))
    }

    companion object {
        private val configFile: File = File(AmbientAddons.configDirectory,"data.json")

        fun load(): PersistentData {
            val data = if (!configFile.exists()) {
                configFile.createNewFile()
                PersistentData()
            } else configFile.runCatching {
                Json.decodeFromString<PersistentData>(this.readText())
            }.getOrNull() ?: PersistentData()
            return data.apply {
                this.save()
            }
        }
    }
}