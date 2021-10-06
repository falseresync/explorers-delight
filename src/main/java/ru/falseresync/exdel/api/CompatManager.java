package ru.falseresync.exdel.api;

import net.fabricmc.loader.api.FabricLoader;
import ru.falseresync.exdel.api.InteractionDriver;
import ru.falseresync.exdel.compat.DefaultDriver;
import ru.falseresync.exdel.compat.FlanDriver;
import ru.falseresync.exdel.compat.GomlDriver;

import java.util.ArrayList;
import java.util.List;

public class CompatManager {
    private static final List<InteractionDriver> INTERACTION_PIPELINE = new ArrayList<>();

    public static void init() {
        INTERACTION_PIPELINE.add(new DefaultDriver());

        if (FabricLoader.getInstance().isModLoaded("flan")) {
            INTERACTION_PIPELINE.add(new FlanDriver());
        }

        if (FabricLoader.getInstance().isModLoaded("goml")) {
            INTERACTION_PIPELINE.add(new GomlDriver());
        }
    }

    public static List<InteractionDriver> getInteractionPipeline() {
        return INTERACTION_PIPELINE;
    }
}
