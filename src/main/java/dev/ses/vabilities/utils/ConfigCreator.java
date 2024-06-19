package dev.ses.vabilities.utils;

import dev.ses.vabilities.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConfigCreator extends YamlConfiguration {

    @Getter public final File file;

    private YamlConfiguration configuration;

    public ConfigCreator(String name) throws RuntimeException {
        this.file = new File(Main.getInstance().getDataFolder(), name);

        if(!this.file.exists()) {
            Main.getInstance().saveResource(name, false);
        }

        try {
            this.load(this.file);
        } catch(IOException | InvalidConfigurationException e) {
            Bukkit.getConsoleSender().sendMessage(Color.translate("&cError occurred while loading " + name + "."));

            Stream.of(e.getMessage().split("\n")).forEach(line -> Bukkit.getConsoleSender().sendMessage(line));
            throw new RuntimeException();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void reload() {
        try{
            this.load(this.file);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ConfigurationSection getSection(String name) {
        return super.getConfigurationSection(name);
    }

    @Override
    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    @Override
    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    @Override
    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    @Override
    public String getString(String path) {
        return Color.translate(super.getString(path, ""));
    }



    @Override
    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(Color::translate).collect(Collectors.toList());
    }

}
