package dev.falseresync.exdel;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

import java.util.Optional;

public final class TagUtil {
    public static <T> Optional<T> nextRandomEntry(ServerWorld world, TagKey<T> tag, Random random) {
        return world.getRegistryManager()
                .getOptional(tag.registry())
                .map(registry -> registry.getOrCreateEntryList(tag))
                .flatMap(entries -> entries.getRandom(random).map(RegistryEntry::value));
    }
}
