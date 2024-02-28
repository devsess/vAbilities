package dev.ses.vabilities.utils.config;

import dev.ses.vabilities.vAbilities;

public class Config {

    public static int GLOBAL_COOLDOWN_TIME;


    public Config(){
        GLOBAL_COOLDOWN_TIME = vAbilities.getInstance().getConfigFile().getInt("GLOBAL-COOLDOWN.TIME");
    }
}
