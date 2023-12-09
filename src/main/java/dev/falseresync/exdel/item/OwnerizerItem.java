package dev.falseresync.exdel.item;

import dev.falseresync.exdel.ExDel;
import eu.pb4.common.protection.api.CommonProtection;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        Text.translatable("tooltip.exdel.ownerizer").visit(asString -> {
            for (String s : asString.split("\n")) {
                tooltip.add(Text.literal(s).styled(style -> style.withColor(Formatting.GRAY)));
            }
            return Optional.empty();
        });
    }
}
