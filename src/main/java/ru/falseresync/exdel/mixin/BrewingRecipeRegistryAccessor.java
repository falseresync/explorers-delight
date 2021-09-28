package ru.falseresync.exdel.mixin;

import net.minecraft.item.Item;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;


@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryAccessor {
    @Accessor
    static List<BrewingRecipeRegistry.Recipe<Item>> getITEM_RECIPES() {
        throw new AssertionError();
    }
}
