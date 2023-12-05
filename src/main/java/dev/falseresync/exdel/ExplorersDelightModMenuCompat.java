package dev.falseresync.exdel;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.falseresync.exdel.ExplorersDelight;
import ru.falseresync.exdel.ExplorersDelightConfig;

@Environment(EnvType.CLIENT)
public class ExplorersDelightModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ExplorersDelight.CONFIG::createScreen;
    }
}
