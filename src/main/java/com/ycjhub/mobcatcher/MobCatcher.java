package com.ycjhub.mobcatcher;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class MobCatcher extends JavaPlugin {
    private static final String PREFIX = ChatColor.of("#3ea4c9") + ChatColor.translateAlternateColorCodes('&', "&l捕捉蛋系統&r &8»");


    @Override
    public void onEnable() {
        getCommand("mobcatcher").setExecutor(new command(this));
        Bukkit.getPluginManager().registerEvents(new MobCatcherListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void giveMobCatcherEgg(Player player, boolean isReusable) {
        ItemStack egg = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = egg.getItemMeta();
        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(this, "cjcatcher");
            NamespacedKey reusableKey = new NamespacedKey(this, "reusable");

            // 標記為生物捕捉蛋
            container.set(key, PersistentDataType.BOOLEAN, true);
            meta.setDisplayName(isReusable ? "§6生物捕捉蛋§f (可重複使用)" : "§6生物捕捉蛋");

            // 設定物品 Lore (說明)
            List<String> lore = Arrays.asList(
                    "§7右鍵點擊可投擲",
                    "§7擊中可收納的生物將轉換為生物蛋",
                    "§7不可收納擁有名稱標籤的生物",
                    "",
                    "§f可收納的生物: §7村民、綿羊",
                    "§7熊貓、青蛙、狼、豬、牛、貓",
                    "§7馬、驢、騾、蝙蝠、駱駝、嗅探獸",
                    "§7熾足獸、流浪商人、山羊、山貓",
                    "§7蜜蜂、狐狸、駱馬、北極熊、悅靈",
                    "§7"
            );

            meta.setLore(lore);

            // 如果是可重複使用的蛋，標記可重複使用
            if (isReusable) {
                container.set(reusableKey, PersistentDataType.BOOLEAN, true);
            }

            egg.setItemMeta(meta);
        }

        // 發送生物捕捉蛋給玩家
        player.getInventory().addItem(egg);
        //player.sendMessage(PREFIX + "§a你獲得了一顆 " + (isReusable ? "§b可重複使用的" : "§6") + " 生物捕捉蛋！");
    }

}
