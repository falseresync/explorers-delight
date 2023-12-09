package dev.falseresync.exdel.client;

import dev.falseresync.exdel.ExDel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import dev.falseresync.exdel.client.render.MysteryArrowRenderer;
import dev.falseresync.exdel.item.AssortmentPouchItem;

@Environment(EnvType.CLIENT)
public class ExDelClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // BlockRenderLayers
        BlockRenderLayerMap.INSTANCE.putBlock(ExDel.LUMINOUS_ORB, RenderLayer.getCutout());

        // EntityRenderers
        EntityRendererRegistry.register(ExDel.MYSTERY_ARROW_TYPE, MysteryArrowRenderer::new);

        // Screens
        HandledScreens.register(ExDel.ASSORTMENT_SCREEN_HANDLER, AssortmentPouchItem.AssortmentScreen::new);
    }
}
