package de.jeff_media.JukeBoxPlus;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class JukeboxData {

    Main main;
    static int defaultRadius = 4;
    int radius = defaultRadius; // * 16
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

    boolean addRecord(ItemStack is, Player p) {
        if (records.contains(is.getType())) {
            main.debug("Already contains " + is.getType().name());
            main.messageUtils.send("Jukebox already contains "+is.getType().name(),  false,p,true);
            return false;
        }
        records.add(is.getType());
        main.debug("Added disc " + is.getType().name());
        main.messageUtils.send("Added "+is.getType().name()+" to Jukebox",true,p,true);
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

    boolean radiusPlus() {
        radius++;
        if(radius>16) {
            radius=16;
            return false;
        }
        return true;
    }
    boolean radiusMinus() {
        radius--;
        if(radius<1) {
            radius=1;
            return false;
        }
        return true;
    }

    void nextRecord() {
        if(record==null && records.size()==0) {
            main.debug("This jukebox is empty");
            return;
        }
        if(record==null && records.size()>0) {
            record = records.get(0);
            return;
        }
        int current = records.indexOf(record);
        current++;
        if(current>=records.size()) current = 0;
        record = records.get(current);
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
        yaml.set("radius",radius);
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

    void startJukebox(Jukebox jb, @Nullable Player p) {
        startJukebox(jb,record,p);
    }

    void startJukebox(Jukebox jb, Material r,@Nullable Player p) {
        if (r == null || r == Material.AIR) {
            main.debug("Cannot start without a record");
            if(p != null) {
                main.messageUtils.send("Choose a record first.",false,p,true);
            }
            return;
        }
        main.debug("Starting Jukebox with " + r.name());
        int duration = main.songUtils.getDuration(r);
        //stopJukebox(jb);
        //jb.setRecord(new ItemStack(r));
        //jb.update();
        /*getBlock().getWorld().playSound(
                getBlock().getLocation(),
                Objects.requireNonNull(SongUtils.getSound(r),"Sound is null"),
                SoundCategory.BLOCKS,radius,1);*/
        Collection<Entity> nearby = Utils.getNearbyPlayers(jb.getBlock(),radius);
        for(Entity entity : nearby) {
            Player pn = (Player) entity;
            if(record!=null) {
                pn.stopSound(SongUtils.getSound(record), SoundCategory.BLOCKS);
            }
            pn.playSound(getBlock().getLocation(),SongUtils.getSound(r),SoundCategory.BLOCKS,radius,1);
        }
        setEndTime(duration);
        record = r;
        main.utils.updateInventoryViews("Started Jukebox");
        //startFakeJukeboxes(jb.getBlock().getLocation(),record);
    }

    private void startFakeJukeboxes(Location location, Material record) {
        ArrayList<Location> locs = JukeboxUtils.getDistandSpeakers(location);
        for(Location loc : locs) {
            loc.getWorld().playSound(loc,SongUtils.getSound(record), SoundCategory.BLOCKS,100,1);
            TestUtils.spawnPillar(loc);
        }
    }

    void stopJukebox(Jukebox jb, boolean reset) {

        if(endTime == 0) return;

        endTime = 0;
        if(reset) {
            loop = false;
            shuffle = false;
        }
        //jb.update();
        Collection<Entity> nearbyEntities = Utils.getNearbyPlayers(jb.getBlock(),radius);
        if(record!=null) {
            for (Entity entity : nearbyEntities) {
                main.debug("Stopping " + record + " for player " + entity.getName());
                if (entity instanceof Player)
                    ((Player) entity).stopSound(SongUtils.getSound(record), SoundCategory.BLOCKS);
            }
        }

        /*if(main.getConfig().getBoolean("debug")) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        if(reset) {
            record = null;
        }
        main.utils.updateInventoryViews("Stopped Jukebox");
    }

    void toggleLoop(Main main,@Nullable Player p) {
        this.loop = !loop;
        main.debug("Loop: " + loop);
        if (loop) {
            if (record == null || record == Material.AIR) {
                main.debug("Cannot loop without record");
                if(p!=null) {
                    main.messageUtils.send("Choose a record first.",false,p,true);
                }
                this.loop = false;
                return;
            }

            if (endTime == 0 || new Date().getTime() >= endTime) main.taskController.startLoop(getBlock());

        }
        main.utils.updateInventoryViews("Toggled Loop");
    }

    public void toggleShuffle(Main main,Player p) {
        this.shuffle = !shuffle;
        main.debug("Shuffle: " + shuffle);
        if (shuffle) {
            if (records.size() == 0) {
                main.debug("Cannot shuffle without records");
                main.messageUtils.send("Cannot shuffle without records",false,p,true);
                this.shuffle = false;
                return;
            } else {
                if (endTime == 0 || new Date().getTime() >= endTime) {
                    try {
                        randomRecord();
                        startJukebox(getJukebox(),record,p); //main.taskController.startLoop(getBlock());
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
