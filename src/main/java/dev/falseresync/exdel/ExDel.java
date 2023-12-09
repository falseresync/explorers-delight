package dev.falseresync.exdel;

import dev.falseresync.exdel.entity.MysteryArrowBehavior;
import dev.falseresync.exdel.entity.MysteryArrowEntity;
import dev.falseresync.exdel.entity.OwnedDispenser;
import dev.falseresync.exdel.entity.OwnedProjectileDispenserBehavior;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import dev.falseresync.exdel.block.LuminousOrbBlock;
import dev.falseresync.exdel.item.AssortmentPouchItem;
import dev.falseresync.exdel.item.IlluminationNecklaceItem;
import dev.falseresync.exdel.item.MysteryArrowItem;
import dev.falseresync.exdel.item.RecallPotionItem;

import java.util.List;
import java.util.Optional;

public class ExDel implements ModInitializer {
    public static final ExDelConfig CONFIG;
    private static final List<Identifier> NPCS_ALIKE_LOOT_TABLES;
    public static Block LUMINOUS_ORB;
    public static Item LUMINOUS_ORB_ITEM;
    public static Item ILLUMINATION_NECKLACE;
    public static Item RECALL_POTION;
    public static Item ASSORTMENT_POUCH;
    public static Item MYSTERY;
    public static Item MYSTERY_ARROW;
    public static EntityType<MysteryArrowEntity> MYSTERY_ARROW_TYPE;
    public static TagKey<Item> LUMINOUS_ORBS;
    public static TagKey<Block> MYSTERY_ARROW_TRANSFORMABLE_BLOCKS;
    public static TagKey<Block> MYSTERY_ARROW_RESULT_BLOCKS;
    public static TagKey<EntityType<?>> MYSTERY_ARROW_AGEABLE_ENTITIES;
    public static TagKey<EntityType<?>> MYSTERY_ARROW_TRANSFORMABLE_ENTITIES;
    public static TagKey<EntityType<?>> MYSTERY_ARROW_RESULT_ENTITIES;
    public static ScreenHandlerType<AssortmentPouchItem.AssortmentScreenHandler> ASSORTMENT_SCREEN_HANDLER;
    public static final BlockApiLookup<OwnedDispenser, OwnedDispenser.LookupContext> OWNED_DISPENSER;

    static {
        if (!ExDelConfig.HANDLER.load()) {
            ExDelConfig.HANDLER.save();
        }
        CONFIG = ExDelConfig.HANDLER.instance();

        NPCS_ALIKE_LOOT_TABLES = List.of(
                EntityType.VILLAGER.getLootTableId(),
                EntityType.PILLAGER.getLootTableId(),
                EntityType.ZOMBIE_VILLAGER.getLootTableId(),
                EntityType.EVOKER.getLootTableId(),
                EntityType.HOGLIN.getLootTableId(),
                EntityType.ILLUSIONER.getLootTableId(),
                EntityType.PLAYER.getLootTableId(),
                EntityType.VINDICATOR.getLootTableId(),
                EntityType.WANDERING_TRADER.getLootTableId(),
                EntityType.WITCH.getLootTableId()
        );

        OWNED_DISPENSER = BlockApiLookup.get(
                new Identifier("exdel:owned_dispenser"),
                OwnedDispenser.class,
                OwnedDispenser.LookupContext.class
        );

        OWNED_DISPENSER.registerForBlockEntity((dispenser, context) -> {
            if (dispenser.getWorld() instanceof ServerWorld world) {
                var nbt = dispenser.createNbt();
                if (!nbt.containsUuid("exdel:owner")) {
                    return OwnedDispenser.unowned();
                }

                return Optional.ofNullable(world.getServer().getPlayerManager().getPlayer(nbt.getUuid("exdel:owner")))
                        .map(OwnedDispenser::owned)
                        .orElseGet(OwnedDispenser::unowned);
            }

            return OwnedDispenser.unowned();
        }, BlockEntityType.DISPENSER);
    }

