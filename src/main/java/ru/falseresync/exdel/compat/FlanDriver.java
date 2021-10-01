package ru.falseresync.exdel.compat;

import io.github.flemmli97.flan.api.ClaimHandler;
import io.github.flemmli97.flan.api.permission.PermissionRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import ru.falseresync.exdel.api.InteractionDriver;

public class FlanDriver implements InteractionDriver {
    @Override
    public boolean canPlace(ServerWorld world, BlockPos pos, ServerPlayerEntity player) {
        return ClaimHandler.getPermissionStorage(world).getForPermissionCheck(pos).canInteract(player, PermissionRegistry.PLACE, pos);
    }
}
