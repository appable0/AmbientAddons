package com.ambientaddons.features.farming

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.Extensions.texture
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.render.EntityUtils
import com.ambientaddons.utils.render.EntityUtils.exactPlayerEyeLocation
import com.ambientaddons.utils.render.EntityUtils.renderPositionVector
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

object Pests {
    val pestNames = mapOf(
        "7b50d6e6bf907fa4e3c44f465cd2c4f79124b5703a2df22fac6376b1b91703cf" to "Cricket",
        "fd40aa50905235b628e7379eb31fa45cd41b503f09721db3c437fcee39207dfc" to "Earthworm",
        "52a9fe05bc663efcd12e56a3ccc5ec035bf577b78708548b6f4ffcf1d30eccfe" to "Mosquito",
        "4b24a482a32db1ea78fb98060b0c2fa4a373cbd18a68edddeb7419455a59cda9" to "Locust",
        "65485c4b34e5b5470be94de100e61f7816f81bc5a11dfdf0eccf890172da5d0a" to "Moth",
        "9d90e777826a52461368e26d1b2e19bfa1ba582d602483e545f4124d0f731842" to "Fly",
        "a8abb471db0ab78703011979dc8b40798a941f3a4dec3ec61cbeec2af8cffe8" to "Rat",
        "be6baf6431a9daa2ca604d5a3c26e9a761d5952f0817174a4fe0b764616e21ff" to "Mite",
        "7a79d0fd677b54530961117ef84adc206e2cc5045c1344d61d776bf8ac2fe1ba" to "Slug",
        "35590d5326a65d55b2bc60c5cd194c13d6125658d3d4c60ece1d9becfacea93c" to "Beetle"
    )


    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (config.pestEspTracer == 0 || SBLocation.area != Area.Garden) return
        val esp = config.pestEspTracer >= 1
        val earthwormEntities = mutableSetOf<EntityArmorStand>()
        mc.theWorld.loadedEntityList.forEach { entity ->
            if (entity is EntityArmorStand) {
                val texture = (entity.inventory.getOrNull(4)?.texture) ?: return@forEach
                val pest = pestNames[texture] ?: return@forEach
                if (pest == "Earthworm") {
                    if (earthwormEntities.any {
                            entity.entityBoundingBox.intersectsWith(it.entityBoundingBox.expand(1.0, 1.0, 1.0))
                        }) {
                        earthwormEntities.add(entity)
                        return@forEach
                    }
                    earthwormEntities.add(entity)
                }
                val expansionXZ = if (entity.isSmall) 0.125 else 0.125
                val offset = if (entity.isSmall) 0.125f else 0.7375f
                val stringOffset = if (entity.isSmall) 0.8625 else 1.725
                EntityUtils.drawEntityBox(
                    entity = entity,
                    color = Color.YELLOW,
                    outline = true,
                    fill = true,
                    esp = esp,
                    partialTicks = event.partialTicks,
                    offset = Triple(0f, offset, 0f),
                    expansion = Triple(expansionXZ, -0.6125, expansionXZ)
                )
                if (config.pestEspTracer == 2) {
                    EntityUtils.drawLineWorld(
                        mc.thePlayer.exactPlayerEyeLocation(event.partialTicks),
                        entity.renderPositionVector(event.partialTicks).addVector(0.0, stringOffset, 0.0),
                        Color.YELLOW,
                        thickness = 2f,
                        esp
                    )
                }
                EntityUtils.drawStringInWorld(
                    entity.renderPositionVector(event.partialTicks).addVector(0.0, stringOffset, 0.0),
                    pest, Color.CYAN, 1.5, true, esp
                )

            }
        }
    }
}

