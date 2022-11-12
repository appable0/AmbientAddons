package com.ambientaddons.utils

import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Extensions.enchants
import com.ambientaddons.utils.Extensions.extraAttributes
import com.ambientaddons.utils.Extensions.skyblockID
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.StringUtils

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

    fun String.withModPrefix(): String = "§9§lAmbient §7» §r${this}"

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

}
