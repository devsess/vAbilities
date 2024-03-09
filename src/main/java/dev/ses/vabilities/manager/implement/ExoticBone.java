package dev.ses.vabilities.manager.implement;

import dev.ses.vabilities.manager.Ability;
import dev.ses.vabilities.utils.Color;
import dev.ses.vabilities.utils.CooldownUtil;
import dev.ses.vabilities.vAbilities;
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

        if (CooldownUtil.hasCooldown("DENY-BLOCK", damaged)){
            damager.sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("ANTI-TRAP-EFFECT.ALREADY-HAS")));
            this.setExecute(false);
            return;
        }

        CooldownUtil.setCooldown("DENY-BLOCK", damaged, TimeUnit.SECONDS.toMillis(15));
        super.onRight(damaged.getPlayer());
    }

}
