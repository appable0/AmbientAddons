package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.render.EntityUtils
import gg.essential.universal.UChat
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.monster.EntityGuardian
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.entity.passive.EntityMooshroom
import net.minecraft.item.ItemSkull
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

object CrimsonFishing {
    private const val sparkTexture =
        "ewogICJ0aW1lc3RhbXAiIDogMTY0MzUwNDM3MjI1NiwKICAicHJvZmlsZUlkIiA6ICI2MzMyMDgwZTY3YTI0Y2MxYjE3ZGJhNzZmM2MwMGYxZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUZWFtSHlkcmEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2IzMzI4ZDNlOWQ3MTA0MjAzMjI1NTViMTcyMzkzMDdmMTIyNzBhZGY4MWJmNjNhZmM1MGZhYTA0YjVjMDZlMSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"
    private val knownEntities = mutableSetOf<Entity>()

    fun isSpark(entity: Entity): Boolean {
        return (entity is EntityArmorStand) && run {
            entity.heldItem?.let {
                if (it.item !is ItemSkull) return false
                val nbt = it.tagCompound ?: return false
                if (!nbt.hasKey("SkullOwner", 10)) return false
                sparkTexture == nbt
                    .getCompoundTag("SkullOwner")
                    .getCompoundTag("Properties")
                    .getTagList("textures", 10)
                    .getCompoundTagAt(0)
                    .getString("Value")
            } ?: false
        }
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        knownEntities.clear()
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (SBLocation.area != Area.CrimsonIsle) return
        mc.theWorld.loadedEntityList.forEach {
            if (it is EntityIronGolem || it is EntityGuardian) {
                if (config.crimsonHighlight != 0) {
                    EntityUtils.drawEntityBox(
                        entity = it,
                        color = config.crimsonColor,
                        outline = true,
                        fill = false,
                        esp = config.crimsonHighlight == 2,
                        partialTicks = event.partialTicks
                    )
                }
                if (config.crimsonNotify && !knownEntities.contains(it)) {
                    val distance = sqrt((it.posX - mc.thePlayer.posX).pow(2) +
                            (it.posY - mc.thePlayer.posY).pow(2) +
                            (it.posZ - mc.thePlayer.posZ).pow(2)
                    )
                    if (it is EntityIronGolem) {
                        UChat.chat("\n§c§lA legendary creature has been spotted nearby... Lord Jawbus has arrived.")
                        mc.thePlayer.playSound("random.orb", 1f, 0.5f)
                    } else {
                        UChat.chat("\n§c§lYou hear a massive rumble as a Thunder emerges nearby.")
                        mc.thePlayer.playSound("random.orb", 1f, 0.5f)
                    }
                    UChat.chat("§cSpotted §6§l${distance.roundToInt()} §cblocks away.\n")

                }
                knownEntities.add(it)
            } else if (isSpark(it) && config.crimsonHighlight != 0) {
                EntityUtils.drawEntityBox(
                    entity = it,
                    color = config.crimsonColor,
                    outline = true,
                    fill = true,
                    esp = config.crimsonHighlight == 2,
                    partialTicks = event.partialTicks,
                    offset = Triple(-0.2F, -0.5F, -0.1F),
                    expansion = Triple(-0.1, -0.85, -0.1)
                )
            }
        }
    }


}