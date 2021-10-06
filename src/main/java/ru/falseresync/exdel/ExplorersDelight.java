package ru.falseresync.exdel;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.*;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.falseresync.exdel.api.CompatManager;
import ru.falseresync.exdel.block.LuminousOrbBlock;
import ru.falseresync.exdel.item.AssortmentPouchItem;
import ru.falseresync.exdel.item.IlluminationNecklaceItem;
import ru.falseresync.exdel.item.RecallPotionItem;
import ru.falseresync.exdel.mixin.BrewingRecipeRegistryAccessor;

public class ExplorersDelight implements ModInitializer {
    public static Block LUMINOUS_ORB;
    public static Item ILLUMINATION_NECKLACE;
    public static Item RECALL_POTION;
    public static Item ASSORTMENT_POUCH;
    public static Tag<Item> LUMINOUS_ORBS;
    public static ScreenHandlerType<AssortmentPouchItem.AssortmentScreenHandler> ASSORTMENT_SCREEN_HANDLER;

    @Override
    public void onInitialize() {
        CompatManager.init();

        LUMINOUS_ORB = Registry.register(Registry.BLOCK, new Identifier("exdel:luminous_orb"), new LuminousOrbBlock(FabricBlockSettings.of(Material.FIRE).collidable(false).luminance(15).breakByHand(true).hardness(0.25F)));

        Registry.register(Registry.ITEM, new Identifier("exdel:luminous_orb"), new BlockItem(LUMINOUS_ORB, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        ILLUMINATION_NECKLACE = Registry.register(Registry.ITEM, new Identifier("exdel:illumination_necklace"),
                new IlluminationNecklaceItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxDamage(576)));
        RECALL_POTION = Registry.register(Registry.ITEM, new Identifier("exdel:recall_potion"), new RecallPotionItem(new FabricItemSettings().group(ItemGroup.BREWING)));
        ASSORTMENT_POUCH = Registry.register(Registry.ITEM, new Identifier("exdel:assortment_pouch"), new AssortmentPouchItem(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1)));

        LUMINOUS_ORBS = TagFactory.ITEM.create(new Identifier("exdel:luminous_orbs"));

        BrewingRecipeRegistryAccessor.getITEM_RECIPES().add(
                new BrewingRecipeRegistry.Recipe<>(Items.POTION, Ingredient.ofItems(Items.ENDER_PEARL), RECALL_POTION));

        ASSORTMENT_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier("exdel:assortment_pouch"), AssortmentPouchItem.AssortmentScreenHandler::new);
    }
}
