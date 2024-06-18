package dev.ses.vabilities.manager.implement.right;

import dev.ses.vabilities.manager.Ability;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Resistance extends Ability {

    public Resistance(){
        super("RESISTANCE", "RIGHT");
    }

    @Override
    public void onRight(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6*20, 2));
        super.onRight(player);
    }

}
