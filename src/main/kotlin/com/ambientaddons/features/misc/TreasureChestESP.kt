package com.ambientaddons.features.misc

import AmbientAddons.Companion.config
import AmbientAddons.Companion.mc
import com.ambientaddons.events.BlockActionEvent
import com.ambientaddons.events.BlockChangeEvent
import com.ambientaddons.utils.Area
import com.ambientaddons.utils.SBLocation
import com.ambientaddons.utils.render.EntityUtils
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.init.Blocks
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

object TreasureChestESP {
    private val chests = mutableMapOf<BlockPos, ChestData>()

    @SubscribeEvent
    fun onBlockChange(event: BlockChangeEvent) {
        if (SBLocation.area != Area.CrystalHollows) return
        if (event.state.block == Blocks.chest) {
            val distance = event.pos.distanceSqToCenter(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)
            if (distance <= 100 && mc.theWorld.getBlockState(event.pos).block != Blocks.chest) {
                chests[event.pos] = ChestData(System.currentTimeMillis(), false)
            }
        } else {
            chests.remove(event.pos)
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!SBLocation.inSkyblock) return
        if (event.phase != TickEvent.Phase.START) return
        chests.entries.removeAll {
            val timeExisted = it.value.timeExisted
            timeExisted >= 60 * 1000
        }
    }

    @SubscribeEvent
    fun onBlockAction(event: BlockActionEvent) {
        if (SBLocation.area != Area.CrystalHollows) return
        chests.entries.find { it.key == event.pos }?.run { value.hasClicked = true }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (SBLocation.area != Area.CrystalHollows) return
        if (config.treasureChestHighlight == 0) return

        val camera = Frustum()
        val entity = mc.renderViewEntity
        camera.setPosition(
            entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks.toDouble(),
            entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks.toDouble(),
            entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks.toDouble()
        )

        chests.entries.forEach { (pos, chest) ->
            if (config.treasureChestHighlight == 1 && !chest.hasBeenVisible) {
                val state = mc.theWorld.getBlockState(pos)
                val boundingBox = state.block.getSelectedBoundingBox(mc.theWorld, pos)
                if (camera.isBoundingBoxInFrustum(boundingBox)) {
                    val corners = boundingBox.corners
                    val eyePosition = mc.thePlayer.getPositionEyes(event.partialTicks)
                    chest.hasBeenVisible = corners.any { corner ->
                        val movingObjectPosition = mc.theWorld.rayTraceBlocks(
                            eyePosition, corner, false, true, false
                        )
                        movingObjectPosition == null
                                || movingObjectPosition.typeOfHit == MovingObjectType.MISS
                                || (movingObjectPosition.typeOfHit == MovingObjectType.BLOCK
                                    && movingObjectPosition.blockPos == pos)
                    }
                }

            }
            if (!chest.hasClicked && chest.shouldDisplay) {
                EntityUtils.drawBlockBox(
                    pos,
                    config.treasureChestColor,
                    outline = true,
                    fill = true,
                    esp = true,
                    event.partialTicks
                )
            }
        }
    }

    private val AxisAlignedBB.corners get() = mutableListOf<Vec3>().also {
        it.add(Vec3(this.minX, this.minY, this.minZ))
        it.add(Vec3(this.minX, this.minY, this.maxZ))
        it.add(Vec3(this.minX, this.maxY, this.minZ))
        it.add(Vec3(this.minX, this.maxY, this.maxZ))
        it.add(Vec3(this.maxX, this.minY, this.minZ))
        it.add(Vec3(this.maxX, this.minY, this.maxZ))
        it.add(Vec3(this.maxX, this.maxY, this.minZ))
        it.add(Vec3(this.maxX, this.maxY, this.maxZ))
    }

    data class ChestData(val time: Long, var hasClicked: Boolean, var hasBeenVisible: Boolean = false) {
        val timeExisted get() = System.currentTimeMillis() - time

        val shouldDisplay get() = if (config.treasureChestHighlight == 2) true else hasBeenVisible
    }
}