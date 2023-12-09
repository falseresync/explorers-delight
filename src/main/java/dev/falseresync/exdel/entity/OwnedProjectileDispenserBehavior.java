package dev.falseresync.exdel.entity;

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
        Optional.ofNullable(ExDel.OWNED_DISPENSER
                .find(world, pointer.pos(), pointer.state(), pointer.blockEntity(), OwnedDispenser.NO_CONTEXT)
        ).ifPresent(dispenser -> dispenser.ifOwnedSetForProjectile(projectileEntity));

        world.spawnEntity(projectileEntity);
        stack.decrement(1);
        return stack;
    }
}
