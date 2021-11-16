package ru.falseresync.exdel.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.falseresync.exdel.entity.MysteryArrowEntity;

public class MysteryArrowItem extends ArrowItem {
    public MysteryArrowItem(Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new MysteryArrowEntity(world, shooter);
    }
}
