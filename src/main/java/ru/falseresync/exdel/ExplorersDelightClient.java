package ru.falseresync.exdel;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class ExplorersDelightClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ExplorersDelight.LUMINOUS_ORB, RenderLayer.getCutout());
//        ScreenRegistry.register(ExplorersDelight.ASSORTMENT_SCREEN_HANDLER, GenericContainerScreen::new);
    }
}
