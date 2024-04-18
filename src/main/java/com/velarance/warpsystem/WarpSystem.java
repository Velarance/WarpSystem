package com.velarance.warpsystem;

import com.velarance.warpsystem.commands.WarpCommand;
import com.velarance.warpsystem.compass.CompassHandler;
import com.velarance.warpsystem.compass.CompassManager;
import com.velarance.warpsystem.warps.WarpManager;
import org.bukkit.plugin.java.JavaPlugin;

public class WarpSystem extends JavaPlugin {
    private WarpManager warpManager;
    private CompassManager compassManager;

    @Override
    public void onEnable() {
        this.warpManager = new WarpManager(this);
        this.compassManager = new CompassManager(this);
        warpManager.loadWarps();

        getServer().getPluginManager().registerEvents(new CompassHandler(compassManager), this);
        new WarpCommand(warpManager, compassManager, this.getServer().getCommandMap());
    }

    @Override
    public void onDisable() {
        warpManager.saveWarps();
    }
}
