package ru.falseresync.exdel.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import ru.falseresync.exdel.ExplorersDelight;

public class MysteryArrowEntity extends PersistentProjectileEntity {
    public MysteryArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public MysteryArrowEntity(World world, LivingEntity owner) {
        super(EntityType.SPECTRAL_ARROW, owner, world);
    }

    public MysteryArrowEntity(World world, double x, double y, double z) {
        super(EntityType.SPECTRAL_ARROW, x, y, z, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ExplorersDelight.MYSTERY_ARROW);
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        var pos = blockHitResult.getBlockPos();
        var block = world.getBlockState(pos);
        if (block.isIn(ExplorersDelight.MYSTERY_ARROW_TRANSFORMABLE_BLOCKS)) {
            var newBlock = ExplorersDelight.MYSTERY_ARROW_RESULT_BLOCKS.getRandom(random);
            world.setBlockState(pos, newBlock.getDefaultState());
            discard();
        }
    }
}
