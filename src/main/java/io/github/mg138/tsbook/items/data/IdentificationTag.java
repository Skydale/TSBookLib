package io.github.mg138.tsbook.items.data;

import com.google.gson.JsonSyntaxException;

import io.github.mg138.tsbook.Book;
import io.github.mg138.tsbook.items.ItemIdentification;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class IdentificationTag implements PersistentDataType<String, ItemIdentification> {
    @NotNull
    @Override
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @NotNull
    @Override
    public Class<ItemIdentification> getComplexType() {
        return ItemIdentification.class;
    }

    @NotNull
    @Override
    public String toPrimitive(@NotNull ItemIdentification itemIdentification, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String result = null;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            result = Book.Companion.getGson().toJson(itemIdentification);
            oos.writeObject(result);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert result != null;
        return result;
    }

    @NotNull
    @Override
    public ItemIdentification fromPrimitive(@NotNull String string, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ItemIdentification result = null;
        try {
            result = Book.Companion.getGson().fromJson(string, ItemIdentification.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        assert result != null;
        return result;
    }
}