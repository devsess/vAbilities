package dev.ses.vabilities.ability;

import com.google.common.collect.HashBasedTable;
import dev.ses.vabilities.utils.Color;
import dev.ses.vabilities.utils.CooldownUtil;
import dev.ses.vabilities.utils.Utils;
import dev.ses.vabilities.utils.config.Config;
import dev.ses.vabilities.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AbilityListener implements Listener {

    private HashBasedTable<UUID, UUID, Integer> hitCount;

    @Getter
    private static Map<UUID, UUID> lastDamager;

    public AbilityListener(){
        this.hitCount = HashBasedTable.create();
        lastDamager = new HashMap<>();
    }

    @EventHandler
    private void onHitPlayer(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();
        Ability.getAbilitiesList().forEach(ability -> {

            if (ability == null) return;
            if (!ability.getType().equals("HIT")) return;
            if (!Ability.isAbility(((Player) event.getDamager()).getItemInHand(), ability)) return;
            if (event.getDamager().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                return;
            }
            if (event.getDamager().getWorld().getEnvironment().equals(World.Environment.THE_END)){
                return;
            }
            if (CooldownUtil.hasCooldown("vGLOBAL", damager)){
                damager.sendMessage(Color.translate(Main.getInstance().getLangFile().getString("COOLDOWN.GLOBAL").replace("{cooldown}", Utils.timeFormat(Config.GLOBAL_COOLDOWN_TIME))));
                return;
            }
            if (CooldownUtil.hasCooldown(ability.getName(), (Player) event.getDamager())){
                damager.sendMessage(Color.translate(Main.getInstance().getLangFile().getString("COOLDOWN.ABILITY")
                        .replace("{ability}", ability.getDisplayname())
                        .replace("{cooldown}", CooldownUtil.getCooldown(ability.getName(), damager))));
                return;
            }

            hitCount.put(damager.getUniqueId(), damaged.getUniqueId(), (hitCount.contains(damager.getUniqueId(), damaged.getUniqueId()) ? hitCount.get(damager.getUniqueId(), damaged.getUniqueId())+1 : 1));
            if(hitCount.get(damager.getUniqueId(), damaged.getUniqueId()) < Main.getInstance().getAbilitiesFile().getInt("ABILITIES."+ability.getName()+".HITS")){
                damager.sendMessage(Color.translate(Main.getInstance().getLangFile().getString("HIT-COUNT")
                        .replace("{hits}", String.valueOf(hitCount.get(damager.getUniqueId(), damaged.getUniqueId())))
                        .replace("{max-hits}", String.valueOf(Main.getInstance().getAbilitiesFile().getInt("ABILITIES."+ability.getName()+".HITS")))));
                return;
            }
                ability.onHitPlayer(damager, damaged);
                if (ability.isExecute()){
                    ability.sendExecuteMessage(damager);
                    ability.setCooldown(damager);
                    ability.decrementItem(damager);
                }

        });
    }


    @EventHandler
    private void onRight(PlayerInteractEvent event){
        if (event.getAction().name().startsWith("RIGHT")){
            Ability.getAbilitiesList().forEach(ability -> {
                if (ability == null) return;
                if (!ability.getType().equals("RIGHT")) return;
                if (!Ability.isAbility(event.getItem(), ability)) return;

                event.setCancelled(true);
                if (event.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    event.getPlayer().sendMessage(Color.translate(Main.getInstance().getLangFile().getString("ENVIROMENT.NETHER").replace("{ability}", ability.getDisplayname())));
                    return;
                }

                if (event.getPlayer().getWorld().getEnvironment().equals(World.Environment.THE_END)){
                    event.getPlayer().sendMessage(Color.translate(Main.getInstance().getLangFile().getString("ENVIROMENT.END").replace("{ability}", ability.getDisplayname())));
                    return;
                }

                if (CooldownUtil.hasCooldown("vGLOBAL", event.getPlayer())){
                    event.getPlayer().sendMessage(Color.translate(Main.getInstance().getLangFile().getString("COOLDOWN.GLOBAL")
                            .replace("{cooldown}", CooldownUtil.getCooldown("vGLOBAL", event.getPlayer()))));
                    return;
                }

                if (CooldownUtil.hasCooldown(ability.getName(), event.getPlayer())){
                    event.getPlayer().sendMessage(Color.translate(Main.getInstance().getLangFile().getString("COOLDOWN.ABILITY")
                            .replace("{ability}", ability.getDisplayname())
                            .replace("{cooldown}", CooldownUtil.getCooldown(ability.getName(), event.getPlayer()))));
                    return;
                }
                ability.onRight(event.getPlayer());
                if (!ability.isExecute()) return;
                ability.sendExecuteMessage(event.getPlayer());
                ability.setCooldown(event.getPlayer());
                ability.decrementItem(event.getPlayer());
            });
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent playerInteractEvent) {
        final Player player = playerInteractEvent.getPlayer();
        Block block = playerInteractEvent.getClickedBlock();
        if (block == null) {
            return;
        }
        if (!block.getType().name().contains("GATE") &&
                !block.getType().name().contains("FENCE") &&
                !block.getType().name().contains("LEVER") &&
                !block.getType().name().contains("BUTTON") &&
                !block.getType().name().contains("PLATE") &&
                !block.getType().name().contains("DOOR") &&
                !block.getType().name().contains("CHEST")) {
            return;
        }
        if (CooldownUtil.hasCooldown("DENY-BLOCK", player)) {
            playerInteractEvent.setCancelled(true);
            player.sendMessage(Color.translate(Main.getInstance().getLangFile().getString("ANTI-TRAP-EFFECT.INTERACT")
                    .replace("{cooldown}", CooldownUtil.getCooldown("DENY-BLOCK", player))));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        final Player player = blockPlaceEvent.getPlayer();
        if (CooldownUtil.hasCooldown("DENY-BLOCK", player)) {
            blockPlaceEvent.setCancelled(true);
            player.sendMessage(Color.translate(Main.getInstance().getLangFile().getString("ANTI-TRAP-EFFECT.BLOCK-PLACE")
                    .replace("{cooldown}", CooldownUtil.getCooldown("DENY-BLOCK", player))));        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        final Player player = blockBreakEvent.getPlayer();
        if (CooldownUtil.hasCooldown("DENY-BLOCK", player)) {
            blockBreakEvent.setCancelled(true);
            player.sendMessage(Color.translate(Main.getInstance().getLangFile().getString("ANTI-TRAP-EFFECT.BLOCK-BREAK")
                    .replace("{cooldown}", CooldownUtil.getCooldown("DENY-BLOCK", player))));        }
    }

    //Get last player damager
    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof  Player) || !(event.getEntity() instanceof  Player)) return;

        UUID damagerUUID = event.getDamager().getUniqueId();
        UUID entityUUID = event.getEntity().getUniqueId();

        if (lastDamager.containsKey(entityUUID)){
            lastDamager.remove(entityUUID);
            return;
        }

        lastDamager.put(entityUUID, damagerUUID);
        new BukkitRunnable() {
            @Override
            public void run() {
                lastDamager.remove(entityUUID, damagerUUID);
            }
        }.runTaskLater(Main.getInstance(), 15*20L);
    }

    public static Player getLastDamager(UUID uuid){
        return Bukkit.getPlayer(lastDamager.get(uuid));
    }

    public static Player getLastDamager(Player player){
        return Bukkit.getPlayer(lastDamager.get(player.getUniqueId()));
    }
}
