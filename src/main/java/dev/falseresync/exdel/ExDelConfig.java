package dev.falseresync.exdel;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ExDelConfig {
    public static final ConfigClassHandler<ExDelConfig> HANDLER = ConfigClassHandler
            .createBuilder(ExDelConfig.class)
            .id(new Identifier("exdel", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("exdel.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build()
            ).build();

    @SerialEntry
    public MysteryArrowConfig mysteryArrow = new MysteryArrowConfig();

    @Environment(EnvType.CLIENT)
    public Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("config.exdel.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.exdel.general"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("config.exdel.general.mystery_arrow"))
                                .description(OptionDescription.of(Text.translatable("config.exdel.general.mystery_arrow.description")))
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("config.exdel.general.mystery_arrow.aging_weight"))
                                        .binding(HANDLER.defaults().mysteryArrow.agingWeight, () -> mysteryArrow.agingWeight, newVal -> mysteryArrow.agingWeight = newVal)
                                        .controller(IntegerFieldControllerBuilder::create)
                                        .build()
                                ).option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("config.exdel.general.mystery_arrow.transformation_weight"))
                                        .binding(HANDLER.defaults().mysteryArrow.transformationWeight, () -> mysteryArrow.transformationWeight, newVal -> mysteryArrow.transformationWeight = newVal)
                                        .controller(IntegerFieldControllerBuilder::create)
                                        .build()
                                ).option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("config.exdel.general.mystery_arrow.do_nothing_weight"))
                                        .binding(HANDLER.defaults().mysteryArrow.doNothingWeight, () -> mysteryArrow.doNothingWeight, newVal -> mysteryArrow.doNothingWeight = newVal)
                                        .controller(IntegerFieldControllerBuilder::create)
                                        .build()
                                ).build()
                        ).build()
                ).build()
                .generateScreen(parent);
    }

    public static class MysteryArrowConfig {
        public int agingWeight = 30;
        public int transformationWeight = 15;
        public int doNothingWeight = 5;
    }
}
