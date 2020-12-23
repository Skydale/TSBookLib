package io.github.twilight_book.items;

import com.sun.istack.internal.Nullable;
import io.github.twilight_book.utils.config.ConfigAbstract;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemInstance { //represents a single ItemStack
    private final String NAME;
    private final List<String> LORE;
    private final String PATH;
    private final Material MATERIAL;
    private YamlConfiguration CONFIG;
    private int MODEL = 0;
    private List<String> LoreWithStats = new ArrayList<>();
    private ItemIdentification IDENTIFICATION;

    public ItemInstance(ConfigAbstract config, String ID, String path) {
        switch (path.toLowerCase()) {
            case "item":
                CONFIG = config.getItemByID(ID);
                break;
            case "unid":
                CONFIG = config.getUnidentifiedByID(ID);
                break;
        }
        NAME = config.getLang().translate    ("FORMAT.NAME", null, CONFIG);
        LORE = config.getLang().translateList("FORMAT.LORE", null, CONFIG);

        String material = CONFIG.getString("MATERIAL");
        if (material == null) throw new IllegalArgumentException("Cannot get Material of the item.");
        MATERIAL = Material.getMaterial(material);

        if (CONFIG.contains("MODEL")) {
            MODEL = CONFIG.getInt("MODEL");
        }
        PATH = path;
    }

    public ItemInstance(ConfigAbstract config, YamlConfiguration setting, String ID, String path) {
        CONFIG = setting;
        NAME = config.getLang().translate    ("FORMAT.NAME", null, CONFIG);
        LORE = config.getLang().translateList("FORMAT.LORE", null, CONFIG);

        String material = CONFIG.getString("MATERIAL");
        if (material == null) throw new IllegalArgumentException("Cannot get Material of the item.");
        MATERIAL = Material.getMaterial(material);

        if (CONFIG.contains("MODEL")) {
            MODEL = CONFIG.getInt("MODEL");
        }
        PATH = path;
    }

    private ItemInstance(String ID, List<String> lore) { //TODO identification also unused
        NAME = null; //for using "final"
        PATH = null;
        CONFIG = null;
        MATERIAL = null;
        LORE = lore;
    }

    public String getName() {
        return NAME;
    }

    public List<String> getLore() {
        return LORE;
    }

    public void updateLore(){
        LoreWithStats.clear();
        for(String s:LORE){
            String l = s.replaceAll("\\[item.damage-physical]",IDENTIFICATION.getStats(ItemUtils.StatsType.PHYSICAL)+"")
                    .replaceAll("\\[item.damage-terra]",IDENTIFICATION.getStats(ItemUtils.StatsType.TERRA)+"")
                    .replaceAll("\\[item.damage-aer]",IDENTIFICATION.getStats(ItemUtils.StatsType.AER)+"")
                    .replaceAll("\\[item.damage-ignis]",IDENTIFICATION.getStats(ItemUtils.StatsType.IGNIS)+"")
                    .replaceAll("\\[item.damage-aqua]",IDENTIFICATION.getStats(ItemUtils.StatsType.AQUA)+"")
                    .replaceAll("\\[item.damage-lumen]",IDENTIFICATION.getStats(ItemUtils.StatsType.LUMEN)+"")
                    .replaceAll("\\[item.damage-umbra]",IDENTIFICATION.getStats(ItemUtils.StatsType.UMBRA)+"");
            LoreWithStats.add(l);
        }
    }

    public List<String> getLoreWithStats(){
        return LoreWithStats;
    }

    public YamlConfiguration getConfig(){
        return CONFIG;
    }

    @Nullable
    public ItemIdentification getIdentification(){
        return IDENTIFICATION;
    }

    public void reIdentify(){
        //TODO if(not identifiable) return;
        if (IDENTIFICATION == null)
            IDENTIFICATION = new ItemIdentification(CONFIG,new Random());
        else
            IDENTIFICATION.rollStats();
    }

    public ItemStack createItem(JavaPlugin plugin) {
        return ItemUtils.createItem(plugin, this, PATH);
    }
}