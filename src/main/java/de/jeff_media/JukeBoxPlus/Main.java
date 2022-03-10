package de.jeff_media.JukeBoxPlus;

import co.aikar.commands.PaperCommandManager;
import com.allatori.annotations.DoNotRename;
import de.jeff_media.JukeBoxPlus.commands.DebugCommand;
import de.jeff_media.JukeBoxPlus.listeners.JoinListener;
import de.jeff_media.daddy.CallHome;
import de.jeff_media.daddy.Stepsister;
import de.jeff_media.updatechecker.UpdateChecker;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main instance;

    // TODO Allow some Jukeboxes with extra permission to autoloop on server start (for spawn etc)

    String spigotUserId = "%%__USER__%%";
    JukeboxUtils jukeboxUtils;
    Messages msg;
    @Getter HashMap<Block, JukeboxData> jukeboxes;
    TaskController taskController;
    SongUtils songUtils;
    Utils utils;
    Listener listener;
    JukeboxGUIListener jukeboxGuiListener;
    MessageUtils messageUtils;
    HashMap<UUID, BossBar> bossbars;
    HashMap<UUID, JukeboxGUI> openGUIs;
    String uid = "%%__USER__%%";
    UpdateChecker updateChecker;

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

    @DoNotRename
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        saveJukeboxes();
    }

    @Override
    public void onEnable( ) {
        onEnable(false);
    }

    public void onEnable(boolean reload) {

        Stepsister.init(this);
        Stepsister.createVerificationFile();

        instance = this;
        CustomSong.init();

        /*
        Only on Reload
         */
        if(reload) {
            saveJukeboxes();
            reloadConfig();
            createConfig();
            ConfigUpdater.updateConfig(this);
            songUtils = new SongUtils(this);
            initUpdateChecker();
            msg = new Messages(this);
            messageUtils = new MessageUtils((this));
            CustomSong.init();
            return;
        }
        /*
        Only when NOT Reloading
         */
        else {
            // TODO: Check whether this is working async
            /*new BukkitRunnable() {
                @Override
                public void run() {
                    saveJukeboxes();
                }
            }.runTaskTimerAsynchronously(this, 10*60*20,10*60*20);*/
        }

        new ParticleManager();

        reloadConfig();
        createConfig();
        initUpdateChecker();
        ConfigUpdater.updateConfig(this);
        jukeboxes = new HashMap<>();
        msg = new Messages(this);
        jukeboxUtils = new JukeboxUtils(this);
        listener = new Listener(this);
        jukeboxGuiListener = new JukeboxGUIListener(this);
        openGUIs = new HashMap<>();
        this.getServer().getPluginManager().registerEvents(listener, this);
        this.getServer().getPluginManager().registerEvents(jukeboxGuiListener,this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        taskController = new TaskController(this);
        songUtils = new SongUtils(this);
        utils = new Utils(this);
        messageUtils = new MessageUtils((this));
        bossbars=new HashMap<>();

        initUpdateChecker();
        try {
            Metrics metrics = new Metrics(this, BSTATS_ID);
        } catch (Throwable ignored) {

        }
        CallHome.callHome(this);

        loadJukeboxes(null);

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new DebugCommand());
        CustomSong.init();
    }

    private void initUpdateChecker() {
        if(updateChecker == null) {
            updateChecker = UpdateChecker.init(this,
                    UPDATECHECKER_LINK_API)
                    .setDownloadLink(UPDATECHECKER_LINK_DOWNLOAD)
                    .setChangelogLink(UPDATECHECKER_LINK_CHANGELOG)
                    .setDonationLink(UPDATECHECKER_LINK_DONATE)
                    .suppressUpToDateMessage(true);
        } else {
            updateChecker.stop();
        }

        switch(getConfig().getString(Config.CHECK_FOR_UPDATES).toLowerCase()) {
            case "true":
                //debug(getConfig().getDouble(Config.UPDATE_CHECK_INTERVAL)+"");
                //debug(getConfig().getDouble(Config.UPDATE_CHECK_INTERVAL)*60*60+"");
                updateChecker.checkEveryXHours(getConfig().getDouble(Config.UPDATE_CHECK_INTERVAL)).checkNow();
                break;
            case "false":
                break;
            default:
                updateChecker.checkNow();
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
        if(!getFile("custom-discs.yml").exists()) {
            saveResource("custom-discs.yml", false);
        }
    }

    File getFile(String name) {
        return new File(getDataFolder()+File.separator+name);
    }

    public void loadJukeboxes(World world) {
        //System.out.println("Loading Jukeboxes in World " + world);
        File jukeboxesDir = new File(getDataFolder() + File.separator + "jukeboxes");
        for (File file : jukeboxesDir.listFiles()) {
            Bukkit.getScheduler().runTask(this, () -> {
                        try {
                            JukeboxData jbData = new JukeboxData(file, this);
                            //System.out.println("  Loading JB - Saved UUID: " + jbData.getWorld());
                            if (world == null || jbData.getWorld().equals(world.getUID())) {
                                //System.out.println("Loaded Jukebox at " + jbData.getBlock());
                                jukeboxes.put(jbData.getBlock(), jbData);
                            }
                        } catch (JukeboxData.WorldNotFoundException e) {
                            //e.printStackTrace();
                        }
                    });
            //file.delete();
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
