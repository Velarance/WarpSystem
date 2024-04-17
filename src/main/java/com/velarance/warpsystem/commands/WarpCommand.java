package com.velarance.warpsystem.commands;

import com.velarance.warpsystem.warps.Warp;
import com.velarance.warpsystem.warps.WarpManager;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpCommand extends BukkitCommand {
    private final WarpManager warpManager;

    public WarpCommand(@NotNull WarpManager warpManager, CommandMap commandMap) {
        super("warp");
        commandMap.register("warp", this);

        this.warpManager = warpManager;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("watafak");
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(WarpManager.COMMAND_PREFIX +
                    "Используйте:\n" +
                    "/warp create <название_варпа> - создаёт варп\n" +
                    "/warp add <название_варпа> <ник_игрока> - позволяет добавить игрока в список игроков, которые могут телепортироваться на этот варп\n" +
                    "/warp remove <название_варпа> - удаляет варп, если он принадлежит игроку, который исполняет команду\n" +
                    "/warp teleport <название_варпа> - телепортирует на точку варпа"
            );
            return false;
        }

        String commandArg = args[0].toLowerCase();

        // wtf?? switch?????
        switch (commandArg) {
            case "create" -> {
                if (args.length > 1) {
                    String warpName = args[1];

                    boolean created = warpManager.createWarp(warpName, player.getName(), player.getLocation());
                    if (created) {
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Варп " + warpName + " создан!");
                    } else {
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Варп " + warpName + " уже существует");
                    }
                } else {
                    player.sendMessage(WarpManager.COMMAND_PREFIX + "Используйте: /warp create <название_варпа> - создаёт варп");
                }
            }
            case "add" -> {
                if (args.length > 2) {
                    String warpName = args[1];
                    String playerName = args[2];
                    if (!warpManager.isExistWarp(warpName)) {
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Варпа '" + warpName + "' не существует");
                        return true;
                    }

                    Warp warp = warpManager.getWarp(warpName);
                    if (!warp.getOwner().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Вы не являетесь владельцем данного варпа");
                        return true;
                    } else if (warp.getOwner().equalsIgnoreCase(playerName)) {
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Вы не можете добавить самого себя");
                        return true;
                    } else if (warp.isExistPlayer(playerName)) {
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Данный игрок уже имеет доступ к варпу " + warpName);
                        return true;
                    }

                    warpManager.addPlayerToWarp(warpName, playerName);
                    player.sendMessage(WarpManager.COMMAND_PREFIX + "Игроку " + playerName + " был выдан доступ к варпу " + warpName + "!");
                } else {
                    player.sendMessage(WarpManager.COMMAND_PREFIX + "Используйте: /warp add <название_варпа> <ник_игрока> - позволяет добавить игрока в список игроков, которые могут телепортироваться на этот варп");
                }
            }
            case "remove" -> {
                if (args.length > 1) {
                    String warpName = args[1];
                    if (!warpManager.isExistWarp(warpName)) {
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Варпа '" + warpName + "' не существует");
                        return true;
                    } else if (!warpManager.getWarp(warpName).getOwner().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Вы не являетесь владельцем данного варпа");
                        return true;
                    }

                    warpManager.removeWarp(warpName);
                    player.sendMessage(WarpManager.COMMAND_PREFIX + "Варп " + warpName + " удален!");
                } else {
                    player.sendMessage(WarpManager.COMMAND_PREFIX + "Используйте: /warp remove <название_варпа> - удаляет варп, если он принадлежит игроку, который исполняет команду");
                }
            }
            case "teleport" -> {
                if (args.length > 1) {
                    String warpName = args[1];
                    if (!warpManager.isExistWarp(warpName)) {
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Варпа '" + warpName + "' не существует");
                        return true;
                    } else if (!warpManager.getWarp(warpName).isExistPlayer(player.getName()) && !warpManager.getWarp(warpName).getOwner().equalsIgnoreCase(player.getName())){
                        player.sendMessage(WarpManager.COMMAND_PREFIX + "Вам не разрешено телепортироваться на варп '" + warpName);
                        return true;
                    }

                    warpManager.teleportToWarp(player, warpName);
                    player.sendMessage(WarpManager.COMMAND_PREFIX + "Вы успешно телепортировались на варп " + warpName);
                } else {
                    player.sendMessage(WarpManager.COMMAND_PREFIX + "Используйте: /warp teleport <название_варпа> - телепортирует на точку варпа");
                }
            }
            default -> player.sendMessage(WarpManager.COMMAND_PREFIX +
                    "Используйте:\n" +
                    "/warp create <название_варпа> - создаёт варп\n" +
                    "/warp add <название_варпа> <ник_игрока> - позволяет добавить игрока в список игроков, которые могут телепортироваться на этот варп\n" +
                    "/warp remove <название_варпа> - удаляет варп, если он принадлежит игроку, который исполняет команду\n" +
                    "/warp teleport <название_варпа> - телепортирует на точку варпа"
            );
        }

        return true;
    }
}
