package com.ambientaddons.mixin;

import com.ambientaddons.events.GuiContainerEvent;
import com.ambientaddons.events.ItemOverlayEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem {
    @Inject(method = "renderItemOverlayIntoGUI", at = @At("HEAD"),cancellable = true)
    public void renderItemOverlayIntoGui(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new ItemOverlayEvent(stack, xPosition, yPosition))) {
            ci.cancel();
        }
    }
}
