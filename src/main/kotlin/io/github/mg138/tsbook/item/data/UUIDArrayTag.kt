package io.github.mg138.tsbook.item.data;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

public class UUIDArrayTag implements PersistentDataType<byte[], UUID[]> {
    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<UUID[]> getComplexType() {
        return UUID[].class;
    }

    @NotNull
    @Override
    public byte[] toPrimitive(UUID[] complex, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16 * complex.length]);
        for (UUID uuid : complex) {
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
        }
        return bb.array();
    }

    @NotNull
    @Override
    public UUID[] fromPrimitive(@NotNull byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        List<UUID> uuids = new ArrayList<>();
        ByteBuffer bb = ByteBuffer.wrap(primitive);
        for (int i = 0; i < primitive.length / 16; i++) {
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            uuids.add(new UUID(firstLong, secondLong));
        }
        return uuids.toArray(new UUID[0]);
    }
}