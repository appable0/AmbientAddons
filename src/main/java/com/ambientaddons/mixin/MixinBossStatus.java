package com.ambientaddons.mixin;

import com.ambientaddons.events.BossStatusEvent;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossStatus.class)
public abstract class MixinBossStatus {

    @Inject(method = "setBossStatus", at = @At("HEAD"), cancellable = true)
    private static void onSetBossStatus(IBossDisplayData displayData, boolean hasColorModifierIn, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new BossStatusEvent(displayData, hasColorModifierIn))) {
            ci.cancel();
        }
    }
}
