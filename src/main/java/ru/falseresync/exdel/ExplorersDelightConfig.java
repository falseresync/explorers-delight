package ru.falseresync.exdel;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ExplorersDelightConfig {
    public static ConfigClassHandler<ExplorersDelightConfig> HANDLER = ConfigClassHandler
            .createBuilder(ExplorersDelightConfig.class)
            .id(new Identifier("exdel", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("exdel.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build()
            )
            .build();

    @SerialEntry
    public MysteryArrowConfig mysteryArrow = new MysteryArrowConfig();

    @Environment(EnvType.CLIENT)
    public Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Used for narration. Could be used to render a title in the future."))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Name of the category"))
                        .tooltip(Text.literal("This text will appear as a tooltip when you hover or focus the button with Tab. There is no need to add \n to wrap as YACL will do it for you."))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Name of the group"))
                                .description(OptionDescription.of(Text.literal("This text will appear when you hover over the name or focus on the collapse button with Tab.")))
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.literal("Boolean Option"))
                                        .description(OptionDescription.of(Text.literal("This text will appear as a tooltip when you hover over the option.")))
                                        .binding(20, () -> this.mysteryArrow.agingWeight, newVal -> this.mysteryArrow.agingWeight = newVal)
                                        .controller(IntegerFieldControllerBuilder::create)
                                        .build()
                                )
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.literal("Boolean Option"))
                                        .description(OptionDescription.of(Text.literal("This text will appear as a tooltip when you hover over the option.")))
                                        .binding(4, () -> this.mysteryArrow.transformationWeight, newVal -> this.mysteryArrow.transformationWeight = newVal)
                                        .controller(IntegerFieldControllerBuilder::create)
                                        .build()
                                )
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.literal("Boolean Option"))
                                        .description(OptionDescription.of(Text.literal("This text will appear as a tooltip when you hover over the option.")))
                                        .binding(4, () -> this.mysteryArrow.doNothingWeight, newVal -> this.mysteryArrow.doNothingWeight = newVal)
                                        .controller(IntegerFieldControllerBuilder::create)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build()
                .generateScreen(parent);
    }

    public static class MysteryArrowConfig {
        public int agingWeight = 20;
        public int transformationWeight = 4;
        public int doNothingWeight = 4;
    }
}
