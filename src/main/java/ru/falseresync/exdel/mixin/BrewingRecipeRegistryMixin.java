package ru.falseresync.exdel.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    @Inject(
            method = "hasRecipe",
            at = @At(
                    value = "RETURN",
                    ordinal = 0
            ),
            cancellable = true
    )
    private static void exdel_hasRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        for (BrewingRecipeRegistry.Recipe<Item> recipe : BrewingRecipeRegistryAccessor.getITEM_RECIPES()) {
            if (input.isOf(recipe.input) && recipe.ingredient.test(ingredient)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
