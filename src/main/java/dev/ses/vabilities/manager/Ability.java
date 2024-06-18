package dev.ses.vabilities.manager;

import dev.ses.vabilities.manager.implement.hit.ExoticBone;
import dev.ses.vabilities.manager.implement.right.NinjaStar;
import dev.ses.vabilities.manager.implement.right.Paralyzer;
import dev.ses.vabilities.manager.implement.right.Resistance;
import dev.ses.vabilities.manager.implement.right.Strength;
import dev.ses.vabilities.utils.Color;
import dev.ses.vabilities.utils.CooldownUtil;
import dev.ses.vabilities.utils.Utils;
import dev.ses.vabilities.utils.item.ItemBuilder;
import dev.ses.vabilities.vAbilities;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public abstract class Ability {

    private String name, displayname, material, type;
    private int data, time;
    private ItemStack abilityItem;
    private List<String> lore;
    private boolean execute;
    private static List<Ability> abilities;

    public Ability(String name, String type) {
        this.name = name;
        this.displayname = vAbilities.getInstance().getAbilitiesFile().getString("ABILITIES." + getName()+".DISPLAYNAME");
        this.material = vAbilities.getInstance().getAbilitiesFile().getString("ABILITIES." + getName()+".MATERIAL");
        this.data = vAbilities.getInstance().getAbilitiesFile().getInt("ABILITIES." + getName()+".DATA");
        this.lore = vAbilities.getInstance().getAbilitiesFile().getStringList("ABILITIES." + getName()+".LORE");
        this.time = vAbilities.getInstance().getAbilitiesFile().getInt("ABILITIES." + getName()+".COOLDOWN");
        this.type = type;
        this.abilityItem = new ItemBuilder(Material.getMaterial(material)).name(displayname).lore(lore).build();

    }

    public static void load() {
        abilities = new ArrayList<>();
        abilities.add(new Strength());
        abilities.add(new Resistance());
        abilities.add(new ExoticBone());
        abilities.add(new NinjaStar());
        abilities.add(new Paralyzer());

        Bukkit.getPluginManager().registerEvents(new AbilityListener(), vAbilities.getInstance());
    }

    public static List<Ability> getAbilitiesList() {
        return abilities;
    }

    public static Ability getAbility(String name) {
        return abilities.stream().filter(abilityName -> name.equalsIgnoreCase(abilityName.getName())).findFirst().orElse(null);
    }

    public void sendExecuteMessage(Player player){
        for (String strings : this.executeMessage()){
            player.sendMessage(Color.translate(strings.replace("{player}", player.getName()).replace("{ability}", getDisplayname()).replace("{time}", Utils.timeFormat(getTime()))));
        }
    }

    public void decrementItem(Player player){
        if (player.getItemInHand().getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
        player.updateInventory();
    }

    public void setCooldown(Player player){
        CooldownUtil.setCooldown(getName(), player, TimeUnit.SECONDS.toMillis(getTime()));
    }

    public List<String> executeMessage(){
        return vAbilities.getInstance().getLangFile().getStringList("ABILITIES-LANG."+getName() + ".EXECUTE");
    }

    public String getAbilityMessages(String path){
        return Color.translate(vAbilities.getInstance().getLangFile().getString("ABILITIES-LANG."+getName() + "." +path));
    }

    public static boolean isAbility(ItemStack itemEvent, Ability ability){
        return (itemEvent != null && itemEvent.hasItemMeta() &&
                itemEvent.getItemMeta().hasDisplayName() &&
                itemEvent.getItemMeta().getDisplayName().equals(ability.getAbilityItem().getItemMeta().getDisplayName()) &&
                itemEvent.getItemMeta().hasLore() &&
                itemEvent.getItemMeta().getLore().equals(ability.getAbilityItem().getItemMeta().getLore()));
    }

    protected void onRight(Player player) {
        setExecute(true);
    }

    protected void onHitPlayer(Player damager, Player damaged) {setExecute(true);}
}
