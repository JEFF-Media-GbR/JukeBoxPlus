package de.jeff_media.JukeBoxPlus;

import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    // TODO Allow some Jukeboxes with extra permission to autoloop on server start (for spawn etc)

    String spigotUserId = "%%__USER__%%";
    JukeboxUtils jukeboxUtils;
    Messages msg;
    HashMap<Block, JukeboxData> jukeboxes;
    TaskController taskController;
    SongUtils songUtils;
    Utils utils;
    Listener listener;
    JukeboxGUIListener jukeboxGuiListener;
    MessageUtils messageUtils;
    HashMap<UUID, BossBar> bossbars;
    HashMap<UUID, JukeboxGUI> openGUIs;
    String uid = "%%__USER__%%";
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
            reloadConfig();
            songUtils = new SongUtils(this);
            return;
        }

        createConfig();
        jukeboxes = new HashMap<>();
        msg = new Messages(this);
        jukeboxUtils = new JukeboxUtils(this);
        listener = new Listener(this);
        jukeboxGuiListener = new JukeboxGUIListener(this);
        openGUIs = new HashMap<>();
        this.getServer().getPluginManager().registerEvents(listener, this);
        this.getServer().getPluginManager().registerEvents(jukeboxGuiListener,this);
        getCommand("jukebox").setExecutor(new CommandReload(this));
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
        if(!getFile("discs.yml").exists()) {
            saveResource("discs.yml", false);
        }
    }

    File getFile(String name) {
        return new File(getDataFolder()+File.separator+name);
    }

    public void loadJukeboxes() {
        File jukeboxesDir = new File(getDataFolder() + File.separator + "jukeboxes");
        for (File file : jukeboxesDir.listFiles()) {
            try {
                JukeboxData jbData = new JukeboxData(file,this);
                jukeboxes.put(jbData.getBlock(), jbData);
            } catch (JukeboxData.WorldNotFoundException e) {
                e.printStackTrace();
            }
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
