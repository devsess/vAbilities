package dev.ses.vabilities.ability.implement.right;

import dev.ses.vabilities.ability.Ability;
import dev.ses.vabilities.ability.AbilityListener;
import dev.ses.vabilities.utils.Color;
import dev.ses.vabilities.Main;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NinjaStar extends Ability {

    public NinjaStar() {
        super("NINJA-STAR", "RIGHT");
    }

    @Override
    protected void onRight(Player player) {

        if (!AbilityListener.getLastDamager().containsKey(player.getUniqueId())){
            player.sendMessage(Color.translate(Main.getInstance().getLangFile().getString("NO-LAST-DAMAGER")));
            this.setExecute(false);
            return;
        }

        Player target = AbilityListener.getLastDamager(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(target.getLocation());
            }
        }.runTaskLater(Main.getInstance(), 5*20L);
        super.onRight(player);
    }
}
