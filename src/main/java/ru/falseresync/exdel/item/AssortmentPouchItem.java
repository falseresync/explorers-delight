package ru.falseresync.exdel.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import ru.falseresync.exdel.ExplorersDelight;

import java.util.Set;

public class AssortmentPouchItem extends Item {
    public static final int INV_ROWS = 5;
    public static final int INV_SIZE = 9*INV_ROWS;

    public AssortmentPouchItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        if (!world.isClient) {
            user.openHandledScreen(new AssortmentScreenHandlerFactory(getName(stack), new AssortmentInventory(INV_SIZE, stack)));
        }
        return TypedActionResult.success(stack);
    }

    protected record AssortmentScreenHandlerFactory(Text displayName, Inventory inventory) implements NamedScreenHandlerFactory {
        @Override
        public Text getDisplayName() {
            return displayName;
        }

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
            return new AssortmentScreenHandler(syncId, playerInventory, inventory, INV_ROWS);
        }
    }

    public static class AssortmentScreenHandler extends ScreenHandler {
        public AssortmentScreenHandler(int syncId, PlayerInventory playerInventory) {
            this(syncId, playerInventory, new AssortmentInventory(INV_SIZE, ItemStack.EMPTY.copy()), INV_ROWS);
        }

        public AssortmentScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, int rows) {
            super(ExplorersDelight.ASSORTMENT_SCREEN_HANDLER, syncId);
            checkSize(inventory, rows * 9);
//            this.inventory = inventory;
//            this.rows = rows;
//            inventory.onOpen(playerInventory.player);
//            int i = (this.rows - 4) * 18;
//
//            int n;
//            int m;
//            for(n = 0; n < this.rows; ++n) {
//                for(m = 0; m < 9; ++m) {
//                    this.addSlot(new Slot(inventory, m + n * 9, 8 + m * 18, 18 + n * 18));
//                }
//            }
//
//            for(n = 0; n < 3; ++n) {
//                for(m = 0; m < 9; ++m) {
//                    this.addSlot(new Slot(playerInventory, m + n * 9 + 9, 8 + m * 18, 103 + n * 18 + i));
//                }
//            }
//
//            for(n = 0; n < 9; ++n) {
//                this.addSlot(new Slot(playerInventory, n, 8 + n * 18, 161 + i));
//            }
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return true;
        }
    }

    protected static class AssortmentInventory implements Inventory {
        private final int size;
        private final ItemStack parent;
        private final DefaultedList<ItemStack> stacks;

        public AssortmentInventory(int size, ItemStack parent) {
            this.size = size;
            this.parent = parent;
            this.stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return stacks.stream().allMatch(ItemStack::isEmpty);
        }

        @Override
        public ItemStack getStack(int slot) {
            return stacks.get(slot);
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            return removeStack(slot);
        }

        @Override
        public ItemStack removeStack(int slot) {
            return stacks.set(slot, ItemStack.EMPTY);
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            if (slot >= 0 && slot < size) {
                stacks.set(slot, stack);
                stack.setCount(getMaxCountPerStack());
            }
        }

        @Override
        public int getMaxCountPerStack() {
            return 1;
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return true;
        }

        @Override
        public void onOpen(PlayerEntity player) {
            Inventory.super.onOpen(player);
            Inventories.readNbt(parent.getOrCreateNbt(), stacks);
        }

        @Override
        public void onClose(PlayerEntity player) {
            Inventory.super.onClose(player);
            Inventories.writeNbt(parent.getOrCreateNbt(), stacks);
        }

        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return !stack.isOf(ExplorersDelight.ASSORTMENT_POUCH);
        }

        @Override
        public int count(Item item) {
            return (int) stacks.stream().filter(stack -> stack.isOf(item)).count();
        }

        @Override
        public boolean containsAny(Set<Item> items) {
            return stacks.stream().anyMatch(stack -> items.contains(stack.getItem()));
        }

        @Override
        public void clear() {
            stacks.clear();
        }
    }
}
