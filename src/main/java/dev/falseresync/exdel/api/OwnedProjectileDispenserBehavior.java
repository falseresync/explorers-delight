package dev.falseresync.exdel.api;

import dev.falseresync.exdel.ExDel;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;

import java.util.Optional;

public abstract class OwnedProjectileDispenserBehavior extends ProjectileDispenserBehavior {
    @Override
    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        var world = pointer.world();
        var position = DispenserBlock.getOutputLocation(pointer);
        var direction = pointer.state().get(DispenserBlock.FACING);

        var projectileEntity = createProjectile(world, position, stack);
        projectileEntity.setVelocity(
                direction.getOffsetX(), direction.getOffsetY() + 0.1F, direction.getOffsetZ(), getForce(), getVariation()
        );
        Optional.ofNullable(ExDel.OWNABLE.find(world, pointer.pos(), pointer.state(), pointer.blockEntity(), null))
                .ifPresent(ownable -> {
                    var ownerUuid = ownable.exdel$getOwnerUuid();
                    if (ownerUuid != null) {
                        var entity = world.getEntity(ownerUuid);
                        if (entity != null) {
                            projectileEntity.setOwner(entity);
                        }
                    }
                });

        world.spawnEntity(projectileEntity);
        stack.decrement(1);
        return stack;
    }
}
