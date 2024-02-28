package dev.ses.vabilities.utils.config;

import dev.ses.vabilities.vAbilities;

public class Lang {

    public static String NO_PERMISSIONS;

    public Lang(){
        NO_PERMISSIONS = vAbilities.getInstance().getLangFile().getString("NO-PERMISSIONS");
    }
}
