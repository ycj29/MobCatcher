package com.ycjhub.mobcatcher;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class command implements CommandExecutor {
    private final MobCatcher plugin; // Reference to main plugin class
    private static final String PREFIX = ChatColor.of("#3ea4c9") + ChatColor.translateAlternateColorCodes('&' ,"&l捕捉蛋系統&r &8»");


    public command(MobCatcher plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            // 玩家執行指令 `/mobcatcher` 或 `/mobcatcher reusable`
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c請輸入玩家名稱來給予捕捉蛋！用法: /mobcatcher <玩家> [reusable]");
                return true;
            }

            Player player = (Player) sender;
            boolean isReusable = args.length > 0 && args[0].equalsIgnoreCase("reusable");

            plugin.giveMobCatcherEgg(player, isReusable);
            player.sendMessage("§a你獲得了一顆 " + (isReusable ? "§b可重複使用的" : "§6") + " 生物捕捉蛋！");
            return true;
        }

        // 主控台或管理員使用 `/mobcatcher <玩家> [reusable]`
        if (!sender.hasPermission("mobcatcher.give")) {
            sender.sendMessage("§c你沒有權限使用此指令！");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("§c找不到玩家 " + args[0] + "！");
            return true;
        }

        boolean isReusable = args.length > 1 && args[1].equalsIgnoreCase("reusable");
        plugin.giveMobCatcherEgg(target, isReusable);

        sender.sendMessage("§a成功給予 " + target.getName() + " 一顆 " + (isReusable ? "§b可重複使用的" : "§6") + " 生物捕捉蛋！");
        //target.sendMessage("§a你獲得了一顆 " + (isReusable ? "§b可重複使用的" : "§6") + " 生物捕捉蛋！");

        return true;
    }



}
