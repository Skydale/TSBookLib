package io.github.mg138.tsbook.items.data;

import io.github.mg138.tsbook.items.ItemIdentification;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

public class Identification implements PersistentDataType<byte[], ItemIdentification> { //basically copied from the internet
    @NotNull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    @Override
    public Class<ItemIdentification> getComplexType() {
        return ItemIdentification.class;
    }

    @NotNull
    @Override
    public byte[] toPrimitive(@NotNull ItemIdentification itemIdentification, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        byte[] result = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(itemIdentification);
            out.flush();
            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ignored) {
            }
        }
        assert result != null;
        return result;
    }

    @NotNull
    @Override
    public ItemIdentification fromPrimitive(@NotNull byte[] bytes, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        ItemIdentification i = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            if (!(o instanceof ItemIdentification)) throw new IllegalArgumentException("Something went terribly wrong!");
            i = (ItemIdentification) o;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignored) {
            }
        }
        return Objects.requireNonNull(i);
    }
}