package dev.falseresync.exdel.entity;

import dev.falseresync.exdel.ExDel;
import dev.falseresync.exdel.TagUtil;
import eu.pb4.common.protection.api.CommonProtection;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class MysteryArrowEntity extends PersistentProjectileEntity {
    public MysteryArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        init();
    }

    public MysteryArrowEntity(World world, double x, double y, double z) {
        super(ExDel.MYSTERY_ARROW_TYPE, x, y, z, world);
        init();
    }

    public MysteryArrowEntity(World world, LivingEntity owner) {
        super(ExDel.MYSTERY_ARROW_TYPE, owner, world);
        init();
    }

    protected void init() {
        setDamage(0);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ExDel.MYSTERY_ARROW);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        var hitEntity = entityHitResult.getEntity();
        var shouldPropagateToSuper = false;

        if (hitEntity instanceof LivingEntity target
                && getWorld() instanceof ServerWorld world
                && (
                !CommonProtection.isProtected(world, target.getBlockPos())
                        || (
                        getOwner() instanceof PlayerEntity player
                                && CommonProtection.canDamageEntity(world, target, player.getGameProfile(), player)
                )
        )
        ) {
            iterate:
            for (var behavior : MysteryArrowBehavior.viewWeightedRandomEntityHitBehaviors(random)) {
                var result = behavior.onHit(this, world, target, random);
                switch (result) {
                    case SUCCESS -> {
                        shouldPropagateToSuper = false;
                        discard();
                        break iterate;
                    }
                    case PASS -> {
                        shouldPropagateToSuper = true;
                        break iterate;
                    }
                    case FAILURE -> shouldPropagateToSuper = true;
                }
            }
        }

        if (shouldPropagateToSuper) {
            super.onEntityHit(entityHitResult);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        var pos = blockHitResult.getBlockPos();
        var block = getWorld().getBlockState(pos);
        if (block.isIn(ExDel.MYSTERY_ARROW_TRANSFORMABLE_BLOCKS)
                && getWorld() instanceof ServerWorld world
                && (
                !CommonProtection.isProtected(world, pos)
                        || (
                        getOwner() instanceof PlayerEntity player
                                && CommonProtection.canPlaceBlock(world, pos, player.getGameProfile(), player)
                )
        )
        ) {
            TagUtil.nextRandomEntry(world, ExDel.MYSTERY_ARROW_RESULT_BLOCKS, random)
                    .ifPresent(randomTagEntry -> {
                        getWorld().setBlockState(pos, randomTagEntry.getDefaultState());
                        discard();
                    });
        }
    }
}
