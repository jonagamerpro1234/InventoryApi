package jss.inventoryApi.menus;

import jss.inventoryApi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class PaginatedMenu<T> extends CustomMenu {

    protected int currentPage = 0;
    protected final int pageSize;
    protected final List<T> items;

    public PaginatedMenu(Player player, String title, int size, int pageSize, List<T> items) {
        super(player, title, size);
        this.pageSize = pageSize;
        this.items = items;
    }

    /**
     * Crea los ítems dinámicos en la página actual.
     */
    protected abstract void renderItem(T item, int slot);

    /**
     * Define el slot inicial para ítems dinámicos.
     */
    protected abstract int getStartSlot();

    @Override
    public void setItems() {
        getInventory().clear();
        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, items.size());
        int slot = getStartSlot();

        for (int i = start; i < end; i++) {
            renderItem(items.get(i), slot++);
        }

        setupNavigationButtons();
    }

    private void setupNavigationButtons() {
        // Botón anterior
        if (currentPage > 0) {
            ItemStack prev = new ItemBuilder(Material.ARROW)
                    .setName("&a← Página anterior")
                    .build();

            getInventory().setItem(getInventory().getSize() - 9, prev);
            setAction(getInventory().getSize() - 9, e -> {
                currentPage--;
                open();
            });
        }

        // Botón siguiente
        if ((currentPage + 1) * pageSize < items.size()) {
            ItemStack next = new ItemBuilder(Material.ARROW)
                    .setName("&aSiguiente página →")
                    .build();

            getInventory().setItem(getInventory().getSize() - 1, next);
            setAction(getInventory().getSize() - 1, e -> {
                currentPage++;
                open();
            });
        }
    }
}
