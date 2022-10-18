package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.render.EntityUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.passive.*
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object Trapper {
    private var color: Color? = null
    private val trapperRegex =
        Regex("^§e\\[NPC\\] Trevor The Trapper§f: §rYou can find your §(?<color>[0-9a-f])\\w+ §fanimal near the §(?<locationColor>[0-9a-f])(?<location>[\\w ]+)§f.§r$")
    private val animals =
        listOf(EntityCow::class, EntityPig::class, EntitySheep::class, EntityCow::class, EntityChicken::class, EntityRabbit::class)
    private val animalHp: List<Float?> = listOf(100F, 200F, 500F, 1000F, 1024F, 2048F)

    fun isTrapperAnimal(entity: Entity): Boolean =
        entity.ticksExisted >= 10 && (entity::class in animals) && animalHp.contains((entity as? EntityLiving)?.maxHealth)

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (SBLocation.area != Area.FarmingIslands || event.message == null || event.type == 2.toByte()) return
        if (config.autoTrapper) {
            val siblings = event.message.siblings ?: return
            if (siblings.getOrNull(0)?.unformattedText == "Accept the trapper's task to hunt the animal?") {
                val command = siblings.getOrNull(3)?.chatStyle?.chatClickEvent?.value ?: return
                mc.thePlayer.sendChatMessage(command)
            }
        }
        val matchResult = event.message.formattedText.let { trapperRegex.find(it) }
        if (matchResult != null) {
            val colorCode = (matchResult.groups as? MatchNamedGroupCollection)?.get("color")?.value ?: return
            color = Color(mc.fontRendererObj.getColorCode(colorCode.single()))
        } else if (
            event.message.formattedText.startsWith("§r§aKilling the animal rewarded you ") ||
            event.message.formattedText.startsWith("§r§aYour mob died randomly, you are rewarded ")
        ) {
            color = null
        }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (SBLocation.area != Area.FarmingIslands || !config.trapperEsp) return
        mc.theWorld.loadedEntityList.forEach {
            if (color != null && isTrapperAnimal(it)) {
                val renderManager = mc.renderManager
                val x = it.lastTickPosX + (it.posX - it.lastTickPosX) * event.partialTicks - renderManager.viewerPosX
                val y = it.lastTickPosY + (it.posY - it.lastTickPosY) * event.partialTicks - renderManager.viewerPosY
                val z = it.lastTickPosZ + (it.posZ - it.lastTickPosZ) * event.partialTicks - renderManager.viewerPosZ
                val entityHeight = it.entityBoundingBox.maxY - it.entityBoundingBox.minY
                EntityUtils.drawEntityBox(
                    it,
                    color!!,
                    outline = true,
                    fill = true,
                    partialTicks = event.partialTicks,
                    esp = true
                )
                EntityUtils.renderBeaconBeam(x - 0.5, y + entityHeight, z - 0.5, color!!, 1F, event.partialTicks, true)
            }
        }
    }
}