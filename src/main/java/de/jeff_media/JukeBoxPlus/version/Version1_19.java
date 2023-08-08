package de.jeff_media.JukeBoxPlus.version;

public class Version1_19 {

    private static Boolean is1_19 = null;

    public static boolean is1_19() {
        if(is1_19 == null) {
            boolean result = false;
            try {
                Class.forName("org.bukkit.entity.Allay");
                result = true;
            } catch (Throwable ignored) {

            }
            is1_19 = result;
        }
        return is1_19;
    }

}
