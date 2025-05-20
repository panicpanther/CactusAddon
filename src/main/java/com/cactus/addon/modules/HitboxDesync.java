package com.cactus.addon.modules;

import com.cactus.addon.AddonCactus;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;

import static java.lang.Math.abs;

public class HitboxDesync extends Module {

    public HitboxDesync() {
        super(AddonCactus.CATEGORY, "Hitbox Desync", "test thing idk");
    }

    @EventHandler
    private void onTick(TickEvent event) {
        assert mc.world == null; assert mc.player != null;

        double numOffset = .200009968835369999878673424677777777777761;
        Direction facing = mc.player.getHorizontalFacing();
        Box boundingBox = mc.player.getBoundingBox();

        Vec3d center = boundingBox.getCenter();
        Vec3d offset = new Vec3d(facing.getUnitVector());

        Vec3d doMath = new Vec3d(Vec3d.of(BlockPos.ofFloored(center)).add(.5, 0, .5).add(offset.multiply(numOffset)).x * abs(facing.getUnitVector().x()), Vec3d.of(BlockPos.ofFloored(center)).add(.5, 0, .5).add(offset.multiply(numOffset)).y * abs(facing.getUnitVector().y()), Vec3d.of(BlockPos.ofFloored(center)).add(.5, 0, .5).add(offset.multiply(numOffset)).z * abs(facing.getUnitVector().z()));
        mc.player.setPosition(doMath.x == 0 ? mc.player.getX() : doMath.x,
                mc.player.getY(),
                doMath.z == 0 ? mc.player.getZ() : doMath.z);
        toggle();
    }

}