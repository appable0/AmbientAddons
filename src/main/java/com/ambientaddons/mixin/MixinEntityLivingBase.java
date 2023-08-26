package com.ambientaddons.mixin;

import com.ambientaddons.features.misc.PotionHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase {

    @Inject(method = "isPotionActive(Lnet/minecraft/potion/Potion;)Z", at = @At("HEAD"), cancellable = true)
    private void isPotionActive(Potion potion, CallbackInfoReturnable<Boolean> cir) {
        if (PotionHook.INSTANCE.shouldIgnorePotion(potion)) {
            cir.setReturnValue(false);
        }
    }
}
