package com.velarance.warpsystem.warps;

import com.velarance.warpsystem.WarpSystem;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpManager {
    public static final String COMMAND_PREFIX = "Warps | ";

    private final Map<String, Warp> warps;

    private final File file;
    private final YamlConfiguration config;

    public WarpManager(WarpSystem loader) {
        this.warps = new HashMap<>();

        this.file = new File(loader.getDataFolder(), "warps.yml");
        this.config = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            loader.saveResource("warps.yml", false);
        }
    }

    public boolean isExistWarp(String warpName) {
        return warps.containsKey(warpName.toLowerCase());
    }

    public ArrayList<Warp> getPlayerWarps(String playerName) {
        ArrayList<Warp> playerWarps = new ArrayList<>();
        for (Warp warp : warps.values()) {
            if (warp.getOwner().equalsIgnoreCase(playerName)) {
                playerWarps.add(warp);
            }
        }
        return playerWarps;
    }

    public boolean createWarp(String warpName, String owner, Location location) {
        if (!isExistWarp(warpName)){
            Warp warp = new Warp(warpName, warpName.toLowerCase(), owner, location);

            warps.put(warpName.toLowerCase(), warp);
            saveWarp(warpName);
            return true;
        }
        return false;
    }

    public void removeWarp(String warpName) {
        if (isExistWarp(warpName)) {
            Warp warp = getWarp(warpName);

            warp.removeFromConfig(config);
            warps.remove(warpName.toLowerCase());
            try {
                config.save(file);
            } catch (IOException ignored) {}
        }
    }

    public Warp getWarp(String warpName) {
        return warps.get(warpName.toLowerCase());
    }

    public void addPlayerToWarp(String warpName, String player) {
        if (isExistWarp(warpName)) {
            getWarp(warpName).addAllowedPlayer(player);
            saveWarp(warpName);
        }
    }

    public void teleportToWarp(Player player, String warpName) {
        if (isExistWarp(warpName)) {
            player.teleport(getWarp(warpName).getLocation());
        }
    }

    public void saveWarp(String warpName) {
        Warp warp = getWarp(warpName);
        warp.saveToConfig(config);

        try {
            config.save(file);
        } catch (IOException ignored) {}
    }

    public void saveWarps() {
        for (Warp warp : warps.values()) {
            warp.saveToConfig(config);
        }

        try {
            config.save(file);
        } catch (IOException ignored) {}
    }

    public void loadWarps() {
        if (config.isConfigurationSection("warps")) {
            ConfigurationSection warpsSection = config.getConfigurationSection("warps");
            if (warpsSection != null) {
                for (String key : warpsSection.getKeys(false)) {
                    Warp warp = new Warp(key,
                            warpsSection.getString(key + ".lowerCaseName"),
                            warpsSection.getString(key + ".owner"),
                            warpsSection.getLocation(key + ".location"));

                    List<String> allowedPlayers = warpsSection.getStringList(key + ".allowedPlayers");
                    warp.getAllowedPlayers().addAll(allowedPlayers);

                    warps.put(key.toLowerCase(), warp);
                }
            }
        } else {
            config.createSection("warps");
        }
    }
}