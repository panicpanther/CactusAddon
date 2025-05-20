package com.cactus.addon.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.Vec3d;

public class Teleport extends Command {

    public Teleport() {
        super("teleport", "Send a packet with a new player position.", "tp");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("x", DoubleArgumentType.doubleArg()).then(argument("y", DoubleArgumentType.doubleArg()).then(argument("z", DoubleArgumentType.doubleArg()).executes(ctx -> {
            assert mc.player != null;
            mc.player.setPosition(DoubleArgumentType.getDouble(ctx, "x"),DoubleArgumentType.getDouble(ctx, "y"),DoubleArgumentType.getDouble(ctx, "z"));
            return SINGLE_SUCCESS;
        }))));
    }
}