package dev.falseresync.exdel.mixin;

import dev.falseresync.exdel.api.Ownable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(BlockEntity.class)
public final class BlockEntityMixin implements Ownable {
    @Unique
    private UUID ownerUuid = null;

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void readOwnerFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.containsUuid("exdel:ownable_owner")) {
            ownerUuid = nbt.getUuid("exdel:ownable_owner");
        }
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void writeOwnerToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (ownerUuid != null) {
            nbt.putUuid("exdel:ownable_owner", ownerUuid);
        }
    }

    @Override
    public UUID exdel$getOwnerUuid() {
        return ownerUuid;
    }

    @Override
    public void exdel$setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }
}
