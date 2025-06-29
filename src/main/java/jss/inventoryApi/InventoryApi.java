package jss.inventoryApi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class InventoryApi {

    private static final Map<Player, RegisteredInventory> registeredInventories = new HashMap<>();

    public static void registerInventory(Player player, RegisteredInventory regInv) {
        registeredInventories.put(player, regInv);
    }

    public static void openInventory(Player player, RegisteredInventory regInv) {
        registerInventory(player, regInv);
        player.openInventory(regInv.getInventory());
    }

    public static boolean isRegistered(Player player, Inventory inventory) {
        RegisteredInventory regInv = registeredInventories.get(player);
        return regInv != null && regInv.getInventory().equals(inventory);
    }

    public static void unregisterInventory(Player player) {
        registeredInventories.remove(player);
    }

    public static RegisteredInventory getRegisteredInventory(Player player) {
        return registeredInventories.get(player);
    }

}
