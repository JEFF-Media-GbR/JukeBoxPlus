package de.jeff_media.JukeBoxPlus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Jukebox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class JukeboxGUI implements InventoryHolder {

    final Main main;

    final Jukebox jb;
    final JukeboxData jd;
    final Inventory inv;

    final ItemStack frame;

    public static Set<Integer> DISC_SPACES = new HashSet<Integer>() {
        {
            add(1);
            add(2);
            add(3);
        }
    };


    JukeboxGUI(Jukebox jb, JukeboxData jd, Main main) {
        this.jb = jb;
        this.jd = jd;
        this.main = main;
        frame = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = frame.getItemMeta();
        meta.setDisplayName("§7");
        frame.setItemMeta(meta);
        this.inv = Bukkit.createInventory(this, 54, main.getConfig().getString("gui-title"));
    }

    void addFrame() {

        drawLine(frame, 0);
        inv.setItem(1 * 9 + 0, frame);
        inv.setItem(1 * 9 + 8, frame);
        //drawLine(frame, 1);
        inv.setItem(2 * 9 + 0, frame);
        inv.setItem(2 * 9 + 8, frame);
        inv.setItem(3 * 9 + 0, frame);
        inv.setItem(3 * 9 + 8, frame);
        //drawLine(frame, 3);
        drawLine(frame, 4);
        inv.setItem(5 * 9 + 0, frame);
        inv.setItem(5 * 9 + 8, frame);
        inv.setItem(5 * 9 + 0, frame);
        inv.setItem(5 * 9 + 8, frame);
        //drawLine(frame, 5);

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
        inv.setItem(ButtonSlot.LOOP, button);
    }

    void makeItShine(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(main, main.getDescription().getName());
        Enchantment glow = Enchantment.UNBREAKING;
        meta.addEnchant(glow, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
        inv.setItem(ButtonSlot.SHUFFLE, button);
    }

    void addNextPageButton(int currentPage) {
        ItemStack button = HeadCreator.getHead(
                main.getConfig().getString("button-next-page"));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.NEXT_PAGE);
        meta.getPersistentDataContainer().set(PDC.CURRENT_PAGE, PersistentDataType.INTEGER, currentPage);
        button.setItemMeta(meta);
        inv.setItem(ButtonSlot.NEXT_PAGE, button);
    }

    void addPrevPageButton(int currentPage) {
        ItemStack button = HeadCreator.getHead(
                main.getConfig().getString("button-previous-page"));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.PREVIOUS_PAGE);
        meta.getPersistentDataContainer().set(PDC.CURRENT_PAGE, PersistentDataType.INTEGER, currentPage);
        button.setItemMeta(meta);
        inv.setItem(ButtonSlot.PREVIOUS_PAGE, button);
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
        inv.setItem(ButtonSlot.AUTOSTART, button);
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
        inv.setItem(ButtonSlot.STOP, button);
    }

    private List<String> stringToList(String s) {
        ArrayList<String> list = new ArrayList<>();
        list.add(s);
        return list;
    }

    private void addRadiusMinusButton() {
        ItemStack button = HeadCreator.getHead(main.getConfig().getString("button-radius-minus"));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.RADIUS+" -");
        meta.setLore(stringToList(main.msg.RADIUS+": "+jd.radius));
        button.setItemMeta(meta);
        inv.setItem(ButtonSlot.RADIUS_MINUS,button);
    }

    private void addRadiusPlusButton() {
        ItemStack button = HeadCreator.getHead(main.getConfig().getString("button-radius-plus"));
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(main.msg.RADIUS+" +");
        meta.setLore(stringToList(main.msg.RADIUS+": "+jd.radius));
        button.setItemMeta(meta);
        inv.setItem(ButtonSlot.RADIUS_PLUS,button);
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

//    void open(Player p) {
//        open(p, 0);
//    }

    void open(Player p/*, int page*/) {

        int page = jd.currentPage;
        inv.clear();

        addFrame();

        addStopButton();

        if(p.hasPermission(Permissions.ALLOW_LOOP)) addLoopButton();
        if(p.hasPermission(Permissions.ALLOW_SHUFFLE)) addShuffleButton();
        if(p.hasPermission(Permissions.ALLOW_AUTOSTART)) addAutostartButton();
        if(p.hasPermission(Permissions.ALLOW_CHANGE_RADIUS)) {
            addRadiusMinusButton();
            addRadiusPlusButton();
        }


        for(int line = 0; line <= 5; line++) {
            if(DISC_SPACES.contains(line)) continue;

            for(int column = 0; column <= 8; column++) {
                if(inv.getItem(line*9+column)==null) {
                    inv.setItem(line*9+column,frame);
                }
            }
        }

        if(page < 0) page = 0;
        //if(page > getMaxPage()) page = getMaxPage();

        int spacesPerPage = DISC_SPACES.size() * 7; // or 9

        int startingIndex = page * spacesPerPage;
        int checkedDiscs = 0;
        boolean hasNextPage = false;

        for (ItemStack record : Objects.requireNonNull(Objects.requireNonNull(jd, "jd is null").records, "jd.records is null")) {
            if (checkedDiscs++ < startingIndex) {
                continue;
            }

            ItemStack disc = RecordUtils.getDiscItem(record);

            if (RecordUtils.itemStackEquals(record,jd.record)) {
                makeItShine(disc);
            } else {
                disc.removeEnchantment(Enchantment.UNBREAKING);
            }
            hasNextPage = !inv.addItem(new ItemStack(disc)).isEmpty();
        }

        if(page > 0) {
            addPrevPageButton(page);
        }
        if(hasNextPage) {
            addNextPageButton(page);
        }

        jd.currentPage = page;


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
