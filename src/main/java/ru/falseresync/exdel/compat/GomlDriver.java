package ru.falseresync.exdel.compat;

import draylar.goml.api.ClaimUtils;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import ru.falseresync.exdel.api.InteractionDriver;

import java.util.stream.Collectors;

public class GomlDriver implements InteractionDriver {
    @Override
    public boolean canPlace(ServerWorld world, BlockPos pos, ServerPlayerEntity player) {
        // I have to collect this into a list, because RTree's Selection is :concern: and doesn't consistently result in allMatch -> true for empty selections
        return ClaimUtils.getClaimsAt(world, pos).collect(Collectors.toList()).stream().allMatch(entry -> entry.getValue().hasPermission(player));
    }
}
