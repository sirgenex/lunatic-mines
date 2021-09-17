package br.com.lunaticmc.mines.enchantment.list;

import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.cache.PickaxeCache;
import br.com.lunaticmc.mines.enchantment.EnchantmentController;
import br.com.lunaticmc.mines.object.Pickaxe;
import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Explosive extends Enchantment {

    @Getter private static final Explosive enchantment = new Explosive();

    public Explosive() {
        super(1000);
    }

    @Override
    public String getName() {
        return "Explosivo";
    }

    public String getPath(){
        return "explosive";
    }

    @Override
    public int getMaxLevel() {
        return Mines.getInstance().getConfig().getInt("enchantments.efficiency.max");
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    public void apply(ItemStack item, int level){
        item.addUnsafeEnchantment(this, level);
    }

    public double getRandomPercentage(Player p){
        Pickaxe pickaxe = PickaxeCache.getInstance().get(p.getName());
        int level = pickaxe.getExplosive();
        double percentage = EnchantmentController.getInstance().getPercentage(this.getPath());
        return percentage-level;
    }

}
