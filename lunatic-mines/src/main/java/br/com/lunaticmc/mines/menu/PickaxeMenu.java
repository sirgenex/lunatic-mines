package br.com.lunaticmc.mines.menu;

import br.com.lunaticmc.mines.builder.CustomSkullBuilder;
import br.com.lunaticmc.mines.cache.MinePlayerCache;
import br.com.lunaticmc.mines.config.ConfigurationValues;
import br.com.lunaticmc.mines.enchantment.EnchantmentController;
import br.com.lunaticmc.mines.manager.BlocksManager;
import br.com.lunaticmc.mines.manager.FormatManager;
import br.com.lunaticmc.mines.object.MinePlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class PickaxeMenu {

    @Getter private static final PickaxeMenu instance = new PickaxeMenu();

    @Getter private String title;
    private int hotbars;
    public final HashMap<String, Integer> slots = new HashMap<>();
    private ItemStack info;
    private final HashMap<Boolean, ItemStack> enchantment_items = new HashMap<>();
    private ItemStack unavailable;
    private ItemStack max;
    private final ArrayList<Integer> unavailable_slots = new ArrayList<>();

    public void load(FileConfiguration c){
        this.title = c.getString("menu.title").replace("&", "§");
        this.hotbars = c.getInt("menu.hotbars");
        this.info = new CustomSkullBuilder(c.getString("menu.info.skull-url")).setName(c.getString("menu.info.name")).setLore(c.getStringList("menu.info.lore")).get();
        this.unavailable = new CustomSkullBuilder(c.getString("menu.unavailable.skull-url")).setName(c.getString("menu.unavailable.name")).setLore(c.getStringList("menu.unavailable.lore")).get();
        unavailable_slots.addAll(c.getIntegerList("menu.unavailable.slots"));
        slots.put("info", c.getInt("menu.info.slot"));
        this.info = new CustomSkullBuilder(c.getString("menu.info.skull-url")).setName(c.getString("menu.info.name")).setLore(c.getStringList("menu.info.lore")).get();
        enchantment_items.put(true, new CustomSkullBuilder(c.getString("menu.enchantments.can_skull-url")).setName(c.getString("menu.enchantments.name")).setLore(c.getStringList("menu.enchantments.lore")).get());
        enchantment_items.put(false, new CustomSkullBuilder(c.getString("menu.enchantments.cannot_skull-url")).setName(c.getString("menu.enchantments.name")).setLore(c.getStringList("menu.enchantments.lore")).get());
        this.max = new CustomSkullBuilder(c.getString("menu.enchantments.max_skull-url")).setName(c.getString("menu.enchantments.name")).setLore(c.getStringList("menu.enchantments.lore")).get();
        slots.put("enchantment_item", c.getInt("menu.enchantment_item.slot"));
        slots.put("pickaxe", c.getInt("menu.pickaxe_slot"));
    }

    public void open(Player p, ItemStack pickaxe){
        Inventory inventory = Bukkit.createInventory(p, hotbars*9, title);
        inventory.setItem(slots.get("info"), info);
        inventory.setItem(slots.get("pickaxe"), pickaxe);
        unavailable_slots.forEach(slot -> inventory.setItem(slot, unavailable));
        EnchantmentController.getInstance().enchants.keySet().forEach(type -> updateEnchant(p, inventory, type));
        p.openInventory(inventory);
    }

    public void updateEnchant(Player p, Inventory inventory, String type){
        String name = EnchantmentController.getInstance().getName(type);
        MinePlayer player = MinePlayerCache.getInstance().get(p.getName());
        double price = player.getPickaxe().getPriceByEnchantmentType(type);
        ItemStack item;
        if(BlocksManager.getInstance().has(p.getName(), price)) item = enchantment_items.get(true).clone();
        else item = enchantment_items.get(false).clone();
        int level = player.getPickaxe().getLevelByEnchantmentType(type);
        int max = EnchantmentController.getInstance().getMaxLevel(type);
        if(level >= max) item = this.max.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(meta.getDisplayName().replace("{enchantment}", name));
        ArrayList<String> lore = new ArrayList<>();
        for(String line : meta.getLore()) {
            if (line.contains("{description")) EnchantmentController.getInstance().getDescription(type).forEach(desc -> lore.add(desc.replace("&", "§")));
            else lore.add(line.replace("&", "§")
                    .replace("{level}", String.valueOf(level))
                    .replace("{max}", String.valueOf(max))
                    .replace("{price}", FormatManager.getInstance().format(price)));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        int slot = EnchantmentController.getInstance().getSlot(type);
        inventory.setItem(slot, item);
        p.getInventory().setHeldItemSlot(ConfigurationValues.getInstance().getPickaxeSlot());
        inventory.setItem(slots.get("pickaxe"), p.getInventory().getItemInHand());
    }

}
