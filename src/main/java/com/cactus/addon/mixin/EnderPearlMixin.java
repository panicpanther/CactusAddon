package com.cactus.addon.mixin;

import com.cactus.addon.modules.EnderPearlVelocity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.math.Vec3d;

@Mixin(EnderPearlEntity.class)
public class EnderPearlMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void modifyVelocity(CallbackInfo ci) {
        if (EnderPearlVelocity.isEnabled()) {
            EnderPearlEntity self = (EnderPearlEntity) (Object) this;
            Vec3d velocity = self.getVelocity();
            double multiplier = EnderPearlVelocity.getMultiplier();
            self.setVelocity(velocity.multiply(multiplier));
        }
    }
}
