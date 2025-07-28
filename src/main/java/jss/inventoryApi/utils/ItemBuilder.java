package jss.inventoryApi.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public ItemBuilder setLore(String... lines) {
        meta.setLore(format(lines));
        return this;
    }

    public ItemBuilder setLore(List<String> lines) {
        meta.setLore(format(lines));
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level, boolean unsafe) {
        meta.addEnchant(ench, level, unsafe);
        return this;
    }

    public ItemBuilder hideAttributes() {
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        return this;
    }

    public ItemBuilder hideEnchants() {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    private @NotNull List<String> format(String @NotNull ... lines) {
        List<String> formatted = new ArrayList<>();
        for (String line : lines) {
            formatted.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return formatted;
    }

    private @NotNull List<String> format(@NotNull List<String> lines) {
        List<String> formatted = new ArrayList<>();
        for (String line : lines) {
            formatted.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return formatted;
    }
}
