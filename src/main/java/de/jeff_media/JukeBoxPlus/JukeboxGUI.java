package de.jeff_media.JukeBoxPlus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JukeboxGUI implements InventoryHolder {

    final Main main;

    final Jukebox jb;
    final JukeboxData jd;
    final Inventory inv;

    final ItemStack frame;


    JukeboxGUI(Jukebox jb, JukeboxData jd, Main main) {
        this.jb = jb;
        this.jd = jd;
        this.main = main;
        frame = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = frame.getItemMeta();
        meta.setDisplayName("");
        frame.setItemMeta(meta);
        this.inv = Bukkit.createInventory(this, 54, main.getConfig().getString("gui-title"));

    }

    void addFrame() {

        drawLine(frame, 0);
        inv.setItem(1 * 9 + 0, frame);
        inv.setItem(1 * 9 + 8, frame);
        inv.setItem(2 * 9 + 0, frame);
        inv.setItem(2 * 9 + 8, frame);
        drawLine(frame, 3);
        inv.setItem(4 * 9 + 0, frame);
        inv.setItem(4 * 9 + 8, frame);
        inv.setItem(4 * 9 + 0, frame);
        inv.setItem(4 * 9 + 8, frame);
        drawLine(frame, 5);

    }

    void addLoopButton() {
        ItemStack button = HeadCreator.getHead(jd.loop ?
                main.getConfig().getString("button-loop-enabled")
                :
                main.getConfig().getString("button-loop-disabled"));

        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.LOOP+": " + enabledString(jd.loop));
        button.setItemMeta(meta);
        if(jd.loop) makeItShine(button);
        inv.setItem(4 * 9 + 1, button);
    }

    void makeItShine(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(main, main.getDescription().getName());
        Glow glow = new Glow(key);
        meta.addEnchant(glow, 1, true);
        item.setItemMeta(meta);
    }

    void addShuffleButton() {
        ItemStack button = HeadCreator.getHead(jd.shuffle ?
                main.getConfig().getString("button-shuffle-enabled")
                :
                main.getConfig().getString("button-shuffle-disabled"));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.SHUFFLE+": " + enabledString(jd.shuffle));
        button.setItemMeta(meta);
        if(jd.shuffle) makeItShine(button);
        inv.setItem(4 * 9 + 2, button);
    }

    void addAutostartButton() {
        ItemStack button = HeadCreator.getHead(jd.autostart ?
                main.getConfig().getString("button-autostart-enabled")
                :
                main.getConfig().getString("button-autostart-disabled"));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.AUTOSTART+": " + enabledString(jd.autostart));
        button.setItemMeta(meta);
        if(jd.autostart) makeItShine(button);
        inv.setItem(4 * 9 + 3, button);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean isJukeboxGUI(Inventory inv) {
        if(inv==null) return false;
        if(inv.getHolder()==null) return false;
        return inv.getHolder() instanceof JukeboxGUI;
    }

    private void addStopButton() {
        ItemStack button = HeadCreator.getHead(main.getConfig().getString("button-stop"));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.STOP);
        button.setItemMeta(meta);
        inv.setItem(4 * 9 + 7, button);
    }

    private List<String> StringToList(String s) {
        ArrayList<String> list = new ArrayList<>();
        list.add(s);
        return list;
    }

    private void addRadiusMinusButton() {
        ItemStack button = HeadCreator.getHead(main.getConfig().getString("button-radius-minus"));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.RADIUS+" -");
        meta.setLore(StringToList(main.msg.RADIUS+": "+jd.radius));
        button.setItemMeta(meta);
        inv.setItem(4*9+4,button);
    }

    private void addRadiusPlusButton() {
        ItemStack button = HeadCreator.getHead(main.getConfig().getString("button-radius-plus"));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.RADIUS+" +");
        meta.setLore(StringToList(main.msg.RADIUS+": "+jd.radius));
        button.setItemMeta(meta);
        inv.setItem(4*9+5,button);
    }

    void drawLine(ItemStack is, int line) {
        for (int i = line * 9; i < 9 + line * 9; i++) {
            inv.setItem(i, is);
        }
    }

    String enabledString(Boolean b) {
        if (b) return main.msg.ENABLED;
        return main.msg.DISABLED;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    void update() {
        getInventory().getViewers().forEach((viewer) -> {
            if(viewer instanceof Player) {
                open((Player)viewer);
            }
        });
    }

    ItemStack getDiscItem(Material record) {
        ItemStack disc = new ItemStack(record);
        ItemMeta meta = disc.getItemMeta();
        meta.setDisplayName(main.getConfig().getString(Config.DISC_NAME).replaceAll("\\{NAME}",main.songUtils.getName(record)));
        ArrayList<String> lore = new ArrayList<>();
        for(String line : main.getConfig().getString(Config.DISC_LORE).split("\n")) {
            lore.add(line.replaceAll("\\{DURATION}",main.songUtils.getFormattedDuration(record))
                    .replaceAll("\\{NAME}",main.songUtils.getName(record)));
        }
        meta.setLore(lore);
        disc.setItemMeta(meta);
        return disc;
    }

    void open(Player p) {
        inv.clear();

        addFrame();

        for (Material record : Objects.requireNonNull(Objects.requireNonNull(jd, "jd is null").records, "jd.records is null")) {
            ItemStack disc = getDiscItem(record);

            if (record == jd.record) {
                makeItShine(disc);
            }
            inv.addItem(new ItemStack(disc));
        }

        addStopButton();

        if(p.hasPermission(Permissions.ALLOW_LOOP)) addLoopButton();
        if(p.hasPermission(Permissions.ALLOW_SHUFFLE)) addShuffleButton();
        if(p.hasPermission(Permissions.ALLOW_AUTOSTART)) addAutostartButton();
        if(p.hasPermission(Permissions.ALLOW_CHANGE_RADIUS)) {
            addRadiusMinusButton();
            addRadiusPlusButton();
        }
        // TODO heads must not glow as it cannot be seen anyway

        if (p.getOpenInventory() == null || p.getOpenInventory().getTopInventory() != inv) {
            p.openInventory(inv);
            main.debug("Opening new inv");
        } else {
            p.updateInventory();
            main.debug("Updating inv");
        }

        main.openGUIs.put(p.getUniqueId(),this);
    }
}
