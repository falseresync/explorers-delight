package ru.falseresync.exdel;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import ru.falseresync.exdel.item.RecallPotionItem;
import ru.falseresync.exdel.mixin.BrewingRecipeRegistryAccessor;

public class ExplorersDelight implements ModInitializer {
    public static Item RECALL_POTION;
    @Override
    public void onInitialize() {
        RECALL_POTION = Registry.register(Registry.ITEM, new Identifier("exdel:recall_potion"), new RecallPotionItem(new FabricItemSettings().group(ItemGroup.BREWING)));
        BrewingRecipeRegistryAccessor.getITEM_RECIPES().add(
                new BrewingRecipeRegistry.Recipe<>(Items.POTION, Ingredient.ofItems(Items.ENDER_PEARL), RECALL_POTION));
    }
}