    @Override
    public void onInitialize() {
        // Blocks
        LUMINOUS_ORB = Registry.register(
                Registries.BLOCK,
                new Identifier("exdel:luminous_orb"),
                new LuminousOrbBlock(FabricBlockSettings.copyOf(Blocks.FIRE).collidable(false).luminance(15).breakInstantly().hardness(0.25F))
        );

        // BlockItems
        LUMINOUS_ORB_ITEM = Registry.register(
                Registries.ITEM,
                new Identifier("exdel:luminous_orb"),
                new BlockItem(LUMINOUS_ORB, new FabricItemSettings())
        );

        // Items
        ILLUMINATION_NECKLACE = Registry.register(
                Registries.ITEM,
                new Identifier("exdel:illumination_necklace"),
                new IlluminationNecklaceItem(new FabricItemSettings().maxDamage(576))
        );
        RECALL_POTION = Registry.register(
                Registries.ITEM,
                new Identifier("exdel:recall_potion"),
                new RecallPotionItem(new FabricItemSettings())
        );
        ASSORTMENT_POUCH = Registry.register(
                Registries.ITEM,
                new Identifier("exdel:assortment_pouch"),
                new AssortmentPouchItem(new FabricItemSettings().maxCount(1))
        );
        MYSTERY = Registry.register(
                Registries.ITEM,
                new Identifier("exdel:mystery"),
                new Item(new FabricItemSettings())
        );
        MYSTERY_ARROW = Registry.register(
                Registries.ITEM,
                new Identifier("exdel:mystery_arrow"),
                new MysteryArrowItem(new FabricItemSettings())
        );

        // ItemGroups
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(LUMINOUS_ORB_ITEM);
            entries.add(MYSTERY);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(ILLUMINATION_NECKLACE);
            entries.add(ASSORTMENT_POUCH);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.add(RECALL_POTION));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.add(MYSTERY_ARROW));

        // EntityTypes
        MYSTERY_ARROW_TYPE = Registry.register(
                Registries.ENTITY_TYPE,
                new Identifier("exdel:mystery_arrow"),
                FabricEntityTypeBuilder
                        .<MysteryArrowEntity>create(SpawnGroup.MISC, MysteryArrowEntity::new)
                        .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                        .trackRangeBlocks(4)
                        .trackedUpdateRate(20)
                        .build()
        );

        // Item Tags
        LUMINOUS_ORBS = TagKey.of(RegistryKeys.ITEM, new Identifier("exdel:luminous_orbs"));

        // Block Tags
        MYSTERY_ARROW_TRANSFORMABLE_BLOCKS = TagKey.of(RegistryKeys.BLOCK, new Identifier("exdel:mystery_arrow/transformable"));
        MYSTERY_ARROW_RESULT_BLOCKS = TagKey.of(RegistryKeys.BLOCK, new Identifier("exdel:mystery_arrow/results"));

        // Entity Tags
        MYSTERY_ARROW_AGEABLE_ENTITIES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("exdel:mystery_arrow/ageable"));
        MYSTERY_ARROW_TRANSFORMABLE_ENTITIES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("exdel:mystery_arrow/transformable"));
        MYSTERY_ARROW_RESULT_ENTITIES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("exdel:mystery_arrow/results"));

        // Brewing recipes
        // This is not possible because vanilla creates potion items for you:
//        BrewingRecipeRegistry.registerPotionRecipe(Potions.THICK, Items.ENDER_PEARL, RECALL_POTION);
        BrewingRecipeRegistry.ITEM_RECIPES.add(
                new BrewingRecipeRegistry.Recipe<>(Items.POTION, Ingredient.ofItems(Items.ENDER_PEARL), RECALL_POTION));

        // ScreenHandlers
        ASSORTMENT_SCREEN_HANDLER = new ScreenHandlerType<>(AssortmentPouchItem.AssortmentScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
        Registry.register(Registries.SCREEN_HANDLER, new Identifier("exdel:assortment_pouch"), ASSORTMENT_SCREEN_HANDLER);

        // Misc
        MysteryArrowBehavior.registerAll();

        DispenserBlock.registerBehavior(MYSTERY_ARROW, new OwnedProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                var arrowEntity = new MysteryArrowEntity(world, position.getX(), position.getY(), position.getZ());
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return arrowEntity;
            }
        });

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (NPCS_ALIKE_LOOT_TABLES.contains(id)) {
                var poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .bonusRolls(BinomialLootNumberProvider.create(50, 0.1F))
                        .with(ItemEntry.builder(MYSTERY));

                tableBuilder.pool(poolBuilder);
            }
        });
    }
}
