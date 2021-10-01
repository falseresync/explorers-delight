package ru.falseresync.exdel.compat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import ru.falseresync.exdel.api.InteractionDriver;

public class DefaultDriver implements InteractionDriver {
    @Override
    public boolean canPlace(ServerWorld world, BlockPos pos, ServerPlayerEntity player) {
        return true;
    }
}
