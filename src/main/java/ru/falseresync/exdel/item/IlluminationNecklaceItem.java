package ru.falseresync.exdel.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.falseresync.exdel.ExplorersDelight;
import ru.falseresync.exdel.api.CompatManager;

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
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        if (entity instanceof ServerPlayerEntity player) {
            var nbt = stack.getOrCreateSubNbt("exdel");
            var cooldown = nbt.getInt("Cooldown");
            if (cooldown <= 0) {
                cooldown = 5;
                var inventory = player.getInventory();
                var orbs = ItemStack.EMPTY;
                for (var i = 0; i < inventory.size(); i++) {
                    var inventoryStack = inventory.getStack(i);
                    if (inventoryStack.isIn(ExplorersDelight.LUMINOUS_ORBS)) {
                        orbs = inventoryStack;
                        break;
                    }
                }

                if (orbs.isEmpty() && player.getAbilities().creativeMode) {
                    orbs = ExplorersDelight.LUMINOUS_ORB.asItem().getDefaultStack();
                }

                if (!orbs.isEmpty() && orbs.getItem() instanceof BlockItem orbBlockItem) {
                    var world = player.getServerWorld();
                    var pos = player.getBlockPos();
                    if (world.getLightLevel(pos) < 8 && player.canPlaceOn(pos, player.getMovementDirection(), orbs)
                            && CompatManager.getInteractionPipeline().stream().allMatch(driver -> driver.canPlace(world, pos, player))) {
                        world.setBlockState(pos, orbBlockItem.getBlock().getDefaultState());
                        if (!player.getAbilities().creativeMode) {
                            stack.damage(1, player, ignored -> {});
                            orbs.decrement(1);
                        }
                    }
                }
            } else {
                cooldown -= 1;
            }
            nbt.putInt("Cooldown", cooldown);
            stack.setSubNbt("exdel", nbt);
        }
    }
}
