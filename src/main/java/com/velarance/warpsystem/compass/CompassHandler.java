package com.velarance.warpsystem.compass;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class CompassHandler implements Listener {
    private final CompassManager compassManager;

    public CompassHandler(CompassManager compassManager) {
        this.compassManager = compassManager;
    }

    @EventHandler
    public void handlePlayerDropItem(PlayerDropItemEvent event) {
        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (droppedItem.getType() == Material.COMPASS && compassManager.isWarpCompass(droppedItem)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleItemMove(InventoryClickEvent event){
        Inventory top = event.getView().getTopInventory();
        Inventory bottom = event.getView().getBottomInventory();

        if (top.getType() != InventoryType.PLAYER && bottom.getType() == InventoryType.PLAYER) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.COMPASS && compassManager.isWarpCompass(event.getCurrentItem())) {
                if (top.getType() != InventoryType.CRAFTING) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void handlePlayerDeath(PlayerDeathEvent event) {
        List<ItemStack> drops = event.getDrops();
        drops.removeIf(item -> item.getType() == Material.COMPASS && compassManager.isWarpCompass(item));
    }
}
