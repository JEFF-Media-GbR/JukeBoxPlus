package de.jeff_media.JukeBoxPlus;

import de.jeff_media.PluginUpdateChecker.PluginUpdateChecker;
import org.bstats.bukkit.Metrics;
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
    PluginUpdateChecker updateChecker;

    private static final String SPIGOT_RESOURCE_ID = "87750";
    static final int BSTATS_ID = 10139;
    private static final String UPDATECHECKER_LINK_API = "https://api.jeff-media.de/jukeboxplus/latest-version.txt";
    private static final String UPDATECHECKER_LINK_DOWNLOAD = "https://www.spigotmc.org/resources/"+SPIGOT_RESOURCE_ID;
    private static final String UPDATECHECKER_LINK_CHANGELOG = "https://www.spigotmc.org/resources/"+SPIGOT_RESOURCE_ID+"/updates";
    private static final String UPDATECHECKER_LINK_DONATE = "https://paypal.me/mfnalex";

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
            createConfig();
            ConfigUpdater.updateConfig(this);
            songUtils = new SongUtils(this);
            initUpdateChecker();
            msg = new Messages(this);
            messageUtils = new MessageUtils((this));
            return;
        }

        createConfig();
        ConfigUpdater.updateConfig(this);
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

        initUpdateChecker();
        Metrics metrics = new Metrics(this,BSTATS_ID);

        loadJukeboxes();
    }

    private void initUpdateChecker() {
        if(updateChecker == null) {
            updateChecker = new PluginUpdateChecker(this,
                    UPDATECHECKER_LINK_API,
                    UPDATECHECKER_LINK_DOWNLOAD,
                    UPDATECHECKER_LINK_CHANGELOG,
                    UPDATECHECKER_LINK_DONATE);
        } else {
            updateChecker.stop();
        }

        switch(getConfig().getString(Config.CHECK_FOR_UPDATES).toLowerCase()) {
            case "true":
                //debug(getConfig().getDouble(Config.UPDATE_CHECK_INTERVAL)+"");
                //debug(getConfig().getDouble(Config.UPDATE_CHECK_INTERVAL)*60*60+"");
                updateChecker.check((long) (getConfig().getDouble(Config.UPDATE_CHECK_INTERVAL) * 60 * 60));
                break;
            case "false":
                break;
            default:
                updateChecker.check();
        }
    }

    private void createConfig() {
        getDataFolder().getAbsoluteFile().mkdirs();
        getFile("jukeboxes").getAbsoluteFile().mkdirs();
        saveDefaultConfig();
        new Config(this);
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
