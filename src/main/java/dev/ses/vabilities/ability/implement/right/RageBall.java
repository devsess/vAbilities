package dev.ses.vabilities.ability.implement.right;

import com.google.common.collect.Lists;
import dev.ses.vabilities.ability.Ability;
import dev.ses.vabilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RageBall extends Ability implements Listener {

    private final Map<UUID, Projectile> rageballMap;

    public RageBall() {
        super("RAGE-BALL", "RIGHT");
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        this.rageballMap = new HashMap<>();
    }

    public List<PotionEffect> getNegativeEffects(){
        return Lists.newArrayList(
                new PotionEffect(PotionEffectType.BLINDNESS, 5*20, 2),
                new PotionEffect(PotionEffectType.POISON, 5*20, 2),
                new PotionEffect(PotionEffectType.CONFUSION, 5*20, 2));
    }

    public List<PotionEffect> getPositiveEffects(){
        return Lists.newArrayList(
                new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*20, 1),
                new PotionEffect(PotionEffectType.REGENERATION, 5*20, 2),
                new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5*20, 2),
                new PotionEffect(PotionEffectType.SPEED, 5*20, 2));
    }

    @Override
    protected void onRight(Player player) {
        Egg egg = player.launchProjectile(Egg.class);
        rageballMap.put(player.getUniqueId(), egg);
        super.onRight(player);
    }

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent event){
        Player player = (Player) event.getEntity().getShooter();
        Projectile projectile = event.getEntity();

        if (!rageballMap.containsKey(player.getUniqueId())) return;
        if (projectile != rageballMap.get(player.getUniqueId())) return;

        getPositiveEffects().forEach(player::addPotionEffect);
        rageballMap.remove(player.getUniqueId());
        projectile.getWorld().createExplosion(projectile.getLocation(), 0.0f, false);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(projectile.getLocation(), "EXPLODE", 0f, 0f);
        }

        for (Entity nearby : player.getNearbyEntities(15, 15, 15)) {
            if (!(nearby instanceof Player)) continue;
            Player playerNearby = (Player) nearby;
            if (playerNearby == player) continue;
            getNegativeEffects().forEach(playerNearby::addPotionEffect);
        }
    }


}
