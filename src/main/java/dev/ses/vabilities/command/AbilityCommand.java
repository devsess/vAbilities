package dev.ses.vabilities.command;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import dev.ses.vabilities.manager.Ability;
import dev.ses.vabilities.utils.Color;
import dev.ses.vabilities.utils.Utils;
import dev.ses.vabilities.utils.config.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AbilityCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage(Color.translate("&cThis command is only executable in game."));
            return true;
        }

        if (!sender.hasPermission("vabilities.admin")){
            sender.sendMessage(Color.translate(Lang.NO_PERMISSIONS));
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0){
            sender.sendMessage(Color.translate("&7&m" + Strings.repeat("-", 40)));
            sender.sendMessage(Color.translate("&4&lvAbilities"));
            sender.sendMessage("");
            sender.sendMessage(Color.translate("&4* &c/ability list"));
            sender.sendMessage(Color.translate("&4* &c/ability get <ability> <amount>"));
            sender.sendMessage(Color.translate("&4* &c/ability getall"));
            sender.sendMessage("");
            sender.sendMessage(Color.translate("&7&m" + Strings.repeat("-", 40)));
            return true;
        }

        if (args[0].equalsIgnoreCase("getall")){
            for (Ability ability : Ability.getAbilitiesList()){
                player.getInventory().addItem(ability.getAbilityItem());
            }
        }

        if (args[0].equalsIgnoreCase("get")){
            if (args.length < 3){
                sender.sendMessage(Color.translate("&cUsage: /ability get <ability> <amount>"));
                return true;
            }

            String abilityName = args[1];
            int amount = Utils.getOrNull(args[2]);
            Ability ability = Ability.getAbility(abilityName);

            if (amount > 64){
                player.sendMessage(Color.translate("&cInvalid amount."));
                return true;
            }

            if (ability == null){
                player.sendMessage(Color.translate("&cInvalid name."));
                return true;
            }

            ItemStack newAbility = ability.getAbilityItem().clone();
            newAbility.setAmount(amount);
            player.getInventory().addItem(newAbility);
            player.updateInventory();
        }

        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("get")){
            List<String> abilitiesName = Lists.newArrayList();
            for(Ability ability : Ability.getAbilitiesList()){
                abilitiesName.add(ability.getName().toUpperCase());
            }
            return abilitiesName;
        }
        return null;
    }
}
