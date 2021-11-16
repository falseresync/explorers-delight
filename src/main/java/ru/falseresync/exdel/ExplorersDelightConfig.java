package ru.falseresync.exdel;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public record ExplorersDelightConfig() {
    public static final String CONFIG_FILENAME = "explorers-delight.json";
    private static ExplorersDelightConfig instance;

    public static ExplorersDelightConfig get() {
        if (instance == null) {
            load();
        }

        return instance;
    }

    public static void load() {
        var gson = new Gson();
        var configPath = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), CONFIG_FILENAME);
        try {
            instance = gson.fromJson(Files.readString(configPath), ExplorersDelightConfig.class);
        } catch (IOException e) {
            try {
                var inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_FILENAME);
                if (inputStream == null) {
                    throw new IllegalArgumentException("Default config file not available! " + CONFIG_FILENAME);
                }
                Files.copy(inputStream, configPath);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
