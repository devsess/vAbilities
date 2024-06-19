package dev.ses.vabilities.utils.config;

import dev.ses.vabilities.Main;

public class Lang {

    public static String NO_PERMISSIONS;

    public Lang(){
        NO_PERMISSIONS = Main.getInstance().getLangFile().getString("NO-PERMISSIONS");
    }
}
