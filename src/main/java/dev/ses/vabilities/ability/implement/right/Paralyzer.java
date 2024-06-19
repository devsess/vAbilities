package dev.ses.vabilities.ability.implement.right;

import dev.ses.vabilities.ability.Ability;
import dev.ses.vabilities.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class Paralyzer extends Ability implements Listener {

    public Paralyzer() {
        super("PARALYZER", "RIGHT");
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    protected void onRight(Player player) {
        Egg egg = player.launchProjectile(Egg.class);
        egg.setMetadata("ParalyzerLaunched", new FixedMetadataValue(Main.getInstance(), true));
        super.onRight(player);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Egg) || !(event.getEntity() instanceof Player)) return;

        Egg egg = (Egg) event.getDamager();
        Player damaged = (Player) event.getEntity();

        if (!egg.hasMetadata("ParalyzerLaunched")){
            return;
        }
        damaged.setMetadata("Paralyzed", new FixedMetadataValue(Main.getInstance(), null));
        egg.removeMetadata("ParalyzerLaunched", Main.getInstance());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!damaged.hasMetadata("Paralyzed")){
                    cancel();
                    return;
                }
                damaged.removeMetadata("Paralyzed",  Main.getInstance());
                damaged.sendMessage(getAbilityMessages("NOW-MOVE"));
            }
        }.runTaskLater(Main.getInstance(), 4*20L);
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event){
        if (event.getPlayer().hasMetadata("Paralyzed")){
            event.setCancelled(true);
            return;
        }
    }
}
