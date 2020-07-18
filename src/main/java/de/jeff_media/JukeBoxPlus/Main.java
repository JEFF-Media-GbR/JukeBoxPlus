package de.jeff_media.JukeBoxPlus;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.Block;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    JukeboxUtils jukeboxUtils;
    Messages msg;
    HashMap<Block,JukeboxData> jukeboxes = new HashMap<>();

    public void onEnable() {

        System.out.println("==========================================================================================");

        createFolders();

        msg = new Messages(this);

        jukeboxUtils = new JukeboxUtils(this);
        this.getServer().getPluginManager().registerEvents(new Listener(this),this);

        loadJukeboxes();
    }

    public void onDisable() {
        saveJukeboxes();
    }

    private void saveJukeboxes() {
        getLogger().info("Saving Jukeboxes");
        for(Map.Entry<Block,JukeboxData> entry : jukeboxes.entrySet()) {
            System.out.println("SAVING JB ");
            entry.getValue().save(Utils.block2file(entry.getKey(),this));
        }
    }

    private void createFolders() {
        getLogger().info("Creating directories");
        getDataFolder().getAbsoluteFile().mkdirs();
        new File(getDataFolder()+ File.separator+"jukeboxes").getAbsoluteFile().mkdirs();

        System.out.println(getDataFolder().getAbsolutePath());
        System.out.println(getDataFolder().getAbsoluteFile().toString());
        if(!getDataFolder().exists()) {
            System.out.println("SEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE, I TOLD YOU!");
        }
    }

    public void loadJukeboxes() {
        File jukeboxesDir = new File(getDataFolder()+File.separator+"jukeboxes");
        for(File file : jukeboxesDir.listFiles()) {
            System.out.println("LOADING JB "+file.getName());
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
