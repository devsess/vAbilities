package dev.ses.vabilities.ability.implement.right;

import dev.ses.vabilities.ability.Ability;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Strength extends Ability {


    public Strength(){
        super("STRENGTH", "RIGHT");
    }

    @Override
    public void onRight(Player player) {

        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 6*20, 1));
        super.onRight(player);
    }
}
