package dev.ses.vabilities;

import com.google.common.base.Strings;
import dev.ses.vabilities.command.AbilityCommand;
import dev.ses.vabilities.manager.Ability;
import dev.ses.vabilities.utils.Color;
import dev.ses.vabilities.utils.ConfigCreator;
import dev.ses.vabilities.utils.config.Config;
import dev.ses.vabilities.utils.config.Lang;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
@Getter @Setter
public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    private ConfigCreator configFile, abilitiesFile, langFile;

    @Override
    public void onEnable() {
        instance = this;

        this.configFile = new ConfigCreator("config.yml");
        this.langFile = new ConfigCreator("lang.yml");
        this.abilitiesFile = new ConfigCreator("abilities.yml");

        Ability.load();
        this.getCommand("abilities").setExecutor(new AbilityCommand());
        this.getCommand("abilities").setTabCompleter(new AbilityCommand());

        new Config();
        new Lang();

        log("&4" + Strings.repeat("=", 40));
        log("&c&lvAbilities - v1.0");
        log(" ");
        log("&cAuthor: &fdevses");
        log("&cDiscord Support: &fmavzm");
        log("&4" + Strings.repeat("=", 40));
    }

    @Override
    public void onDisable() {

    }

    public void log(String text){
        Bukkit.getConsoleSender().sendMessage(Color.translate(text));
    }
}
