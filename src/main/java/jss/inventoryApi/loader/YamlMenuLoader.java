package jss.inventoryApi.loader;

import jss.inventoryApi.wrappers.RegisteredInventory;
import jss.inventoryApi.api.InventoryApi;
import jss.inventoryApi.api.MenuRegistry;
import jss.inventoryApi.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class YamlMenuLoader {

    private final File menuFolder;
    private final JavaPlugin plugin;
    private final Map<UUID, Map<String, String>> playerVariables = new HashMap<>();


    public YamlMenuLoader(File pluginFolder, JavaPlugin plugin) {
        this.menuFolder = new File(pluginFolder, "menus");
        this.plugin = plugin;
        if (!menuFolder.exists()) menuFolder.mkdirs();
    }

    public void loadMenus() {
        for (File file : menuFolder.listFiles((dir, name) -> name.endsWith(".yml"))) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            String id = file.getName().replace(".yml", "").toLowerCase();

            MenuRegistry.register(id, player -> openYamlMenu(player, config));
        }
    }

    private void openYamlMenu(Player player, @NotNull YamlConfiguration config) {
        String title = config.getString("title", "§7Menú");
        int size = config.getInt("size", 27);

        boolean isPerPlayer = config.getBoolean("per-player", false);
        Inventory inv = isPerPlayer
                ? Bukkit.createInventory(player, size, title)
                : Bukkit.createInventory(null, size, title);

        RegisteredInventory regInv = new RegisteredInventory(inv);

        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            int slot = Integer.parseInt(key);
                ConfigurationSection section = config.getConfigurationSection("items." + key);


            // ⛔ Verificar permiso
            if (section.contains("require-permission")) {
                String permission = section.getString("require-permission");
                if (!player.hasPermission(permission)) {
                    continue; // No mostrar el ítem
                }
            }

            // Condición: requiere ítem
            if (section.contains("require-item")) {
                String required = section.getString("require-item");
                Material mat = Material.getMaterial(required);
                if (mat == null || !player.getInventory().contains(mat)) {
                    continue;
                }
            }

            if (section.contains("if-variable")) {
                if (!checkVariable(player, section.getString("if-variable"))) {
                    continue;
                }
            }


            Material material = Material.getMaterial(section.getString("material", "STONE"));
            if (material == null) continue;

            ItemBuilder builder = new ItemBuilder(material);
            if (section.contains("name")) builder.setName(section.getString("name"));
            if (section.contains("lore")) builder.setLore(section.getStringList("lore"));

            inv.setItem(slot, builder.build());

            List<String> actions = section.getStringList("actions");
            boolean requiresConfirm = section.getBoolean("confirm", false);

            if (!actions.isEmpty()) {
                regInv.setAction(slot, e -> {
                    if (requiresConfirm) {
                        openConfirmMenu(player, inv.getItem(slot), actions);
                    } else {
                        int delay = section.getInt("delay", 0);
                        if (delay > 0) {
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                actions.forEach(act -> runAction(player, act));
                            }, delay);
                        } else {
                            actions.forEach(act -> runAction(player, act));
                        }
                    }
                });


                // Sonido al hacer clic
                if (section.contains("sound")) {
                    String soundName = section.getString("sound");
                    regInv.setAction(slot, e -> {
                        try {
                            Sound sound = Sound.valueOf(soundName.toUpperCase());
                            player.playSound(player.getLocation(), sound, 1f, 1f);
                        } catch (IllegalArgumentException ex) {
                            player.sendMessage("§c[MenuYAML] Sonido inválido: " + soundName);
                        }
                    });
                }

            }

        }


        InventoryApi.openInventory(player, regInv);
    }

    private void openConfirmMenu(Player player, ItemStack originalItem, List<String> actions) {
        Inventory confirmInv = Bukkit.createInventory(null, 9, "§c¿Confirmar acción?");
        RegisteredInventory reg = new RegisteredInventory(confirmInv);

        ItemStack aceptar = new ItemBuilder(Material.GREEN_WOOL).setName("&aConfirmar").build();
        ItemStack cancelar = new ItemBuilder(Material.RED_WOOL).setName("&cCancelar").build();

        confirmInv.setItem(3, aceptar);
        confirmInv.setItem(5, cancelar);
        confirmInv.setItem(4, originalItem);

        reg.setAction(3, e -> {
            for (String action : actions) {
                runAction(player, action);
            }
        });

        reg.setAction(5, e -> player.closeInventory());

        InventoryApi.openInventory(player, reg);
    }


    private void runAction(Player player, String action) {
        action = replacePlaceholders(action, player);

        if (action.startsWith("message:")) {
            player.sendMessage("§a" + action.substring("message:".length()));

        } else if (action.startsWith("give:")) {
            String matName = action.substring("give:".length());
            Material mat = Material.getMaterial(matName);
            if (mat != null) {
                player.getInventory().addItem(new ItemStack(mat));
            }

        } else if (action.startsWith("command:")) {
            String cmd = action.substring("command:".length());
            player.performCommand(cmd);

        } else if (action.equalsIgnoreCase("close")) {
            player.closeInventory();
        } else if (action.startsWith("console:")) {
            String cmd = action.substring("console:".length());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        } else if (action.startsWith("open-menu:")) {
            String menuId = action.substring("open-menu:".length());
            if (!MenuRegistry.open(menuId, player)) {
                player.sendMessage("§cNo se pudo abrir el menú: " + menuId);
            }
        } else if (action.startsWith("set-variable:")) {
            String[] parts = action.substring("set-variable:".length()).split("=", 2);
            if (parts.length == 2) {
                setVariable(player, parts[0], parts[1]);
            }
        }

    }

    private String replacePlaceholders(String input, @NotNull Player player) {
        input = input.replace("%player%", player.getName())
                .replace("%uuid%", player.getUniqueId().toString());

        // Soporte PlaceholderAPI
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            input = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, input);
        }

        return input;
    }

    private void setVariable(@NotNull Player player, String key, String value) {
        playerVariables.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(key, value);
    }

    private boolean checkVariable(Player player, @NotNull String condition) {
        String[] parts = condition.split("=", 2);
        if (parts.length != 2) return false;
        String key = parts[0];
        String expected = parts[1];

        return expected.equals(
                playerVariables.getOrDefault(player.getUniqueId(), new HashMap<>()).get(key)
        );
    }


}
