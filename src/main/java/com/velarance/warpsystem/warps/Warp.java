package com.velarance.warpsystem.warps;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Warp {
    private final String name;
    private final String lowerCaseName;

    private final String owner;
    private final Location location;
    private final List<String> allowedPlayers;

    public Warp(String name, String lowerCaseName, String owner, Location location) {
        this.name = name;
        this.lowerCaseName = lowerCaseName;

        this.owner = owner;
        this.location = location;
        this.allowedPlayers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getLowerCaseName() {
        return lowerCaseName;
    }

    public String getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getAllowedPlayers() {
        return allowedPlayers;
    }

    public boolean isExistPlayer(String player) {
        return allowedPlayers.contains(player.toLowerCase());
    }

    public void addAllowedPlayer(String player) {
        allowedPlayers.add(player.toLowerCase());
    }

    public void saveToConfig(YamlConfiguration config) {
        config.set("warps." + name + ".lowerCaseName", lowerCaseName);
        config.set("warps." + name + ".owner", owner);
        config.set("warps." + name + ".location", location);
        config.set("warps." + name + ".allowedPlayers", allowedPlayers);
    }

    public void removeFromConfig(YamlConfiguration config) {
        config.set("warps." + name, null);
    }
}
