package ru.falseresync.exdel.api;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface InteractionDriver {
    boolean canPlace(ServerWorld world, BlockPos pos, ServerPlayerEntity player);
}
