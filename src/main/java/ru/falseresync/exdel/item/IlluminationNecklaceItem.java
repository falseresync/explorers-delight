package ru.falseresync.exdel.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import eu.pb4.common.protection.api.CommonProtection;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.falseresync.exdel.ExplorersDelight;

public class IlluminationNecklaceItem extends TrinketItem {
    public IlluminationNecklaceItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        var stack = super.getDefaultStack();
        var nbt = new NbtCompound();
        nbt.putInt("Cooldown", 0);
        stack.setSubNbt("exdel", nbt);
        return stack;
    }

    @Override
    public boolean canUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return !(EnchantmentHelper.hasBindingCurse(stack) && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(entity));
    }

    @Override
    public void tick(ItemStack necklace, SlotReference slot, LivingEntity entity) {
        super.tick(necklace, slot, entity);

        if (entity instanceof ServerPlayerEntity player) {
            var nbt = necklace.getOrCreateSubNbt("exdel");
            var cooldown = nbt.getInt("cooldown");
            if (cooldown <= 0) {
                cooldown = 5;
                tryToCreateOrb(necklace, player);
            } else {
                cooldown -= 1;
            }
            nbt.putInt("cooldown", cooldown);
            necklace.setSubNbt("exdel", nbt);
        }
    }

    protected void tryToCreateOrb(ItemStack necklace, ServerPlayerEntity player) {
        var inventory = player.getInventory();
        var orbs = ItemStack.EMPTY;
        for (var i = 0; i < inventory.size(); i++) {
            var inventoryStack = inventory.getStack(i);
            if (inventoryStack.isIn(ExplorersDelight.LUMINOUS_ORBS)) {
                orbs = inventoryStack;
                break;
            }
        }

        var world = player.getWorld();
        var pos = player.getBlockPos();
        if (orbs.isEmpty() && player.getAbilities().creativeMode) {
            orbs = ExplorersDelight.LUMINOUS_ORB.asItem().getDefaultStack();
        }

        if (!orbs.isEmpty()
                && orbs.getItem() instanceof BlockItem orbBlockItem
                && world.getLightLevel(pos) < 8
                && player.canPlaceOn(pos, player.getMovementDirection(), orbs)
                && world.getBlockState(pos).isAir()
                && CommonProtection.canPlaceBlock(world, pos, player.getGameProfile(), player)
        ) {
            world.setBlockState(pos, orbBlockItem.getBlock().getDefaultState());
            if (!player.getAbilities().creativeMode) {
                necklace.damage(1, player, ignored -> {});
                orbs.decrement(1);
            }
        }
    }
}
