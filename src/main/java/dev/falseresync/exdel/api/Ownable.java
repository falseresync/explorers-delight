package dev.falseresync.exdel.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Ownable {
    @ApiStatus.Internal
    default boolean allowsLookup() {
        return true;
    }

    @Nullable
    UUID exdel$getOwnerUuid();

    void exdel$setOwnerUuid(@Nullable UUID ownerUuid);
}
