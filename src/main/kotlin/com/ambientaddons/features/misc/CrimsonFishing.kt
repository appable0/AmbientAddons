package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.render.EntityUtils
import gg.essential.universal.UChat
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.monster.EntityGuardian
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.entity.passive.EntityHorse
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemSkull
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.roundToInt

object CrimsonFishing {
    private const val sparkTexture =
        "ewogICJ0aW1lc3RhbXAiIDogMTY0MzUwNDM3MjI1NiwKICAicHJvZmlsZUlkIiA6ICI2MzMyMDgwZTY3YTI0Y2MxYjE3ZGJhNzZmM2MwMGYxZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUZWFtSHlkcmEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2IzMzI4ZDNlOWQ3MTA0MjAzMjI1NTViMTcyMzkzMDdmMTIyNzBhZGY4MWJmNjNhZmM1MGZhYTA0YjVjMDZlMSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"
    private val knownEntities = mutableSetOf<Entity>()
    private const val aidanqtUUID = "29e5272a-543f-4ba9-a3cb-cd5ec8d3dde1"

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

        for (entity in mc.theWorld.loadedEntityList) {
            val crimsonMob = crimsonMobs.find { it.isMob(entity) }
            if (config.crimsonHighlight != 0) {
                if (crimsonMob != null) {
                    EntityUtils.drawEntityBox(
                        entity = entity,
                        color = config.crimsonColor,
                        outline = true,
                        fill = false,
                        esp = config.crimsonHighlight == 2,
                        partialTicks = event.partialTicks
                    )
                } else if (isSpark(entity)) {
                    EntityUtils.drawEntityBox(
                        entity = entity,
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

            if (crimsonMob != null && config.crimsonNotify && !knownEntities.contains(entity)) {
                crimsonMob.sendNotification(entity)
                knownEntities.add(entity)
            }
        }
    }

    private const val ragnarokSkin = "ewogICJ0aW1lc3RhbXAiIDogMTc0MTA5ODExNTMwMCwKICAicHJvZmlsZUlkIiA6ICJhNzdkNmQ2YmFjOWE0NzY3YTFhNzU1NjYxOTllYmY5MiIsCiAgInByb2ZpbGVOYW1lIiA6ICIwOEJFRDUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThlMWZlMjE0YjcxZjZlYTY5YzU0MWE4NjFjNjRiYWZkYTdiZjliODVkZTVkZDE3YWIyYjZjY2QxZDMyYjAzOSIKICAgIH0KICB9Cn0="

    private val crimsonMobs = listOf(
        CrimsonMob("Jawbus") { it is EntityIronGolem },
        CrimsonMob("Thunder") { it is EntityGuardian && it.isElder },
        CrimsonMob("Ragnarok") {
            if (it is EntityHorse && it.isUndead) {
                it.riddenByEntity?.skinTexture == ragnarokSkin
            } else false
        }
    )

    private val Entity.skinTexture: String?
        get() {
            val gameProfile = (this as? EntityPlayer)?.gameProfile ?: return null
            return gameProfile.properties.entries()
                .filter { it.key == "textures" }
                .map { it.value }
                .firstOrNull { it.name == "textures" }?.value
        }


    class CrimsonMob(val name: String, val isMob: (entity: Entity) -> Boolean) {
        fun sendNotification(entity: Entity) {
            val distance = entity.positionVector.distanceTo(mc.thePlayer.positionVector).roundToInt()
            UChat.chat("§c§l$name spawned §e§l$distance blocks §c§laway!")
            mc.thePlayer.playSound("random.orb", 1f, 0.5f)
        }
    }

}