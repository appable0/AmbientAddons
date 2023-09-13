package com.ambientaddons.mixin;

import com.ambientaddons.features.dungeon.Clicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getSwingProgress(F)F"))
    private float getItemInUseCountForFirstPerson(AbstractClientPlayer instance, float partialTicks) {
        if (Clicker.INSTANCE.blockSwing()) {
            return 0;
        } else {
            return Minecraft.getMinecraft().thePlayer.getSwingProgress(partialTicks);
        }
    }
}
