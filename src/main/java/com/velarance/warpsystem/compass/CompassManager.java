package com.velarance.warpsystem.compass;

import com.velarance.warpsystem.WarpSystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

public class CompassManager {
    private final NamespacedKey compassKey;

    public CompassManager(WarpSystem loader) {
        this.compassKey = new NamespacedKey(loader, "warp_compass");
    }

    public void giveWarpCompass(Player player, Location location) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta meta = (CompassMeta) compass.getItemMeta();

        if (meta != null) {
            meta.getPersistentDataContainer().set(compassKey, PersistentDataType.INTEGER, 1);

            meta.setLodestoneTracked(false);
            meta.setLodestone(location);
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            compass.setItemMeta(meta);

            player.getInventory().addItem(compass);
        }
    }

    public boolean isWarpCompass(ItemStack item) {
        CompassMeta meta = (CompassMeta) item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(compassKey, PersistentDataType.INTEGER);
    }
}
