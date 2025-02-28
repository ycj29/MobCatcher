package com.ycjhub.mobcatcher;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MobCatcherListener implements Listener {
    private final MobCatcher plugin;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>(); // 冷卻時間紀錄
    private static final String PREFIX = ChatColor.of("#3ea4c9") + ChatColor.translateAlternateColorCodes('&', "&l捕捉蛋系統&r &8»");

    // 設定允許使用生物捕捉蛋的世界
    private static final List<String> ALLOWED_WORLDS = Arrays.asList("survival", "survival_nether", "survival_the_end");

    public MobCatcherListener(MobCatcher plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Projectile)) return;

        Projectile projectile = (Projectile) e.getEntity();

        if (!(projectile.getShooter() instanceof Player)) return;
        Player player = (Player) projectile.getShooter();
        World world = player.getWorld();

        // 檢查世界是否允許使用生物捕捉蛋
        if (!ALLOWED_WORLDS.contains(world.getName())) {
            player.sendMessage(PREFIX + "§c你無法在這個世界使用生物捕捉蛋！");
            e.setCancelled(true);
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "cjcatcher");
            NamespacedKey reusableKey = new NamespacedKey(plugin, "reusable");

            if (container.has(key, PersistentDataType.BOOLEAN)) {
                boolean isCustom = container.get(key, PersistentDataType.BOOLEAN);
                boolean isReusable = container.has(reusableKey, PersistentDataType.BOOLEAN) &&
                        container.get(reusableKey, PersistentDataType.BOOLEAN);

                // 如果是可重複使用的蛋，檢查冷卻時間
                if (isReusable) {
                    UUID playerId = player.getUniqueId();
                    long currentTime = System.currentTimeMillis();
                    if (cooldowns.containsKey(playerId)) {
                        long lastUsed = cooldowns.get(playerId);
                        if (currentTime - lastUsed < 5000) { // 5 秒冷卻
                            player.sendMessage(PREFIX + "§c請稍等 5 秒再使用可重複使用的生物捕捉蛋！");
                            e.setCancelled(true);
                            return;
                        }
                    }
                    cooldowns.put(playerId, currentTime);
                }

                // 如果是可重複使用的蛋，檢查玩家是否有權限
                if (isReusable && !player.hasPermission("mobcatcher.reusable")) {
                    player.sendMessage(PREFIX + "§c你沒有權限使用可重複使用的生物捕捉蛋！");
                    e.setCancelled(true);
                    return;
                }

                // 將生物捕捉蛋的標記存入投射物
                PersistentDataContainer projectileData = projectile.getPersistentDataContainer();
                projectileData.set(key, PersistentDataType.BOOLEAN, isCustom);
                if (isReusable) {
                    projectileData.set(reusableKey, PersistentDataType.BOOLEAN, true);
                }

                player.sendMessage(PREFIX + "§a你投出了生物捕捉蛋！");

                // 如果是可重複使用的蛋，投擲後補發一顆蛋
                if (isReusable) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.giveMobCatcherEgg(player, true), 1L);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Projectile)) return;
        Projectile projectile = (Projectile) e.getEntity();
        World world = projectile.getWorld();

        // 檢查世界是否允許捕捉
        if (!ALLOWED_WORLDS.contains(world.getName())) {
            return;
        }

        PersistentDataContainer data = projectile.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "cjcatcher");

        if (data.has(key, PersistentDataType.BOOLEAN)) {
            Entity hitEntity = e.getHitEntity();
            if (hitEntity == null) return;

            // 如果生物有名字，則不轉換為蛋
            if (hitEntity.getCustomName() != null && !hitEntity.getCustomName().isEmpty()) {
                return;
            }

            // 檢查是否為允許轉換的生物
            Bukkit.getServer().getLogger().info("DEBUG:" + hitEntity.getName());
            EntityType entityType = hitEntity.getType();
            Material spawnEgg = getSpawnEggMaterial(entityType);

            if (spawnEgg != null) {
                hitEntity.getWorld().dropItemNaturally(hitEntity.getLocation(), new ItemStack(spawnEgg));
                hitEntity.remove(); // 移除原生物
                //Bukkit.broadcastMessage(PREFIX + "§b" + entityType.name() + " 被捕獲，並轉換為生物蛋！");
            }
        }
    }

    private Material getSpawnEggMaterial(EntityType entityType) {
        switch (entityType) {
            case VILLAGER: return Material.VILLAGER_SPAWN_EGG;
            case SHEEP: return Material.SHEEP_SPAWN_EGG;
            case WOLF: return Material.WOLF_SPAWN_EGG;
            case PIG: return Material.PIG_SPAWN_EGG;
            case COW: return Material.COW_SPAWN_EGG;
            case CAT: return Material.CAT_SPAWN_EGG;
            case HORSE: return Material.HORSE_SPAWN_EGG;
            case DONKEY: return Material.DONKEY_SPAWN_EGG;
            case MULE: return Material.MULE_SPAWN_EGG;
            case FROG: return Material.FROG_SPAWN_EGG;
            case BAT: return Material.BAT_SPAWN_EGG;
            case CAMEL: return Material.CAMEL_SPAWN_EGG;
            case SNIFFER: return Material.SNIFFER_SPAWN_EGG;
            case STRIDER: return Material.STRIDER_SPAWN_EGG;
            case WANDERING_TRADER: return Material.WANDERING_TRADER_SPAWN_EGG;
            case GOAT: return Material.GOAT_SPAWN_EGG;
            case OCELOT: return Material.OCELOT_SPAWN_EGG;
            case SNOW_GOLEM: return Material.SNOW_GOLEM_SPAWN_EGG;
            case BEE: return Material.BEE_SPAWN_EGG;
            case FOX: return Material.FOX_SPAWN_EGG;
            case LLAMA: return Material.LLAMA_SPAWN_EGG;
            case PANDA: return Material.PANDA_SPAWN_EGG;
            case POLAR_BEAR: return Material.POLAR_BEAR_SPAWN_EGG;
            case ALLAY: return Material.ALLAY_SPAWN_EGG;
            default: return null;
        }
    }




}
