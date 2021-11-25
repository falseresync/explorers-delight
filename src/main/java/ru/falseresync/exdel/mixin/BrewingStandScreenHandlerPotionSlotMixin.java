package ru.falseresync.exdel.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandScreenHandler.PotionSlot.class)
public class BrewingStandScreenHandlerPotionSlotMixin {
    @Inject(
            method = "matches",
            at = @At("TAIL"),
            cancellable = true
    )
    private static void exdel_matches(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            for (BrewingRecipeRegistry.Recipe<Item> recipe : BrewingRecipeRegistryAccessor.getITEM_RECIPES()) {
                if (stack.isOf(recipe.input)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}
