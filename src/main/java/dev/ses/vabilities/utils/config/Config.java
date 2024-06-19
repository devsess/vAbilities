package dev.ses.vabilities.utils.config;

import dev.ses.vabilities.Main;

public class Config {

    public static int GLOBAL_COOLDOWN_TIME;
    public static boolean HAS_LUNAR_CLIENT;


    public Config(){
        GLOBAL_COOLDOWN_TIME = Main.getInstance().getConfigFile().getInt("GLOBAL-COOLDOWN.TIME");
        HAS_LUNAR_CLIENT = Main.getInstance().getConfigFile().getBoolean("LUNAR-CLIENT");
    }
}
