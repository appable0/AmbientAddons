package com.ambientaddons.mixin;

import com.ambientaddons.features.dungeon.IgnoreCarpet;
import net.minecraft.block.BlockCarpet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// credit Floppa Client
@Mixin(BlockCarpet.class)
public abstract class MixinCarpet extends MixinBlock {
    @Inject(method = "setBlockBoundsFromMeta", at = @At("HEAD"), cancellable = true)
    protected void setBlockBoundsFromMeta(int meta, CallbackInfo ci) {
        if (IgnoreCarpet.INSTANCE.shouldIgnoreCarpet()) {
            setBlockBounds(0F, 0F, 0F, 1F, 0F, 1F);
            ci.cancel();
        }
    }
}
