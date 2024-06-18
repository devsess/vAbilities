package dev.ses.vabilities.utils.config;

import dev.ses.vabilities.vAbilities;

public class Config {

    public static int GLOBAL_COOLDOWN_TIME;
    public static boolean HAS_LUNAR_CLIENT;


    public Config(){
        GLOBAL_COOLDOWN_TIME = vAbilities.getInstance().getConfigFile().getInt("GLOBAL-COOLDOWN.TIME");
        HAS_LUNAR_CLIENT = vAbilities.getInstance().getConfigFile().getBoolean("LUNAR-CLIENT");
    }
}
