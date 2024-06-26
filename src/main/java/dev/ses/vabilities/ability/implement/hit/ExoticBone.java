package dev.ses.vabilities.ability.implement.hit;

import dev.ses.vabilities.ability.Ability;
import dev.ses.vabilities.utils.Color;
import dev.ses.vabilities.utils.CooldownUtil;
import dev.ses.vabilities.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.concurrent.TimeUnit;

public class ExoticBone extends Ability implements Listener {

    public ExoticBone() {
        super("EXOTIC-BONE", "HIT");
    }

    @Override
    public void onHitPlayer(Player damager, Player damaged){

        if (CooldownUtil.hasCooldown("DENY-BLOCK", damaged)){
            damager.sendMessage(Color.translate(Main.getInstance().getLangFile().getString("ANTI-TRAP-EFFECT.ALREADY-HAS")));
            this.setExecute(false);
            return;
        }

        CooldownUtil.setCooldown("DENY-BLOCK", damaged, TimeUnit.SECONDS.toMillis(15));
        super.onRight(damaged.getPlayer());
    }

}
