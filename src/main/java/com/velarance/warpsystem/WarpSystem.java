package com.velarance.warpsystem;

import com.velarance.warpsystem.commands.WarpCommand;
import com.velarance.warpsystem.warps.WarpManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpSystem extends JavaPlugin {
    private WarpManager warpManager;

    @Override
    public void onEnable() {
        this.warpManager = new WarpManager(this);
        warpManager.loadWarps();

        new WarpCommand(warpManager, this.getServer().getCommandMap());
    }

    @Override
    public void onDisable() {
        warpManager.saveWarps();
    }
}
