package de.jeff_media.JukeBoxPlus;

import org.apache.commons.collections4.MultiValuedMap;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Main extends JavaPlugin {

    // TODO Allow some Jukeboxes with extra permission to autoloop on server start (for spawn etc)

    JukeboxUtils jukeboxUtils;
    Messages msg;
    HashMap<Block, JukeboxData> jukeboxes;
    TaskController taskController;
    SongUtils songUtils;
    Utils utils;
    Listener listener;
    GUIListener guiListener;
    MessageUtils messageUtils;
    HashMap<UUID, BossBar> bossbars;
    int configVersion = 3;

    void debug(String text) {
        if (getConfig().getBoolean("debug"))
            getLogger().warning("[DEBUG] " + text);
    }

    public void onDisable() {
        saveJukeboxes();
    }

    public void onEnable( ) {
        onEnable(false);
    }

    public void onEnable(boolean reload) {

        if(reload) {
            saveJukeboxes();
        }

        createConfig();
        jukeboxes = new HashMap<>();
        msg = new Messages(this);
        jukeboxUtils = new JukeboxUtils(this);
        listener = new Listener(this);
        guiListener = new GUIListener(this);
        this.getServer().getPluginManager().registerEvents(listener, this);
        this.getServer().getPluginManager().registerEvents(guiListener,this);
        taskController = new TaskController(this);
        songUtils = new SongUtils(this);
        utils = new Utils(this);
        messageUtils = new MessageUtils((this));
        bossbars=new HashMap<>();

        loadJukeboxes();
    }

    private void createConfig() {
        getDataFolder().getAbsoluteFile().mkdirs();
        getFile("jukeboxes").getAbsoluteFile().mkdirs();
        saveDefaultConfig();
        if(!getFile("durations.yml").exists()) {
            saveResource("durations.yml", false);
        }
    }

    File getFile(String name) {
        return new File(getDataFolder()+File.separator+name);
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
            World worldByUUID = getServer().getWorld(world);
            if(worldByUUID==null) {
                getLogger().warning("World with uuid "+world.toString()+" not found. Did you generate it? To restore jukeboxes, type /jukebox admin restore <worldname>");
                continue;
            }
            Block block = Objects.requireNonNull(getServer().getWorld(world).getBlockAt(x, y, z),"Block is null");
            JukeboxData jbData = new JukeboxData(world, x, y, z, file, this);
            jbData.loadRecords(file);
            jukeboxes.put(block, jbData);

            file.delete();
        }
    }

    private void saveJukeboxes() {
        debug("Saving Jukeboxes");
        for (Map.Entry<Block, JukeboxData> entry : jukeboxes.entrySet()) {
            debug("SAVING JB ");
            entry.getValue().save(Utils.block2file(entry.getKey(), this));
        }
    }
}
