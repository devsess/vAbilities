package dev.ses.vabilities.manager;

import com.google.common.collect.HashBasedTable;
import dev.ses.vabilities.utils.Color;
import dev.ses.vabilities.utils.CooldownUtil;
import dev.ses.vabilities.utils.Utils;
import dev.ses.vabilities.utils.config.Config;
import dev.ses.vabilities.vAbilities;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class AbilityListener implements Listener {

    private final HashBasedTable<UUID, UUID, Integer> hitCount;
    public AbilityListener(){
        this.hitCount = HashBasedTable.create();
    }

    @EventHandler
    private void onHitPlayer(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();
        Ability.getAbilitiesList().forEach(ability -> {

            if (ability == null) return;
            if (!ability.getType().equals("HIT")) return;

            if (Ability.isAbility(((Player) event.getDamager()).getItemInHand(), ability)){
                if (event.getDamager().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                    return;
                }

                if (event.getDamager().getWorld().getEnvironment().equals(World.Environment.THE_END)){
                    return;
                }

                if (CooldownUtil.hasCooldown("vGLOBAL", damager)){
                    damager.sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("COOLDOWN.GLOBAL").replace("{cooldown}", Utils.timeFormat(Config.GLOBAL_COOLDOWN_TIME))));
                    return;
                }

                if (CooldownUtil.hasCooldown(ability.getName(), (Player) event.getDamager())){
                    damager.sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("COOLDOWN.ABILITY")
                            .replace("{ability}", ability.getDisplayname())
                            .replace("{cooldown}", CooldownUtil.getCooldown(ability.getName(), damager))));
                    return;
                }

                if (CooldownUtil.hasCooldown("DENY-BLOCK", damaged)){
                    damager.sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("ANTI-TRAP-EFFECT.ALREADY-HAS")));
                    return;
                }

                hitCount.put(damager.getUniqueId(), damaged.getUniqueId(), (hitCount.contains(damager.getUniqueId(), damaged.getUniqueId()) ? hitCount.get(damager.getUniqueId(), damaged.getUniqueId())+1 : 1));
                if(hitCount.get(damager.getUniqueId(), damaged.getUniqueId()) < vAbilities.getInstance().getAbilitiesFile().getInt("ABILITIES."+ability.getName()+".HITS")){
                    damager.sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("HIT-COUNT")
                            .replace("{hits}", String.valueOf(hitCount.get(damager.getUniqueId(), damaged.getUniqueId())))
                            .replace("{max-hits}", String.valueOf(vAbilities.getInstance().getAbilitiesFile().getInt("ABILITIES."+ability.getName()+".HITS")))));
                    return;
                }

                ability.onHitPlayer(damager, damaged);
                ability.sendExecuteMessage(damager);
                ability.setCooldown(damager);

                if (damager.getItemInHand().getAmount() > 1) {
                    damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
                } else {
                    damager.setItemInHand(null);
                }
                damager.updateInventory();
            }
        });
    }


    @EventHandler
    private void onRight(PlayerInteractEvent event){
        if (event.getAction().name().startsWith("RIGHT")){
            Ability.getAbilitiesList().forEach(ability -> {

                if (ability == null) return;
                if (!ability.getType().equals("RIGHT")) return;

                if (Ability.isAbility(event.getItem(), ability)){
                    if (event.getPlayer().getWorld().getEnvironment().equals(World.Environment.NETHER)){
                        event.getPlayer().sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("ENVIROMENT.NETHER").replace("{ability}", ability.getDisplayname())));
                        return;
                    }

                    if (event.getPlayer().getWorld().getEnvironment().equals(World.Environment.THE_END)){
                        event.getPlayer().sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("ENVIROMENT.END").replace("{ability}", ability.getDisplayname())));
                        return;
                    }

                    if (CooldownUtil.hasCooldown("vGLOBAL", event.getPlayer())){
                        event.getPlayer().sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("COOLDOWN.GLOBAL")
                                .replace("{cooldown}", CooldownUtil.getCooldown("vGLOBAL", event.getPlayer()))));
                        return;
                    }

                    if (CooldownUtil.hasCooldown(ability.getName(), event.getPlayer())){
                        event.getPlayer().sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("COOLDOWN.ABILITY")
                                .replace("{ability}", ability.getDisplayname())
                                .replace("{cooldown}", CooldownUtil.getCooldown(ability.getName(), event.getPlayer()))));
                        return;
                    }

                    ability.onRight(event.getPlayer());
                    ability.sendExecuteMessage(event.getPlayer());
                    ability.setCooldown(event.getPlayer());

                    if (event.getPlayer().getItemInHand().getAmount() > 1) {
                        event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                    } else {
                        event.getPlayer().setItemInHand(null);
                    }
                    event.getPlayer().updateInventory();
                }

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
            player.sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("ANTI-TRAP-EFFECT.INTERACT")
                    .replace("{cooldown}", CooldownUtil.getCooldown("DENY-BLOCK", player))));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        final Player player = blockPlaceEvent.getPlayer();
        if (CooldownUtil.hasCooldown("DENY-BLOCK", player)) {
            blockPlaceEvent.setCancelled(true);
            player.sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("ANTI-TRAP-EFFECT.BLOCK-PLACE")
                    .replace("{cooldown}", CooldownUtil.getCooldown("DENY-BLOCK", player))));        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        final Player player = blockBreakEvent.getPlayer();
        if (CooldownUtil.hasCooldown("DENY-BLOCK", player)) {
            blockBreakEvent.setCancelled(true);
            player.sendMessage(Color.translate(vAbilities.getInstance().getLangFile().getString("ANTI-TRAP-EFFECT.BLOCK-BREAK")
                    .replace("{cooldown}", CooldownUtil.getCooldown("DENY-BLOCK", player))));        }
    }
}
