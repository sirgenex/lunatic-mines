package br.com.lunaticmc.mines.object;

import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.enchantment.EnchantmentController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

@Setter
@Getter
@AllArgsConstructor
public class Pickaxe {

    private String name;

    private ItemStack item;

    private int efficiency, fortune, explosive;

    public void save(FileConfiguration file){
        file.set("players."+getName()+".pickaxe.efficiency", getEfficiency());
        file.set("players."+getName()+".pickaxe.fortune", getFortune());
        file.set("players."+getName()+".pickaxe.explosive", getExplosive());
    }

    public double getPriceByEnchantmentType(String type){
        return getLevelByEnchantmentType(type) == 0 ? EnchantmentController.getInstance().getPrice(type) : EnchantmentController.getInstance().getPrice(type)*(EnchantmentController.getInstance().getMultiplier(type)*(getLevelByEnchantmentType(type)));
    }

    public int getLevelByEnchantmentType(String type){
        switch (type){
            case "efficiency":
                return getEfficiency();
            case "fortune":
                return getFortune();
            case "explosive":
                return getExplosive();
        }
        return -1;
    }

    public void reload(ItemStack pickaxe){
        this.item = pickaxe;
        this.save(Mines.getInstance().getDB());
    }

}
