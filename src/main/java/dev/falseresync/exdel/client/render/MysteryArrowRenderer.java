package dev.falseresync.exdel.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import dev.falseresync.exdel.entity.MysteryArrowEntity;

public class MysteryArrowRenderer extends ProjectileEntityRenderer<MysteryArrowEntity> {
    public static final Identifier TEXTURE = new Identifier("exdel:textures/entity/mystery_arrow.png");

    public MysteryArrowRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(MysteryArrowEntity arrowEntity) {
        return TEXTURE;
    }
}
