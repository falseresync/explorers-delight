package ru.falseresync.exdel.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import ru.falseresync.exdel.ExplorersDelight;
import ru.falseresync.exdel.TagUtil;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class MysteryArrowBehavior {
    private static final Random OFFSETS_RANDOM = Random.createLocal();
    private static final NavigableMap<Double, EntityHitBehavior> ACTIONS_ON_ENTITY_HIT = new TreeMap<>();
    private static double TOTAL_WEIGHT = 0;

    public static void registerAll() {
        registerEntityHitBehavior(
                ExplorersDelight.CONFIG.mysteryArrow.agingWeight,
                (arrow, world, target, random) -> {
                    if (target.getType().isIn(ExplorersDelight.MYSTERY_ARROW_AGEABLE_ENTITIES)
                            && target instanceof MobEntity mob) {
                        mob.setBaby(!mob.isBaby());
                        return ActionResult.SUCCESS;
                    }
                    return ActionResult.FAILURE;
                }
        );
        registerEntityHitBehavior(
                ExplorersDelight.CONFIG.mysteryArrow.transformationWeight,
                (arrow, world, target, random) -> {
                    if (!target.getType().isIn(ExplorersDelight.MYSTERY_ARROW_TRANSFORMABLE_ENTITIES)) {
                        return ActionResult.FAILURE;
                    }
                    var entry = TagUtil.nextRandomEntry(world, ExplorersDelight.MYSTERY_ARROW_RESULT_ENTITIES, random);
                    if (entry.isEmpty()) {
                        return ActionResult.FAILURE;
                    }
                    entry.get().spawn(world, target.getBlockPos(), SpawnReason.CONVERSION);
                    target.discard();
                    return ActionResult.SUCCESS;
                }
        );
        registerEntityHitBehavior(
                ExplorersDelight.CONFIG.mysteryArrow.doNothingWeight,
                (arrow, world, target, random) -> ActionResult.PASS
        );
    }

    public static void registerEntityHitBehavior(int weight, EntityHitBehavior action) {
        if (weight <= 0) {
            return;
        }
        // This is to ensure there are no lost actions because of duplicate keys
        var offset = OFFSETS_RANDOM.nextDouble() / 10000;
        ACTIONS_ON_ENTITY_HIT.put(weight + offset, action);
        TOTAL_WEIGHT += weight + offset;
    }

    public static Collection<EntityHitBehavior> viewWeightedRandomEntityHitBehaviors(Random random) {
        double value = random.nextDouble() * TOTAL_WEIGHT;
        return ACTIONS_ON_ENTITY_HIT.tailMap(value, false).values();
    }

    @FunctionalInterface
    public interface EntityHitBehavior {
        ActionResult onHit(MysteryArrowEntity arrow, ServerWorld world, LivingEntity target, Random random);
    }

    public enum ActionResult {
        SUCCESS,
        PASS,
        FAILURE
    }
}
