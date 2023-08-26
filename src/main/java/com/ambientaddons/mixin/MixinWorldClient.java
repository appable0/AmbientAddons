package com.ambientaddons.mixin;
import com.ambientaddons.events.BlockChangeEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient {

    @Inject(method = "invalidateRegionAndSetBlock", at = @At("HEAD"), cancellable = true)
    private void onInvalidateRegionAndSetBlock(BlockPos pos, IBlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftForge.EVENT_BUS.post(new BlockChangeEvent(pos, state))) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

}