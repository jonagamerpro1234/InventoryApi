package jss.inventoryApi;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent e){
        //verifica si al momento de hacer clic es un jugador, si no retorna y vuelve a intentar
        if (!(e.getWhoClicked() instanceof Player player)) return;

        RegisteredInventory regInv = InventoryApi.getRegisteredInventory(player);
        if (regInv != null && e.getInventory().equals(regInv.getInventory())) {
            e.setCancelled(true); // prevenir que muevan cosas
            regInv.handleClick(e); // ejecutar acción si existe
        }
    }

    @EventHandler
    public void onClose(@NotNull InventoryCloseEvent e){
        //verifica si el inventari esta abierto por un jugador, antes de cerralo, eliminar el inventario de la cachel
        if(!(e.getPlayer() instanceof Player player)) return;

        //Limpia el regristro de inventarios
        InventoryApi.unregisterInventory(player);
    }

}
