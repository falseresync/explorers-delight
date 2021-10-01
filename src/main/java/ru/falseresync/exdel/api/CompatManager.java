package ru.falseresync.exdel.api;

import net.fabricmc.loader.api.FabricLoader;
import ru.falseresync.exdel.compat.DefaultDriver;
import ru.falseresync.exdel.compat.FlanDriver;

public class CompatManager {
    private static InteractionDriver INTERACTION_DRIVER;

    public static void init() {
        if (FabricLoader.getInstance().isModLoaded("flan")) {
            INTERACTION_DRIVER = new FlanDriver();
        } else {
            INTERACTION_DRIVER = new DefaultDriver();
        }
    }

    public static InteractionDriver getInteractionDriver() {
        return INTERACTION_DRIVER;
    }
}
