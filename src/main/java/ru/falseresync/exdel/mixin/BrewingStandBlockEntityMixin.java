package ru.falseresync.exdel.mixin;

import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin {
    @Inject(
            method = "isValid",
            at = @At("TAIL"),
            cancellable = true
    )
    public void exdel_isValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            for (BrewingRecipeRegistry.Recipe<Item> recipe : BrewingRecipeRegistryAccessor.getITEM_RECIPES()) {
                if (stack.isOf(recipe.input)) {
                    cir.setReturnValue(((BrewingStandBlockEntity) (Object) this).getStack(slot).isEmpty());
                    return;
                }
            }
        }
    }
}
