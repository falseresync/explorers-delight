package ru.falseresync.exdel.entity;

import eu.pb4.common.protection.api.CommonProtection;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import ru.falseresync.exdel.ExplorersDelight;

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

        if (outerTarget.getType().isIn(ExplorersDelight.MYSTERY_ARROW_TRANSFORMABLE_ENTITIES)) {
            if (random.nextFloat() < ExplorersDelight.CONFIG.mysteryArrow.transformationChance) {
                actions.add(innerTarget -> {
                    if (getWorld() instanceof ServerWorld serverWorld) {
                        var resultEntitiesRegistryEntries = serverWorld.getServer().getRegistryManager()
                                .getOptional(RegistryKeys.ENTITY_TYPE)
                                .map(registry -> registry.getOrCreateEntryList(ExplorersDelight.MYSTERY_ARROW_RESULT_ENTITIES));
                        if (resultEntitiesRegistryEntries.isEmpty()) {
                            return;
                        }

                        var randomEntityRegistryEntry = resultEntitiesRegistryEntries.get().getRandom(random);
                        if (randomEntityRegistryEntry.isEmpty()) {
                            return;
                        }

                        randomEntityRegistryEntry.get().value().spawn(serverWorld, innerTarget.getBlockPos(), SpawnReason.CONVERSION);
                        innerTarget.discard();
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
        var block = getWorld().getBlockState(pos);
        if (block.isIn(ExplorersDelight.MYSTERY_ARROW_TRANSFORMABLE_BLOCKS)
                && getOwner() instanceof PlayerEntity player
                && getWorld() instanceof ServerWorld world
                && CommonProtection.canPlaceBlock(world, pos, player.getGameProfile(), player)) {
            var resultBlocksRegistryEntries = world.getServer().getRegistryManager()
                    .getOptional(RegistryKeys.BLOCK)
                    .map(registry -> registry.getOrCreateEntryList(ExplorersDelight.MYSTERY_ARROW_RESULT_BLOCKS));
            if (resultBlocksRegistryEntries.isEmpty()) {
                return;
            }

            var randomEntityRegistryEntry = resultBlocksRegistryEntries.get().getRandom(random);
            if (randomEntityRegistryEntry.isEmpty()) {
                return;
            }

            getWorld().setBlockState(pos, randomEntityRegistryEntry.get().value().getDefaultState());
            discard();
        }
    }
}
