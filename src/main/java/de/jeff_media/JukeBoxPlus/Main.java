package de.jeff_media.JukeBoxPlus;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.Block;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Main extends JavaPlugin {

    JukeboxUtils jukeboxUtils;
    HashMap<Block,JukeboxData> jukeboxes = new HashMap<>();

    public void onEnable() {

        getDataFolder().mkdir();
        new File(getDataFolder()+ File.separator+"jukeboxes").mkdir();

        jukeboxUtils = new JukeboxUtils(this);
        this.getServer().getPluginManager().registerEvents(new Listener(this),this);
    }

    public void loadJukeboxes() {
        File jukeboxesDir = new File(getDataFolder()+File.separator+"jukeboxes");
        for(File file : jukeboxesDir.listFiles()) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            UUID world = UUID.fromString(yaml.getString("world"));
            int x = yaml.getInt("x");
            int y = yaml.getInt("y");
            int z = yaml.getInt("z");
            Block block = getServer().getWorld(world).getBlockAt(x,y,z);
            JukeboxData jbData = new JukeboxData(world,x,y,z,file);
            jbData.loadRecords(file);
            jukeboxes.put(block,jbData);
        }
    }
}
