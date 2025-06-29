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
        if(!(e.getWhoClicked() instanceof Player player)) return;

        //obtiene el inventario
        Inventory clickedInventory = e.getInventory();

        //verifica sie esta registrado antes de ejecutar alguna accion
        if(InventoryApi.isRegistered(player, clickedInventory)){
            e.setCancelled(true);
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
