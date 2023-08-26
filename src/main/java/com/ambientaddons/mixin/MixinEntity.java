package com.ambientaddons.mixin;

import com.ambientaddons.features.keybinds.SensitivityHook;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Inject(method = "setAngles", at = @At("HEAD"), cancellable = true)
    public void onSetAngles(float yaw, float pitch, CallbackInfo ci) {
        Entity theEntity = (Entity) (Object) this;
        if (yaw != 0F && pitch != 0F && theEntity == Minecraft.getMinecraft().thePlayer) {
            if (SensitivityHook.INSTANCE.shouldBlockRotate()) {
                ci.cancel();
            }
        }
    }
}