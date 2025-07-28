package jss.inventoryApi.menus;

import jss.inventoryApi.wrappers.RegisteredInventory;
import jss.inventoryApi.api.InventoryApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

public abstract class CustomMenu {

    protected final Player player;
    protected final RegisteredInventory menu;

    public CustomMenu(Player player, String title, int size) {
        this.player = player;
        Inventory inventory = Bukkit.createInventory(null, size, title);
        this.menu = new RegisteredInventory(inventory);
    }

    /**
     * Método que define qué ítems se colocan en el menú.
     */
    public abstract void setItems();

    /**
     * Abre el menú al jugador, registrándolo en InventoryApi.
     */
    public void open() {
        setItems();
        InventoryApi.openInventory(player, menu);
    }

    /**
     * Atajo para acceder al inventario base.
     */
    public Inventory getInventory() {
        return menu.getInventory();
    }

    /**
     * Registra una acción para un slot específico.
     */
    public void setAction(int slot, Consumer<InventoryClickEvent> action) {
        menu.setAction(slot, action);
    }
}
