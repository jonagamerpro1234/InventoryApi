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

    private static final Map<Player, Inventory> registeredInventories = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Inicializa los listeners del sistema de inventarios.
     * Esto debe llamarse una vez desde el plugin principal.
     */
    public static void initialize(Plugin plugin) {
        if (initialized) return;
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryListener(), plugin);
        initialized = true;
    }

    /**
     * Registra un inventario personalizado asociado a un jugador.
     */
    public static void registerInventory(Player player, Inventory inventory) {
        registeredInventories.put(player, inventory);
    }

    /**
     * Desregistra un inventario.
     */
    public static void unregisterInventory(Player player) {
        registeredInventories.remove(player);
    }

    /**
     * Obtiene el inventario registrado para el jugador.
     */
    public static Inventory getInventory(Player player) {
        return registeredInventories.get(player);
    }

    /**
     * Abre el inventario al jugador y lo registra.
     */
    public static void openInventory(Player player, Inventory inventory) {
        registerInventory(player, inventory);
        player.openInventory(inventory);
    }

    /**
     * Verifica si el inventario abierto por el jugador está registrado.
     */
    public static boolean isRegistered(Player player, Inventory inventory) {
        return registeredInventories.get(player) == inventory;
    }

}
