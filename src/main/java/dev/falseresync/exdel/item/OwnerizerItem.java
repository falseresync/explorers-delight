package dev.falseresync.exdel.item;

import dev.falseresync.exdel.ExDel;
import eu.pb4.common.protection.api.CommonProtection;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class OwnerizerItem extends Item {
    public OwnerizerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var pos = context.getBlockPos();
        var player = context.getPlayer();
        if (context.getWorld() instanceof ServerWorld world
                && player != null
                && player.isSneaking()
                && CommonProtection.canInteractBlock(world, pos, player.getGameProfile(), player)
        ) {
            var ownable = ExDel.OWNABLE.find(world, pos, null, world.getBlockEntity(pos), null);
            if (ownable == null) {
                player.sendMessage(Text.translatable("message.exdel.ownerizer.unownable"));
                return ActionResult.FAIL;
            }

            var playerUuid = player.getUuid();
            var currentOwnerUuid = ownable.exdel$getOwnerUuid();
            if (currentOwnerUuid != null && currentOwnerUuid != playerUuid) {
                player.sendMessage(Text.translatable("message.exdel.ownerizer.already_owned"));
                return ActionResult.FAIL;
            }

            ownable.exdel$setOwnerUuid(currentOwnerUuid == playerUuid ? null : playerUuid);
            player.sendMessage(Text.translatable("message.exdel.ownerizer.success"));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
