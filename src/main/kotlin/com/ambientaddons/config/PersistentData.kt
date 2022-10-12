package com.ambientaddons.config

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