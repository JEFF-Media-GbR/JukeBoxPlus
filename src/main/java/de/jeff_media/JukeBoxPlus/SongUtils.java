package de.jeff_media.JukeBoxPlus;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class SongUtils {

    Main main;

    SongUtils(Main main)  {
        this.main=main;
        addSongs();
        customSongs = YamlConfiguration.loadConfiguration(new File(main.getDataFolder()+ File.separator+ "discs.yml"));
    }

    HashMap<Material, Integer> defaultDurations = new HashMap<>();
    HashMap<Material, String> defaultNames = new HashMap<>();
    YamlConfiguration customSongs;

    void addSong(Material mat, String name, int minutes, int seconds) {
        defaultDurations.put(mat, minutes * 60 + seconds);
        defaultNames.put(mat, name);
    }

    void addSongs() {

        addSong(Material.MUSIC_DISC_13, "13",2, 58);

        addSong(Material.MUSIC_DISC_CAT, "Cat",3, 5);

        addSong(Material.MUSIC_DISC_BLOCKS, "Blocks",5, 45);

        addSong(Material.MUSIC_DISC_CHIRP, "Chirp",3, 5);

        addSong(Material.MUSIC_DISC_FAR, "Far",2, 54);

        addSong(Material.MUSIC_DISC_MALL, "Mall",3, 17);

        addSong(Material.MUSIC_DISC_MELLOHI, "Mellohi",1, 36);

        addSong(Material.MUSIC_DISC_STAL, "Stal",2, 30);

        addSong(Material.MUSIC_DISC_STRAD, "Strad",3, 8);

        addSong(Material.MUSIC_DISC_WARD, "Ward",4, 11);

        addSong(Material.MUSIC_DISC_11, "11",1, 11);

        addSong(Material.MUSIC_DISC_WAIT, "Wait",3, 58);

        Material pigstep = Material.getMaterial("MUSIC_DISC_PIGSTEP");
        if (pigstep != null) {
            addSong(pigstep, "Pigstep",2, 24);
        }

    }

    String getName(Material mat) {
        String songName = mat.name().replaceFirst("MUSIC_DISC_","").toLowerCase();

        if(customSongs.getString(songName+".name")!=null) {
            main.debug("Custom name for "+songName+" is "+customSongs.getString(songName+".name"));
            return customSongs.getString(songName+".name");
        }

        if (defaultDurations.containsKey(mat)) {
            main.debug("Default name for "+songName+" is "+ defaultNames.get(mat));
            return defaultNames.get(mat);
        }
        main.debug("Could not find name for "+songName);
        return "<UnknownSong>";
    }

    Integer getDuration(Material mat) {

        String songName = mat.name().replaceFirst("MUSIC_DISC_","").toLowerCase();

        if(customSongs.getInt(songName+".duration",0)!=0) {
            main.debug("Custom duration for "+songName+" is "+customSongs.getInt(songName+".duration"));
            return customSongs.getInt(songName+".duration");
        }

        if (defaultDurations.containsKey(mat)) {
            main.debug("Default duration for "+songName+" is "+ defaultDurations.get(mat));
            return defaultDurations.get(mat);
        }
        main.debug("Could not find duration for "+songName);
        return -1;
    }

    static Sound getSound(Material r) {
        if(r==null) {
            //System.out.println("SOUT: MATERIAL R IS NULL");
            return null;
        }
        //System.out.println("SOUT: GETTING SONG FOR MATERIAL " + r.name());
        if(!r.name().startsWith("MUSIC_DISC_")) {
            //System.out.println("SOUT: THIS IS NOT A MUSIC DISC");
            return null;
        }
        return Sound.valueOf(r.name());
    }

}
