package br.com.lunaticmc.mines.adapter;

import br.com.lunaticmc.blocks.object.controller.BlockPlayerController;
import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.cache.MinePlayerCache;
import br.com.lunaticmc.mines.cache.PickaxeCache;
import br.com.lunaticmc.mines.model.PickaxeModel;
import br.com.lunaticmc.mines.object.Pickaxe;
import lombok.Getter;

public class PickaxeAdapter {

    @Getter private static final PickaxeAdapter instance = new PickaxeAdapter();

    public void adaptAll(){
        Mines.getInstance().getDB().getConfigurationSection("players").getKeys(false).forEach(this::adapt);
    }

    public void adapt(String player){
        int fortune = getEnchantmentLevel(player, "fortune");
        int efficiency = getEnchantmentLevel(player, "efficiency");
        int explosive = getEnchantmentLevel(player, "explosive");
        int blocks = (int)BlockPlayerController.getInstance().get(player).getTotal();
        Pickaxe pickaxe = new Pickaxe(player, PickaxeModel.getInstance().get(blocks, fortune, explosive, efficiency), efficiency, fortune, explosive);
        PickaxeCache.getInstance().create(pickaxe);
        MinePlayerCache.getInstance().get(player).setPickaxe(pickaxe);
    }

    public int getEnchantmentLevel(String player, String enchantment){
        return Mines.getInstance().getDB().getInt("players."+player+".pickaxe."+enchantment);
    }

}
