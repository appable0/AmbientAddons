package com.ambientaddons.mixin;

import com.ambientaddons.events.GuiContainerEvent;
import com.ambientaddons.events.HitBlockEvent;
import com.ambientaddons.features.misc.TreasureChestESP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {

    @Inject(method = "clickBlock", at = @At("HEAD"), cancellable = true)
    private void onClickBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftForge.EVENT_BUS.post(new HitBlockEvent(loc, face))) {
            cir.setReturnValue(false);
        }
    }
}
