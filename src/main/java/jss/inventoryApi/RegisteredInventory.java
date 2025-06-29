package jss.inventoryApi;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RegisteredInventory {

    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> slotActions = new HashMap<>();

    public RegisteredInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setAction(int slot, Consumer<InventoryClickEvent> action) {
        slotActions.put(slot, action);
    }

    public void handleClick(@NotNull InventoryClickEvent event) {
        Consumer<InventoryClickEvent> action = slotActions.get(event.getSlot());
        if (action != null) {
            action.accept(event);
        }
    }

}
