package de.jeff_media.JukeBoxPlus;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class JukeboxData {

    final Main main;
    int radius;
    final UUID world;
    final int x;
    final int y;
    final int z;
    final ArrayList<ItemStack> records = new ArrayList<>();
    boolean loop = false;
    boolean shuffle = false;
    boolean autostart = false;
    ItemStack record = null;
    ItemStack lastRecord = null;
    long endTime = 0;
    File file;
    YamlConfiguration yaml;
    final Random random = new Random();

    JukeboxData(Block block, Main main) {
        /*this.world = block.getWorld().getUID();
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.main = main;
        radius = main.getConfig().getInt(Config.DEFAULT_JUKEBOX_RADIUS);*/
        this(block.getWorld().getUID(),block.getX(),block.getY(), block.getZ(), main);
    }

    JukeboxData(File file, Main main) throws WorldNotFoundException {
        this.main=main;
        main.debug("LOADING JB " + file.getName());
        yaml = YamlConfiguration.loadConfiguration(file);
        world = UUID.fromString(yaml.getString("world"));
        x = yaml.getInt("x");
        y = yaml.getInt("y");
        z = yaml.getInt("z");
        autostart = yaml.getBoolean("autostart");
        radius = yaml.getInt("radius",64);
        /*if(!yaml.getItemStack("lastRecord").equals(null)) {
            lastRecord = null; //Material.getMaterial(yaml.getString("lastRecord"));
        }*/
        this.file = file;
        World worldByUUID = main.getServer().getWorld(world);
        if(worldByUUID==null) {
            main.getLogger().warning("World with uuid "+world.toString()+" not found. Did you generate it? To restore jukeboxes, type /jukebox admin restore <worldname>");
            throw new WorldNotFoundException();
        }
        loadRecords();

        if(autostart) {
            main.debug("Autostart enabled: ");
            loop = yaml.getBoolean("loop");
            if(lastRecord==null) {
                loop = false;
                main.debug("Disabling Autostart because loop is true but lastRecord is null");
            }
            boolean shuffleOnStart = yaml.getBoolean("shuffle");

            if(loop) {
                main.debug("Autostart loop "+lastRecord);
                //record = Material.getMaterial(yaml.getString("loop"));
                startJukebox();
            } else if(shuffleOnStart) {
                toggleShuffle();
            }
        }
        Bukkit.getWorld(world).getBlockAt(x,y,z).getChunk().addPluginChunkTicket(main);
        //file.delete();
    }

    JukeboxData(UUID world, int x, int y, int z, Main main) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.main = main;
        this.radius = main.getConfig().getInt(Config.DEFAULT_JUKEBOX_RADIUS);
        Bukkit.getWorld(world).getBlockAt(x,y,z).getChunk().addPluginChunkTicket(main);
    }

    boolean addRecord(ItemStack is, Player p) {
        main.debug("Trying to add music disc to jukebox...");
        boolean duplicate = false;
        for(ItemStack contained : records) {
            if(contained.getType() == is.getType()) {
                if(!contained.getItemMeta().hasCustomModelData() && !is.getItemMeta().hasCustomModelData()) {
                    duplicate = true;
                } else {
                    if(contained.getItemMeta().hasCustomModelData() && is.getItemMeta().hasCustomModelData()) {
                        if(contained.getItemMeta().getCustomModelData() == is.getItemMeta().getCustomModelData()) {
                            duplicate = true;
                        }
                    }
                }
            }
        }
        if (duplicate) {
            main.debug("Already contains " + is.getType().name());
            main.messageUtils.send(main.msg.ALREADY_ADDED.replaceAll("\\{NAME}",main.songUtils.getName(is)),  false,p,true);
            return false;
        }
        ItemStack disc = is.clone();
        disc.setAmount(1);
        records.add(disc);
        main.debug("Added disc " + is.getType().name());
        main.messageUtils.send(main.msg.ADDED_DISK.replaceAll("\\{NAME}",main.songUtils.getName(is)),true,p,true);
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

    void loadRecords() {
        //YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        for (Object s : yaml.getList("records")) {
            try {
                records.add((ItemStack) s);
            } catch (Throwable t) {
                records.add(new ItemStack(Material.valueOf((String) s)));
            }
        }
        //record = Material.getMaterial(yaml.getString("record", "nothing")); // TODO (and not here)
    }

    boolean radiusPlus() {
        radius+=main.getConfig().getInt(Config.RADIUS_CHANGE_INTERVAL);
        if(radius>main.getConfig().getInt(Config.MAX_JUKEBOX_RADIUS)) {
            radius=main.getConfig().getInt(Config.MAX_JUKEBOX_RADIUS);
            return false;
        }
        return true;
    }
    boolean radiusMinus() {
        radius-=main.getConfig().getInt(Config.RADIUS_CHANGE_INTERVAL);
        if(radius<1) {
            radius=main.getConfig().getInt(Config.RADIUS_CHANGE_INTERVAL);
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
                ItemStack oldRecord = record;
                while (record == oldRecord) {
                    record = records.get(random.nextInt(records.size()));
                }
        }
        main.debug("Shuffle chose random record "+record);
    }

    ArrayList<String> recordsToStringList() {
        ArrayList<String> list = new ArrayList<>();
        for (ItemStack record : records) {
            list.add(main.songUtils.getName(record));
        }
        return list;
    }

    void save(File file) {
        YamlConfiguration yaml = new YamlConfiguration();
        //yaml.set("records", recordsToStringList());
        for(ItemStack item : records) {
            item.removeEnchantment(new Glow(new NamespacedKey(main,main.getDescription().getName())));
        }
        yaml.set("records",records);
        //String recordName = record == null || record.getType() == Material.AIR ? "none" : record;
        //yaml.set("record", recordName); // TODO
        yaml.set("loop",loop);
        yaml.set("shuffle",shuffle);
        yaml.set("autostart",autostart);
        yaml.set("world", world.toString());
        yaml.set("lastRecord",lastRecord == null ? null : lastRecord);
        yaml.set("x", x);
        yaml.set("y", y);
        yaml.set("z", z);
        yaml.set("radius",radius);
        /*//System.out.println("records: " + records);
        //System.out.println("records == null: " + records == null);
        //System.out.println("file == null: " + file == null);*/
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
        closeAllInventoryViews();
        for(ItemStack itemStack : records) {
            block.getWorld().dropItem(block.getLocation(),itemStack);
        }
    }

    void startJukebox() {
        try {
            startJukebox(getJukebox());
        } catch (BlockIsNoJukeboxException e) {
            main.debug("Tried to start a jukebox that is no jukebox.");
            main.debug(getBlock().toString());
        }
    }

    void startJukebox(Jukebox jb) {
        startJukebox(jb,null);
    }

    void startJukebox(Jukebox jb, @Nullable Player p) {
        startJukebox(jb,record,p);
    }

    void startJukebox(Jukebox jb, ItemStack itemStack,@Nullable Player p) {
        if ((itemStack == null || itemStack.getType() == Material.AIR) && lastRecord==null) {
            main.debug("Cannot start without a record");
            if(p != null) {
                main.messageUtils.send("Choose a record first.",false,p,true);
            }
            return;
        }
        //main.debug("Starting Jukebox: Record="+itemStack+" LastRecord="+lastRecord);
        if((itemStack==null || itemStack.getType()==Material.AIR)&& lastRecord != null) {
            main.debug("Record is null, using last record "+lastRecord.getType().name());

            itemStack = lastRecord;
        } else {
            main.debug("Setting last record to "+itemStack.getType().name());
            lastRecord = itemStack;
        }
        //main.debug("Starting Jukebox with " + itemStack.getType().name() + " (radius="+radius+")");
        main.debug("Starting Jukebox with " + main.songUtils.getName(itemStack));
        int duration = main.songUtils.getDuration(itemStack);
        Collection<Entity> nearby = Utils.getNearbyPlayers(jb.getBlock(),radius);
        for(Entity entity : nearby) {
            Player pn = (Player) entity;
            if(record!=null) {
                pn.stopSound(SongUtils.getSound(record), SoundCategory.RECORDS);
            }
            String sound = SongUtils.getSound(itemStack);
            pn.playSound(getBlock().getLocation(),sound,SoundCategory.RECORDS,((float) radius)/16,1);
            pn.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',main.msg.NOW_PLAYING.replace("{NAME}",main.songUtils.getName(itemStack)))));
            main.debug("Play volume: " + ((float) radius)/16);
        }
        setEndTime(duration);
        record = itemStack;
        main.utils.updateInventoryViews("Started Jukebox");
        //startFakeJukeboxes(jb.getBlock().getLocation(),record);
    }

    void stopJukebox(Jukebox jb, boolean reset) {

        if(endTime == 0) return;

        if(record != null) {
            for(ItemStack item : records) {
                if(RecordUtils.itemStackEquals(item, record)) {
                    item.removeEnchantment(new Glow(new NamespacedKey(main, main.getDescription().getName())));
                }
            }
        }

        endTime = 0;
        if(reset) {
            loop = false;
            shuffle = false;
        }
        //jb.update();
        Collection<Entity> nearbyEntities = Utils.getNearbyPlayers(jb.getBlock(),radius);
        if(record!=null) {
            for (Entity entity : nearbyEntities) {
                //main.debug("Stopping " + record + " for player " + entity.getName());
                if (entity instanceof Player)
                    ((Player) entity).stopSound(SongUtils.getSound(record), SoundCategory.RECORDS);
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
            if ((record == null || record.getType() == Material.AIR) && lastRecord == null) {
                main.debug("Cannot loop without record");
                if(p!=null) {
                    main.messageUtils.send("Choose a record first.",false,p,true);
                }
                this.loop = false;
                return;
            }

            Iterator<ItemStack> iterator = records.iterator();
            boolean found = false;
            while(iterator.hasNext()) {
                ItemStack current = iterator.next();
                if(!RecordUtils.itemStackEquals(current,lastRecord)) continue;
                found = true;
                break;
            }
            if(!found) {
                return;
            }

            if(record == null || record.getType() == Material.AIR) {
                //record = lastRecord;
                startJukebox();
            }

            if (endTime == 0 || new Date().getTime() >= endTime) main.taskController.startLoop(getBlock());

        }
        main.utils.updateInventoryViews("Toggled Loop");
    }

    public void toggleShuffle() {
        toggleShuffle(null);
    }
    public void toggleShuffle(@Nullable Player p) {
        this.shuffle = !shuffle;
        main.debug("Shuffle: " + shuffle);
        if (shuffle) {
            if (records.size() == 0) {
                main.debug("Cannot shuffle without records");
                if(p!=null) {
                    main.messageUtils.send("Cannot shuffle without records",false,p,true);
                }
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

    void closeAllInventoryViews() {
        for(Map.Entry<UUID, JukeboxGUI> entry : main.openGUIs.entrySet()) {
            for(HumanEntity viewer : entry.getValue().getInventory().getViewers()) {
                viewer.closeInventory();
            }
        }
    }

    private class BlockIsNoJukeboxException extends Exception {

    }

    class WorldNotFoundException extends Exception {

    }

}
