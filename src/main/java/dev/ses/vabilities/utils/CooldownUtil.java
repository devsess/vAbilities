package dev.ses.vabilities.utils;

import com.google.common.collect.HashBasedTable;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class CooldownUtil {

    private HashBasedTable<String, UUID, Long> cooldown = HashBasedTable.create();

    public boolean hasCooldown(String name, Player player) {
        return cooldown.contains(name, player.getUniqueId()) && cooldown.get(name, player.getUniqueId()) > System.currentTimeMillis();
    }

    public boolean isInCooldown(String name, Player player){
        return cooldown.contains(name, player.getUniqueId());
    }

    public void setCooldown(String name, Player player, long time) {
        if (time == 0L) {
            cooldown.remove(name, player.getUniqueId());
        }
        if (time <= 0L) {
            cooldown.remove(name, player.getUniqueId());
        }
        else {
            cooldown.put(name, player.getUniqueId(), System.currentTimeMillis() + time);
        }
    }
    public void removeCooldown(String name, Player player){
        cooldown.remove(name, player.getUniqueId());
    }


    public String getCooldown(String name, Player player) {
        long cooldownLeft = cooldown.get(name, player.getUniqueId()) - System.currentTimeMillis();

        if(TimeUnit.MILLISECONDS.toSeconds(cooldownLeft) >= 60){
            return String.format("%2d minutes %2d seconds", TimeUnit.MILLISECONDS.toMinutes(cooldownLeft), TimeUnit.MILLISECONDS.toSeconds(cooldownLeft) % 60);
        }else if (TimeUnit.MILLISECONDS.toSeconds(cooldownLeft) <60){
            return String.format("%2d seconds", TimeUnit.MILLISECONDS.toSeconds(cooldownLeft));
        }else if (TimeUnit.MILLISECONDS.toSeconds(cooldownLeft) <10){
            return String.format("%d seconds", TimeUnit.MILLISECONDS.toSeconds(cooldownLeft));
        }else{
            return "";
        }
    }

    public long millis(String name, Player player){
        return cooldown.get(name, player.getUniqueId()) - System.currentTimeMillis();

    }

}
