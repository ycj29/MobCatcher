package com.ycjhub.mobcatcher;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class listener implements Listener {
    private final MobCatcher plugin; // Reference to main plugin class
    private String s = ChatColor.of("#3ea4c9") + ChatColor.translateAlternateColorCodes('&' ,"&l捕捉蛋系統&r &8» &r" );

    public listener(MobCatcher plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent e) {
        if (!(e.getEntity() instanceof Projectile)) return;

        Projectile projectile = (Projectile) e.getEntity();

        if (!(projectile.getShooter() instanceof Player)) return;
        Player player = (Player) projectile.getShooter();

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "cjcatcher");

            if (container.has(key, PersistentDataType.BOOLEAN)) {
                boolean isCustom = container.get(key, PersistentDataType.BOOLEAN);

                if (!player.hasPermission("mobcatcher.use")) {
                    player.sendMessage("§cYou do not have permission to throw this!");
                    e.setCancelled(true);
                    return;
                }

                // Store the key inside the projectile
                PersistentDataContainer projectileData = projectile.getPersistentDataContainer();
                projectileData.set(key, PersistentDataType.BOOLEAN, isCustom);

                player.sendMessage("§aYou threw a custom Mob Catcher Egg!");

                // Give a new egg if the player has the "mobcatcher.reusable" permission
                if (player.hasPermission("mobcatcher.reusable")) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> giveMobCatcherEgg(player), 1L);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Projectile)) return;
        Projectile projectile = (Projectile) e.getEntity();

        PersistentDataContainer data = projectile.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "cjcatcher");

        if (data.has(key, PersistentDataType.BOOLEAN)) {
            Entity hitEntity = e.getHitEntity();
            if (hitEntity == null) return;

            // Check if entity is one of the allowed types
            if (hitEntity instanceof Villager || hitEntity instanceof Sheep || hitEntity instanceof Wolf ||
                    hitEntity instanceof Pig || hitEntity instanceof Cow || hitEntity instanceof Cat ||
                    hitEntity instanceof Horse || hitEntity instanceof Donkey || hitEntity instanceof Mule) {

                // Get the entity type and drop the corresponding spawn egg
                EntityType entityType = hitEntity.getType();
                Material spawnEgg = getSpawnEggMaterial(entityType);

                if (spawnEgg != null) {
                    hitEntity.getWorld().dropItemNaturally(hitEntity.getLocation(), new ItemStack(spawnEgg));
                    hitEntity.remove(); // Remove the original entity

                    Bukkit.broadcastMessage("§bA " + entityType.name() + " was captured and turned into a spawn egg!");
                }
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
            default: return null;
        }
    }
}
