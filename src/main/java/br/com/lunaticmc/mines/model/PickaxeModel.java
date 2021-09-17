package br.com.lunaticmc.mines.model;

import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.enchantment.list.Explosive;
import br.com.lunaticmc.mines.manager.PickaxeManager;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
public class PickaxeModel {

    @Getter private static final PickaxeModel instance = new PickaxeModel();

    private ItemStack pickaxe;
    private final List<String> lore = Mines.getInstance().getConfig().getStringList("pickaxe.lore");
    private final String name = Mines.getInstance().getConfig().getString("pickaxe.name");

    public void load(){
        this.pickaxe = get(0, 0, 0, 0);
    }

    public ItemStack get(int blocks, int fortune, int explosive, int efficiency){
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pickaxe.addUnsafeEnchantment(Enchantment.DIG_SPEED, efficiency);
        pickaxe.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, fortune);
        Explosive.getEnchantment().apply(pickaxe, explosive);
        ItemMeta meta = pickaxe.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(PickaxeManager.getInstance().getReplacedName(name,blocks));
        meta.setLore(PickaxeManager.getInstance().getReplacedLore(lore, efficiency, fortune, explosive));
        pickaxe.setItemMeta(meta);
        return pickaxe;
    }

}
