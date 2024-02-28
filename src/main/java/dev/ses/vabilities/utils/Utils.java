package dev.ses.vabilities.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public static String timeFormat(int time) {
        int horas = time / 3600;
        int minutos = (time % 3600) / 60;
        int segundos = time % 60;

        String format = "";
        if (horas > 0) {
            format += horas + " hours ";
        }
        if (minutos > 0) {
            format += minutos + " minutes ";
        }

        format += segundos + " seconds";
        return format;
    }

    public Integer getOrNull(String value){
        try{
            return Integer.valueOf(value);
        }catch(NumberFormatException e){
            return 1;
        }
    }
}
