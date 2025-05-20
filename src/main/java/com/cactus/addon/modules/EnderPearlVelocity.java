package com.cactus.addon.modules;

import com.cactus.addon.AddonCactus;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

public class EnderPearlVelocity extends Module {
    private static EnderPearlVelocity INSTANCE;
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public EnderPearlVelocity() {
        super(AddonCactus.CATEGORY, "EnderPearlVelocity", "Boosts the velocity of thrown ender pearls.");
        INSTANCE = this;
    }

    public final Setting<Double> multiplier = sgGeneral.add(new DoubleSetting.Builder()
            .name("Multiplier")
            .description("The factor by which to multiply the velocity boost.")
            .defaultValue(1.5)
            .min(0.1)
            .sliderRange(0.1, 5.0)
            .build()
    );

    public final Setting<EnderPearlVelocity.Modes> mode = sgGeneral.add(new EnumSetting.Builder<EnderPearlVelocity.Modes>()
            .name("Enabled Hands")
            .description("Enable what hands are affected.")
            .defaultValue(Modes.PlayerVelocity)
            .build()
    );

    public static boolean isEnabled() { return INSTANCE != null && INSTANCE.isActive() && INSTANCE.mode.get() == Modes.Mixin; }
    public static double getMultiplier() { return INSTANCE != null ? INSTANCE.multiplier.get() : 1.0; }

    @EventHandler
    private void onThrowPearl(PacketEvent.Send event) {
        if (event.packet instanceof PlayerInteractItemC2SPacket packet) {
            if (mc.player == null) return;
            if (packet.getHand() == Hand.MAIN_HAND || packet.getHand() == Hand.OFF_HAND) {
                if (mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL || mc.player.getOffHandStack().getItem() == Items.ENDER_PEARL) {
                    Vec3d direction = mc.player.getRotationVec(1.0f).normalize();
                    double boost = multiplier.get();
                    Vec3d velocityBoost = new Vec3d(direction.x * boost, direction.y * boost + 0.2, direction.z * boost);
                    mc.player.setVelocity(mc.player.getVelocity().add(velocityBoost));
                }
            }
        }
    }

    public enum Modes {
        PlayerVelocity,
        Mixin,
    }
}
