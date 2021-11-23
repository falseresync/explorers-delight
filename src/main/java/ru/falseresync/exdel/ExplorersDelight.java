package ru.falseresync.exdel;

import draylar.omegaconfig.OmegaConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.Material;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderTypes;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import ru.falseresync.exdel.api.CompatManager;
import ru.falseresync.exdel.block.LuminousOrbBlock;
import ru.falseresync.exdel.entity.MysteryArrowEntity;
import ru.falseresync.exdel.item.AssortmentPouchItem;
import ru.falseresync.exdel.item.IlluminationNecklaceItem;
import ru.falseresync.exdel.item.MysteryArrowItem;
import ru.falseresync.exdel.item.RecallPotionItem;
import ru.falseresync.exdel.mixin.BrewingRecipeRegistryAccessor;

import java.util.List;

public class ExplorersDelight implements ModInitializer {
    public static final ExplorersDelightConfig CONFIG;
    private static final List<Identifier> NPCS_ALIKE_LOOT_TABLES;
    public static Block LUMINOUS_ORB;
    public static Item ILLUMINATION_NECKLACE;
    public static Item RECALL_POTION;
    public static Item ASSORTMENT_POUCH;
    public static Item MYSTERY;
    public static Item MYSTERY_ARROW;
    public static EntityType<MysteryArrowEntity> MYSTERY_ARROW_TYPE;
    public static Tag<Item> LUMINOUS_ORBS;
    public static Tag<Block> MYSTERY_ARROW_TRANSFORMABLE_BLOCKS;
    public static Tag<Block> MYSTERY_ARROW_RESULT_BLOCKS;
    public static Tag<EntityType<?>> MYSTERY_ARROW_AGEABLE_ENTITIES;
    public static ScreenHandlerType<AssortmentPouchItem.AssortmentScreenHandler> ASSORTMENT_SCREEN_HANDLER;

    static {
        CONFIG = OmegaConfig.register(ExplorersDelightConfig.class);
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
    }

    @Override
    public void onInitialize() {
        // Mod systems
        CompatManager.init();

        // Blocks
        LUMINOUS_ORB = Registry.register(
                Registry.BLOCK,
                new Identifier("exdel:luminous_orb"),
                new LuminousOrbBlock(FabricBlockSettings.of(Material.FIRE).collidable(false).luminance(15).breakByHand(true).hardness(0.25F)));

        // BlockItems
        Registry.register(
                Registry.ITEM,
                new Identifier("exdel:luminous_orb"),
                new BlockItem(LUMINOUS_ORB, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

        // Items
        ILLUMINATION_NECKLACE = Registry.register(
                Registry.ITEM,
                new Identifier("exdel:illumination_necklace"),
                new IlluminationNecklaceItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(576)));
        RECALL_POTION = Registry.register(
                Registry.ITEM,
                new Identifier("exdel:recall_potion"),
                new RecallPotionItem(new FabricItemSettings().group(ItemGroup.BREWING)));
        ASSORTMENT_POUCH = Registry.register(
                Registry.ITEM,
                new Identifier("exdel:assortment_pouch"),
                new AssortmentPouchItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));
        MYSTERY = Registry.register(
                Registry.ITEM,
                new Identifier("exdel:mystery"),
                new Item(new FabricItemSettings().group(ItemGroup.MATERIALS)));
        MYSTERY_ARROW = Registry.register(
                Registry.ITEM,
                new Identifier("exdel:mystery_arrow"),
                new MysteryArrowItem(new FabricItemSettings().group(ItemGroup.COMBAT)));

        // EntityTypes
        MYSTERY_ARROW_TYPE = Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier("exdel:mystery_arrow"),
                FabricEntityTypeBuilder
                        .<MysteryArrowEntity>create(SpawnGroup.MISC, MysteryArrowEntity::new)
                        .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                        .trackRangeBlocks(4)
                        .trackedUpdateRate(20)
                        .build());

        // Item Tags
        LUMINOUS_ORBS = TagFactory.ITEM.create(new Identifier("exdel:luminous_orbs"));

        // Block Tags
        MYSTERY_ARROW_TRANSFORMABLE_BLOCKS = TagFactory.BLOCK.create(new Identifier("exdel:mystery_arrow/transformable"));
        MYSTERY_ARROW_RESULT_BLOCKS = TagFactory.BLOCK.create(new Identifier("exdel:mystery_arrow/results"));

        // Entity Tags
        MYSTERY_ARROW_AGEABLE_ENTITIES = TagFactory.ENTITY_TYPE.create(new Identifier("exdel:mystery_arrow/ageable"));

        // Brewing recipes
        BrewingRecipeRegistryAccessor.getITEM_RECIPES().add(
                new BrewingRecipeRegistry.Recipe<>(Items.POTION, Ingredient.ofItems(Items.ENDER_PEARL), RECALL_POTION));
        BrewingRecipeRegistryAccessor.getITEM_RECIPES().add(
                new BrewingRecipeRegistry.Recipe<>(Items.ARROW, Ingredient.ofItems(MYSTERY), MYSTERY_ARROW));

        // ScreenHandlers
        ASSORTMENT_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("exdel:assortment_pouch"), AssortmentPouchItem.AssortmentScreenHandler::new);

        // Misc
        DispenserBlock.registerBehavior(MYSTERY_ARROW, new ProjectileDispenserBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                var arrowEntity = new MysteryArrowEntity(world, position.getX(), position.getY(), position.getZ());
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return arrowEntity;
            }
        });

        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, table, setter) -> {
            if (NPCS_ALIKE_LOOT_TABLES.contains(id)) {
                var poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .bonusRolls(BinomialLootNumberProvider.create(50, 0.1F))
                        .with(ItemEntry.builder(MYSTERY));

                table.pool(poolBuilder);
            }
        });
    }
}
