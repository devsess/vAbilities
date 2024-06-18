package dev.ses.vabilities.manager.implement.right;

import dev.ses.vabilities.manager.Ability;
import dev.ses.vabilities.utils.Color;
import dev.ses.vabilities.vAbilities;
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
    }

    @Override
    protected void onRight(Player player) {
        Egg egg = player.launchProjectile(Egg.class);
        egg.setMetadata("ParalyzerLaunched", new FixedMetadataValue(vAbilities.getInstance(), true));
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
        damaged.setMetadata("Paralyzed", new FixedMetadataValue(vAbilities.getInstance(), null));
        egg.removeMetadata("ParalyzerLaunched", vAbilities.getInstance());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!damaged.hasMetadata("Paralyzed")){
                    cancel();
                    return;
                }
                damaged.removeMetadata("Paralyzed",  vAbilities.getInstance());
                damaged.sendMessage(getAbilityMessages("NOW-MOVE"));
            }
        }.runTaskLater(vAbilities.getInstance(), 4*20L);
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event){
        if (event.getPlayer().hasMetadata("Paralyzed")){
            event.setCancelled(true);
            return;
        }
    }
}
