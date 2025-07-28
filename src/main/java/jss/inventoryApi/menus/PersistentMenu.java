package jss.inventoryApi.menus;

import jss.inventoryApi.storage.InventoryStorageUtil;
import jss.inventoryApi.wrappers.RegisteredInventory;
import jss.inventoryApi.api.InventoryApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PersistentMenu {

    private static final Map<UUID, ItemStack[]> storage = new HashMap<>();

    protected final Player player;
    protected final Inventory inventory;
    protected final UUID uuid;
    protected final String title;
    protected final int size;

    public PersistentMenu(@NotNull Player player, String title, int size) {
        this.player = player;
        this.title = title;
        this.size = size;
        this.uuid = player.getUniqueId();
        this.inventory = Bukkit.createInventory(null, size, title);

        // Cargar desde disco
        InventoryStorageUtil.loadInventory(uuid, inventory);
    }

    public void open() {
        RegisteredInventory reg = new RegisteredInventory(inventory);

        // Guardar automáticamente al cerrar
        reg.setAction(-1, event -> save());

        InventoryApi.registerInventory(player, reg);
        player.openInventory(inventory);
    }

    public void save() {
        InventoryStorageUtil.saveInventory(uuid, inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
