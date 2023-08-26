package com.ambientaddons.features.misc

import com.ambientaddons.config.Config.antiblind
import com.ambientaddons.utils.SBLocation
import net.minecraft.potion.Potion

object PotionHook {
    private val ignoredPotions = setOf(Potion.blindness, Potion.confusion)

    fun shouldIgnorePotion(potion: Potion): Boolean {
        return antiblind && ignoredPotions.contains(potion)
    }
}