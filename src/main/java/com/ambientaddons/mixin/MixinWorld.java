package com.ambientaddons.mixin;

import com.ambientaddons.events.BlockActionEvent;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class MixinWorld {
    @Inject(method = "addBlockEvent", at = @At("HEAD"))
    private void OnAddBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new BlockActionEvent(pos, eventID, eventParam));
    }
}
