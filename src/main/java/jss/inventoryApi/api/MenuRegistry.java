package jss.inventoryApi.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class MenuRegistry {

    private static final Map<String, Consumer<Player>> menus = new HashMap<>();

    /**
     * Registra un menú con un ID.
     */
    public static void register(@NotNull String id, Consumer<Player> opener) {
        menus.put(id.toLowerCase(), opener);
    }

    /**
     * Abre un menú registrado por ID.
     */
    public static boolean open(@NotNull String id, Player player) {
        Consumer<Player> opener = menus.get(id.toLowerCase());
        if (opener != null) {
            opener.accept(player);
            return true;
        }
        return false;
    }

    /**
     * Verifica si un ID está registrado.
     */
    public static boolean isRegistered(@NotNull String id) {
        return menus.containsKey(id.toLowerCase());
    }

    /**
     * Lista los IDs disponibles.
     */
    public static Map<String, Consumer<Player>> getMenus() {
        return menus;
    }

    /**
     * Lista los IDs disponibles.
     */
    @Contract(pure = true)
    public static @NotNull Set<String> getMenuIds() {
        return menus.keySet();
    }

    public static void clear() {
        menus.clear();
    }


}
