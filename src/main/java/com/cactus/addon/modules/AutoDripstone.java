package com.cactus.addon.modules;

import com.cactus.addon.AddonCactus;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AutoDripstone extends Module {

    public AutoDripstone() {
        super(AddonCactus.CATEGORY, "Auto Dripstone", "Drops dripstone on players below.");
    }

    private final Map<BlockPos, Integer> trapdoorTimers = new HashMap<>();

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        BlockPos playerPos = mc.player.getBlockPos();
        ItemStack mainHand = mc.player.getMainHandStack();

        for (BlockPos pos : BlockPos.iterateOutwards(playerPos, 4, 4, 4)) {
            BlockState state = mc.world.getBlockState(pos);
            if (!(state.getBlock() instanceof TrapdoorBlock)) continue;

            BlockPos below = pos.down();
            if (!mc.world.getBlockState(below).isAir()) continue;
            if (!mainHand.isOf(Items.POINTED_DRIPSTONE)) continue;

            BlockHitResult placeHit = new BlockHitResult(
                    Vec3d.ofCenter(below),
                    Direction.UP,
                    below,
                    false
            );

            assert mc.interactionManager != null;
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, placeHit);

            if (!state.get(TrapdoorBlock.OPEN)) {
                BlockHitResult trapdoorHit = new BlockHitResult(
                        Vec3d.ofCenter(pos),
                        Direction.UP,
                        pos,
                        false
                );
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, trapdoorHit);
                trapdoorTimers.put(pos.toImmutable(), 0);
            }
            break;
        }

        Iterator<Map.Entry<BlockPos, Integer>> it = trapdoorTimers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BlockPos, Integer> entry = it.next();
            BlockPos pos = entry.getKey();
            int timer = entry.getValue();

            if (timer >= 10) {
                BlockState state = mc.world.getBlockState(pos);
                if (state.getBlock() instanceof TrapdoorBlock && state.get(TrapdoorBlock.OPEN)) {
                    BlockHitResult hit = new BlockHitResult(
                            Vec3d.ofCenter(pos),
                            Direction.UP,
                            pos,
                            false
                    );
                    assert mc.interactionManager != null;
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                }
                it.remove();
            } else {
                entry.setValue(timer + 1);
            }
        }
    }

}
