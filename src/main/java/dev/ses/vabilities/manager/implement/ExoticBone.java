package dev.ses.vabilities.manager.implement;

import dev.ses.vabilities.manager.Ability;
import dev.ses.vabilities.utils.CooldownUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.concurrent.TimeUnit;

public class ExoticBone extends Ability implements Listener {

    public ExoticBone() {
        super("EXOTIC-BONE", "HIT");
    }

    @Override
    public void onHitPlayer(Player damager, Player damaged){
        CooldownUtil.setCooldown("DENY-BLOCK", damaged, TimeUnit.SECONDS.toMillis(15));
    }

}
