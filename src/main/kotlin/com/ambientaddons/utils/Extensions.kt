package com.ambientaddons.utils

import AmbientAddons.Companion.mc
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemSkull
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.BlockPos
import net.minecraft.util.StringUtils
import net.minecraft.util.Vec3
import java.util.*

object Extensions {
    fun String.substringBetween(start: String, end: String): String {
        return this
            .substringAfter("(", "")
            .substringBefore(")", "")
    }

    fun String.stripControlCodes(): String {
        return StringUtils.stripControlCodes(this)
    }

    fun String.cleanSB(): String {
        return StringUtils.stripControlCodes(this)
            ?.toCharArray()
            ?.filter { it.code in 21..126 }
            ?.joinToString("")
            ?: ""
    }

    fun String.withModPrefix(): String = "§b§lAmbient §7» §r${this}"

    fun String.renderWidth(): Int = mc.fontRendererObj.getStringWidth(this)

    val GuiScreen.chest: ContainerChest?
        get() = (this as? GuiChest)?.inventorySlots as? ContainerChest

    val IInventory.items: List<ItemStack?>
        get() = (0 until this.sizeInventory).map { this.getStackInSlot(it) }


    private val ItemStack.extraAttributes: NBTTagCompound?
        get() {
            if (!this.hasTagCompound()) return null
            return this.getSubCompound("ExtraAttributes", false) ?: return null
        }

    val ItemStack.skyblockID: String?
        get() = this.extraAttributes?.let {
            if (!it.hasKey("id", 8)) return null
            return it.getString("id")
        }

    val ItemStack.itemQuality: Int?
        get() = this.extraAttributes?.let {
            if (!it.hasKey("baseStatBoostPercentage", 3)) return null
            return it.getInteger("baseStatBoostPercentage")
        }

    val ItemStack.stars: Int?
        get() = this.extraAttributes?.let {
            return when {
                it.hasKey("dungeon_item_level", 3) -> it.getInteger("dungeon_item_level")
                it.hasKey("upgrade_level", 3) -> it.getInteger("upgrade_level")
                else -> null
            }
        }

    val ItemStack.enchants: Map<String, Int>?
        get() = this.extraAttributes?.let { extraAttributes ->
            if (!extraAttributes.hasKey("enchantments", 10)) return null
            val enchantments = extraAttributes.getCompoundTag("enchantments")
            enchantments.keySet.associateWith { enchantments.getInteger(it) }
        }

    val ItemStack.lore: List<String>?
        get() {
            if (!this.hasTagCompound()) return null
            val displayTag = this.getSubCompound("display", false) ?: return null
            if (!displayTag.hasKey("Lore", 9)) return null
            val loreList = displayTag.getTagList("Lore", 8)
            return (0 until loreList.tagCount()).map { loreList.getStringTagAt(it) }
        }

    val ItemStack.recomb: Int?
        get() = this.extraAttributes?.let {
            if (!it.hasKey("rarity_upgrades", 3)) return null
            return it.getInteger("rarity_upgrades")
        }

    val ItemStack.texture: String?
        get() {
            if (this.item !is ItemSkull) return null
            val nbt = this.tagCompound ?: return null
            if (!nbt.hasKey("SkullOwner", 10)) return null
            val base64texture = nbt
                .getCompoundTag("SkullOwner")
                .getCompoundTag("Properties")
                .getTagList("textures", 10)
                .getCompoundTagAt(0)
                .getString("Value")

            val json = Json.parseToJsonElement(Base64.getDecoder().decode(base64texture).decodeToString())
            val texture = json.jsonObject["textures"]!!.jsonObject["SKIN"]!!.jsonObject["url"]!!.jsonPrimitive.content
            return texture.substringAfterLast("/")
        }

    fun Vec3.toBlockPos(): BlockPos = BlockPos(this.xCoord - 0.5, this.yCoord - 0.5, this.zCoord - 0.5)



}
