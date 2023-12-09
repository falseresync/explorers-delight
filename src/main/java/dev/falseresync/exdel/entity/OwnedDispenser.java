package dev.falseresync.exdel.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;

public interface OwnedDispenser {
    LookupContext NO_CONTEXT = new LookupContext();

    static OwnedDispenser unowned() {
        return new OwnedDispenser() {
        };
    }

    static OwnedDispenser owned(PlayerEntity player) {
        return new OwnedDispenser() {
            @Override
            public void ifOwnedSetForProjectile(ProjectileEntity projectileEntity) {
                projectileEntity.setOwner(player);
            }
        };
    }

    default void ifOwnedSetForProjectile(ProjectileEntity projectileEntity) {
    }

    final class LookupContext {
    }
}
