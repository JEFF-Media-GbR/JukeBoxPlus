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
        customSongs = YamlConfiguration.loadConfiguration(new File(main.getDataFolder()+ File.separator+"durations.yml"));
    }

    HashMap<Material, Integer> songs = new HashMap<>();
    YamlConfiguration customSongs;

    void addSong(Material mat, int minutes, int seconds) {
        songs.put(mat, minutes * 60 + seconds);
    }

    void addSongs() {

        addSong(Material.MUSIC_DISC_13, 2, 58);

        addSong(Material.MUSIC_DISC_CAT, 3, 5);

        addSong(Material.MUSIC_DISC_BLOCKS, 5, 45);

        addSong(Material.MUSIC_DISC_CHIRP, 3, 5);

        addSong(Material.MUSIC_DISC_FAR, 2, 54);

        addSong(Material.MUSIC_DISC_MALL, 3, 17);

        addSong(Material.MUSIC_DISC_MELLOHI, 1, 36);

        addSong(Material.MUSIC_DISC_STAL, 2, 30);

        addSong(Material.MUSIC_DISC_STRAD, 3, 8);

        addSong(Material.MUSIC_DISC_WARD, 4, 11);

        addSong(Material.MUSIC_DISC_11, 1, 11);

        addSong(Material.MUSIC_DISC_WAIT, 3, 58);

        Material pigstep = Material.getMaterial("MUSIC_DISC_PIGSTEP");
        if (pigstep != null) {
            addSong(pigstep, 2, 24);
        }

    }

    Integer getDuration(Material mat) {

        String songName = mat.name().replaceFirst("MUSIC_DISC_","").toLowerCase();

        if(customSongs.getInt(songName,0)!=0) {
            main.debug("Custom duration for "+songName+" is "+customSongs.getInt(songName));
            return customSongs.getInt(songName);
        }

        if (songs.containsKey(mat)) {
            main.debug("Default duration for "+songName+" is "+songs.get(mat));
            return songs.get(mat);
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
