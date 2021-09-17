package br.com.lunaticmc.mines.enchantment;

import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.cache.MinePlayerCache;
import br.com.lunaticmc.mines.enchantment.list.Explosive;
import br.com.lunaticmc.mines.manager.BlocksManager;
import br.com.lunaticmc.mines.manager.PickaxeManager;
import br.com.lunaticmc.mines.menu.PickaxeMenu;
import br.com.lunaticmc.mines.model.PickaxeModel;
import br.com.lunaticmc.mines.object.MinePlayer;
import br.com.lunaticmc.mines.object.Pickaxe;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentController {

    @Getter private static final EnchantmentController instance = new EnchantmentController();

    private final HashMap<String, String[]> values = new HashMap<>();
    public final HashMap<String, String> enchants = new HashMap<>();
    private final HashMap<String, ArrayList<String>> descriptions = new HashMap<>();

    public void load(){
        for(String type : Arrays.asList(
                "efficiency",
                "fortune",
                "explosive"))
            enchants.put(type, Mines.getInstance().getConfig().getString("enchantments."+type+".name").replace("&", "§"));
        register(Explosive.getEnchantment());
        for(String type : enchants.keySet()){
            ArrayList<String> description = new ArrayList<>(Mines.getInstance().getConfig().getStringList("enchantments."+type+".description"));
            descriptions.put(type, description);
            String[] value = new String[5];
            value[0] = Mines.getInstance().getConfig().getString("enchantments."+type+".price");
            value[1] = Mines.getInstance().getConfig().getString("enchantments."+type+".percent");
            value[2] = Mines.getInstance().getConfig().getString("enchantments."+type+".multiplier");
            value[3] = Mines.getInstance().getConfig().getString("enchantments."+type+".max");
            value[4] = Mines.getInstance().getConfig().getString("enchantments."+type+".slot");
            values.put(type, value);
        }
    }

    public void apply(Inventory inventory, Pickaxe pickaxe, String enchant, double blocks){
        Player p = Bukkit.getPlayer(pickaxe.getName());
        MinePlayer player = MinePlayerCache.getInstance().get(p.getName());
        ItemStack item = p.getItemInHand();
        switch (enchant) {
            case "efficiency":
                if (pickaxe.getEfficiency() < getMaxLevel(enchant)) {
                    pickaxe.setEfficiency(pickaxe.getEfficiency()+1);
                    item.addUnsafeEnchantment(Enchantment.DIG_SPEED, pickaxe.getEfficiency()+1);
                }
                break;
            case "fortune":
                if (pickaxe.getFortune() < getMaxLevel(enchant)) {
                    pickaxe.setFortune(pickaxe.getFortune()+1);
                    item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, pickaxe.getFortune()+1);
                }
                break;
            case "explosive":
                if (pickaxe.getExplosive() < getMaxLevel(enchant)) {
                    pickaxe.setExplosive(pickaxe.getExplosive()+1);
                    Explosive.getEnchantment().apply(item, pickaxe.getExplosive()+1);
                }
                break;
        }
        BlocksManager.getInstance().remove(p.getName(), blocks);
        ItemMeta meta = item.getItemMeta();
        meta.setLore(PickaxeManager.getInstance().getReplacedLore(PickaxeModel.getInstance().getLore(), pickaxe.getEfficiency(), pickaxe.getFortune(), pickaxe.getExplosive()));
        item.setItemMeta(meta);
        pickaxe.reload(item);
        PickaxeMenu.getInstance().updateEnchant(p, inventory, enchant);
        player.setPickaxe(pickaxe);
    }

    public void unload(){
        unregister(Explosive.getEnchantment());
    }

    public double getPrice(String type){
        return Double.parseDouble(values.get(type)[0]);
    }

    public double getPercentage(String type){
        return Double.parseDouble(values.get(type)[1]);
    }

    public double getMultiplier(String type){
        return Double.parseDouble(values.get(type)[2]);
    }

    public int getMaxLevel(String type){
        return Integer.parseInt(values.get(type)[3]);
    }

    public int getSlot(String type){
        return Integer.parseInt(values.get(type)[4]);
    }

    public String getName(String type){
        return enchants.get(type);
    }

    public ArrayList<String> getDescription(String type){
        return descriptions.get(type);
    }

    public void register(Enchantment enchantment) {
        try {
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void unregister(Enchantment enchantment) {
        try {
            Field byIdField = Enchantment.class.getDeclaredField("byId");
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byIdField.setAccessible(true);
            byNameField.setAccessible(true);
            Map<Integer, Enchantment> byId = (Map<Integer, Enchantment>) byIdField.get(null);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
            byId.remove(enchantment.getId());
            byName.remove(enchantment.getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
