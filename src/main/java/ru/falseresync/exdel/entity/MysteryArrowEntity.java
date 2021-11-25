package ru.falseresync.exdel.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import ru.falseresync.exdel.ExplorersDelight;
import ru.falseresync.exdel.api.CompatManager;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MysteryArrowEntity extends PersistentProjectileEntity {
    public MysteryArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public MysteryArrowEntity(World world, LivingEntity owner) {
        super(ExplorersDelight.MYSTERY_ARROW_TYPE, owner, world);
    }

    public MysteryArrowEntity(World world, double x, double y, double z) {
        super(ExplorersDelight.MYSTERY_ARROW_TYPE, x, y, z, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ExplorersDelight.MYSTERY_ARROW);
    }

    @Override
    protected void onHit(LivingEntity outerTarget) {
        super.onHit(outerTarget);

        var actions = new ArrayList<Consumer<LivingEntity>>();

        if (outerTarget.getType().isIn(ExplorersDelight.MYSTERY_ARROW_AGEABLE_ENTITIES)) {
            if (random.nextFloat() < ExplorersDelight.CONFIG.mysteryArrow.agingChance) {
                actions.add(innerTarget -> {
                    if (innerTarget instanceof PassiveEntity ageable) {
                        ageable.setBaby(!ageable.isBaby());
                    } else if (innerTarget instanceof ZombieEntity ageable) {
                        ageable.setBaby(!ageable.isBaby());
                    }
                });
            }
        }

        actions.forEach(action -> action.accept(outerTarget));
        if (!actions.isEmpty()) {
            discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        var pos = blockHitResult.getBlockPos();
        var block = world.getBlockState(pos);
        if (block.isIn(ExplorersDelight.MYSTERY_ARROW_TRANSFORMABLE_BLOCKS)
                && getOwner() instanceof ServerPlayerEntity player
                && !world.isClient
                && CompatManager.getInteractionPipeline().stream().allMatch(driver -> driver.canPlace((ServerWorld) world, pos, player))) {
            var newBlock = ExplorersDelight.MYSTERY_ARROW_RESULT_BLOCKS.getRandom(random);
            world.setBlockState(pos, newBlock.getDefaultState());
            discard();
        }
    }
}
