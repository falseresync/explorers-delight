package ru.falseresync.exdel.compat;

import io.github.flemmli97.flan.api.ClaimHandler;
import io.github.flemmli97.flan.api.permission.PermissionRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class FlanCompat {
    public static final boolean isFlanLoaded;

    static {
        isFlanLoaded = FabricLoader.getInstance().isModLoaded("flan");
    }

    public static void init() {
    }

    public static boolean canPlaceBlock(ServerWorld world, BlockPos pos, ServerPlayerEntity player) {
        if (isFlanLoaded) {
            return ClaimHandler.getPermissionStorage(world).getForPermissionCheck(pos).canInteract(player, PermissionRegistry.PLACE, pos);
        }

        return true;
    }
}
