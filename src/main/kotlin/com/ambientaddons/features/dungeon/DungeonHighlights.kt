package com.ambientaddons.features.dungeon

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.Extensions.skyblockID
import com.ambientaddons.utils.SkyBlock
import com.ambientaddons.utils.render.EntityUtils
import gg.essential.universal.UChat
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.monster.EntityCaveSpider
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.entity.passive.EntityBat
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

// modified from Harry282/Skyblock-Client
object DungeonHighlights {
    private val markedArmorStands = mutableSetOf<EntityArmorStand>()
    private val starredMobs = mutableSetOf<Entity>()
    private var nearIdkmansry = false

    private val idkmansry = UUID.fromString("93ce1cad-833f-46ff-a124-b66d2b99c4fd")

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        markedArmorStands.clear()
        starredMobs.clear()
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (SkyBlock.area != Area.Dungeon) return
        nearIdkmansry = false
        mc.theWorld.loadedEntityList.forEach { entity ->
            if (entity is EntityArmorStand && entity.customNameTag.contains("✯") && !markedArmorStands.contains(entity)) {
                if (config.starredHighlight == 0) return@forEach
                mc.theWorld.getEntitiesInAABBexcluding(
                    entity, entity.entityBoundingBox.offset(0.0, -1.0, 0.0)
                ) { isValidEntity(it) }.also {
                    if (it.isNotEmpty()) markedArmorStands.add(entity)
                }.forEach {
                    starredMobs.add(it)
                }
            } else if (entity is EntityPlayer) {
                if (entity.uniqueID == idkmansry) {
                    nearIdkmansry = true
                }
            }
        }
        mc.theWorld.loadedEntityList.forEach {
            if (it is EntityArmorStand) return@forEach
            val wasStarred = renderStarredHighlight(it, event.partialTicks)
            if (!wasStarred) {
                when (it) {
                    is EntityBat -> renderBatHighlight(it, event.partialTicks)
                    is EntityCaveSpider -> renderCellarHighlight(it, event.partialTicks)
                    is EntitySkeleton -> renderSniperHighlight(it, event.partialTicks)
                    is EntityPlayer -> renderShadowHighlight(it, event.partialTicks)
                }
            }

        }
    }

    private fun renderStarredHighlight(entity: Entity, partialTicks: Float): Boolean {
        if (config.starredHighlight == 0) return false
        if (starredMobs.contains(entity)) {
            EntityUtils.drawEntityBox(
                entity, config.starredColor, outline = true, fill = false, config.starredHighlight == 2, partialTicks
            )
            return true
        }
        return false
    }

    private fun renderShadowHighlight(entity: EntityPlayer, partialTicks: Float) {
        if (config.saHighlight == 0) return
        val boots = entity.getCurrentArmor(0)
        if (entity.heldItem?.skyblockID != "SILENT_DEATH" && (boots?.item as? ItemArmor)?.getColor(boots) != 6029470) return
        EntityUtils.drawEntityBox(
            entity, config.saColor, outline = true, fill = false, config.saHighlight == 2, partialTicks
        )
    }


    private fun renderCellarHighlight(entity: EntityCaveSpider, partialTicks: Float) {
        if (config.bestiaryHighlight == 0 || nearIdkmansry) return
        EntityUtils.drawEntityBox(
            entity, config.bestiaryColor, outline = true, fill = false, config.bestiaryHighlight == 2, partialTicks
        )
    }

    private fun renderSniperHighlight(entity: EntitySkeleton, partialTicks: Float) {
        if (config.bestiaryHighlight == 0 || nearIdkmansry) return
        if (entity.getCurrentArmor(3)?.skyblockID != "SNIPER_HELMET") return
        EntityUtils.drawEntityBox(
            entity, config.bestiaryColor, outline = true, fill = false, config.bestiaryHighlight == 2, partialTicks
        )
    }

    private fun renderBatHighlight(entity: EntityBat, partialTicks: Float) {
        if (config.batHighlight == 0) return
        if (!listOf(100F, 200F, 400F, 800F).contains(entity.maxHealth)) return
        EntityUtils.drawEntityBox(
            entity, config.batColor, outline = true, fill = false, config.batHighlight == 2, partialTicks
        )
    }

    private fun isValidEntity(entity: Entity?): Boolean {
        return when (entity) {
            is EntityEnderman -> !entity.isInvisible || (config.starredHighlight == 2)
            is EntityArmorStand -> false
            is EntityWither -> false
            is EntityPlayer -> entity.uniqueID.version() == 2 && entity != mc.thePlayer
            else -> true
        }
    }
}