package de.jeff_media.JukeBoxPlus;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    // TODO Allow some Jukeboxes with extra permission to autoloop on server start (for spawn etc)

    JukeboxUtils jukeboxUtils;
    Messages msg;
    HashMap<Block, JukeboxData> jukeboxes = new HashMap<>();
    TaskController taskController;
    SongUtils songUtils;
    Utils utils;
    int configVersion = 3;

    private void createFolders() {
        debug("Creating directories");
        getDataFolder().getAbsoluteFile().mkdirs();
        new File(getDataFolder() + File.separator + "jukeboxes").getAbsoluteFile().mkdirs();

        debug(getDataFolder().getAbsolutePath());
        debug(getDataFolder().getAbsoluteFile().toString());

    }

    void debug(String text) {
        if (getConfig().getBoolean("debug")) getLogger().warning("[DEBUG] " + text);
    }

    public void loadJukeboxes() {
        File jukeboxesDir = new File(getDataFolder() + File.separator + "jukeboxes");
        for (File file : jukeboxesDir.listFiles()) {
            debug("LOADING JB " + file.getName());
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            UUID world = UUID.fromString(yaml.getString("world"));
            int x = yaml.getInt("x");
            int y = yaml.getInt("y");
            int z = yaml.getInt("z");
            Block block = getServer().getWorld(world).getBlockAt(x, y, z);
            JukeboxData jbData = new JukeboxData(world, x, y, z, file, this);
            jbData.loadRecords(file);
            jukeboxes.put(block, jbData);

            file.delete();
        }
    }

    public void onDisable() {
        saveJukeboxes();
    }

    public void onEnable() {

        createFolders();
        saveResource("durations.yml",false);

        msg = new Messages(this);

        jukeboxUtils = new JukeboxUtils(this);
        this.getServer().getPluginManager().registerEvents(new Listener(this), this);

        loadJukeboxes();
        taskController = new TaskController(this);
        songUtils = new SongUtils(this);
        utils = new Utils(this);
    }

    private void saveJukeboxes() {
        debug("Saving Jukeboxes");
        for (Map.Entry<Block, JukeboxData> entry : jukeboxes.entrySet()) {
            debug("SAVING JB ");
            entry.getValue().save(Utils.block2file(entry.getKey(), this));
        }
    }
}
