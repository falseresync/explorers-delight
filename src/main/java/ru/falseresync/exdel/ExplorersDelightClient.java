package ru.falseresync.exdel;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import ru.falseresync.exdel.client.render.MysteryArrowRenderer;
import ru.falseresync.exdel.item.AssortmentPouchItem;

@Environment(EnvType.CLIENT)
public class ExplorersDelightClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // BlockRenderLayers
        BlockRenderLayerMap.INSTANCE.putBlock(ExplorersDelight.LUMINOUS_ORB, RenderLayer.getCutout());

        // EntityRenderers
        EntityRendererRegistry.register(ExplorersDelight.MYSTERY_ARROW_TYPE, MysteryArrowRenderer::new);

        // Screens
        HandledScreens.register(ExplorersDelight.ASSORTMENT_SCREEN_HANDLER, AssortmentPouchItem.AssortmentScreen::new);
    }
}
