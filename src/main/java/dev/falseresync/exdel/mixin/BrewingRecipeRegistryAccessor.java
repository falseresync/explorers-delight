package dev.falseresync.exdel.mixin;

import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(BrewingRecipeRegistry.class)
public interface BrewingRecipeRegistryAccessor {
//    @Accessor
//    static List<BrewingRecipeRegistry.Recipe<Item>> getITEM_RECIPES() {
//        throw new AssertionError();
//    }
}
