package jss.inventoryApi.storage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class InventoryStorageUtil {

    private static File dataFolder;

    public static void setup(File pluginDataFolder) {
        dataFolder = new File(pluginDataFolder, "InvData");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public static void saveInventory(@NotNull UUID uuid, @NotNull Inventory inv) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        YamlConfiguration config = new YamlConfiguration();

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                config.set("inventory." + i, item);
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadInventory(@NotNull UUID uuid, Inventory inv) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        if (!file.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getConfigurationSection("inventory").getKeys(false)) {
            int slot = Integer.parseInt(key);
            ItemStack item = config.getItemStack("inventory." + key);
            inv.setItem(slot, item);
        }
    }
}
