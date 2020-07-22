package de.jeff_media.JukeBoxPlus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JukeboxData {

    Main main;

    UUID world;
    int x, y, z;
    ArrayList<Material> records = new ArrayList<>();
    boolean loop = false;
    boolean shuffle = false;
    Material record = null;
    long endTime = 0;
    Random random = new Random();

    JukeboxData(Block block, Main main) {
        this.world = block.getWorld().getUID();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.main = main;
    }

    JukeboxData(UUID world, int x, int y, int z, File file, Main main) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.main = main;
    }

    boolean add(ItemStack is) {
        if (records.contains(is.getType())) {
            main.debug("Already contains " + is.getType().name());
            return false;
        }
        records.add(is.getType());
        main.debug("Added disc " + is.getType().name());
        main.utils.updateInventoryViews("Record added");
        return true;
    }

    Block getBlock() {
        World w = Objects.requireNonNull(Bukkit.getWorld(world), "World with UUID " + world.toString() + " is not loaded, cannot get block.");
        return w.getBlockAt(x, y, z);
    }

    Jukebox getJukebox() throws BlockIsNoJukeboxException {
        if (getBlock().getType() == Material.JUKEBOX) {
            return (Jukebox) getBlock().getState();
        }
        throw new BlockIsNoJukeboxException();
    }

    void loadRecords(File file) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (String s : yaml.getStringList("records")) {
            records.add(Material.getMaterial(s));
        }
        //record = Material.getMaterial(yaml.getString("record", "nothing")); // TODO (and not here)
    }

    void randomRecord() {
        switch (records.size()) {
            case 0:
                break;
            case 1:
                record = records.get(0);
                break;
            case 2:
                record = records.get(records.indexOf(record) == 0 ? 1 : 0);
                break;
            default:
                Material oldRecord = record;
                while (record == oldRecord) {
                    record = records.get(random.nextInt(records.size()));
                }
        }
        main.debug("Shuffle chose random record "+record.name());
    }

    ArrayList<String> recordsToStringList() {
        ArrayList<String> list = new ArrayList<String>();
        for (Material record : records) {
            list.add(record.name());
        }
        return list;
    }

    void save(File file) {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("records", recordsToStringList());
        String recordName = record == null || record == Material.AIR ? "none" : record.name();
        //yaml.set("record", recordName); // TODO
        //yaml.set("loop",loop); // TODO
        //yaml.set("shuffle",shuffle); // TODO
        yaml.set("world", world.toString());
        yaml.set("x", x);
        yaml.set("y", y);
        yaml.set("z", z);
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setEndTime(int duration) {
        endTime = new Date().getTime() + (duration * 1000);
    }

    void destroy(Block block) {
        for(Material mat : records) {
            block.getWorld().dropItem(block.getLocation(),new ItemStack(mat));
        }
        main.jukeboxes.remove(block);
    }

    void startJukebox(Jukebox jb, Material r) {
        if (r == null || r == Material.AIR) {
            main.debug("Cannot start without a record");
            return;
        }
        main.debug("Starting Jukebox with " + r.name());
        int duration = main.songUtils.getDuration(r);
        //stopJukebox(jb);
        jb.setRecord(new ItemStack(r));
        jb.update();
        setEndTime(duration);
        record = r;
        main.utils.updateInventoryViews("Started Jukebox");
    }

    void stopJukebox(Jukebox jb) {
        record = null;
        //record = jb.getPlaying();
        jb.setRecord(null);
        endTime = 0;
        loop=false;
        shuffle=false;
        jb.update();
        main.utils.updateInventoryViews("Stopped Jukebox");
    }

    void toggleLoop(Main main) {
        this.loop = !loop;
        main.debug("Loop: " + loop);
        if (loop) {
            if (record == null || record == Material.AIR) {
                main.debug("Cannot loop without record");
                this.loop = false;
                return;
            }

            if (endTime == 0 || new Date().getTime() >= endTime) main.taskController.startLoop(getBlock());

        }
        main.utils.updateInventoryViews("Toggled Loop");
    }

    public void toggleShuffle(Main main) {
        this.shuffle = !shuffle;
        main.debug("Shuffle: " + shuffle);
        if (shuffle) {
            if (records.size() == 0) {
                main.debug("Cannot shuffle without records");
                this.shuffle = false;
                return;
            } else {
                if (endTime == 0 || new Date().getTime() >= endTime) {
                    try {
                        randomRecord();
                        startJukebox(getJukebox(),record); //main.taskController.startLoop(getBlock());
                    } catch (BlockIsNoJukeboxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        main.utils.updateInventoryViews("Toggled Shuffle");
    }

    private class BlockIsNoJukeboxException extends Exception {

    }

}
