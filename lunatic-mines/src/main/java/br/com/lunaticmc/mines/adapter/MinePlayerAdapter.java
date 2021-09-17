package br.com.lunaticmc.mines.adapter;

import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.cache.MinePlayerCache;
import br.com.lunaticmc.mines.cache.PickaxeCache;
import br.com.lunaticmc.mines.object.MinePlayer;
import lombok.Getter;

public class MinePlayerAdapter {

    @Getter private static final MinePlayerAdapter instance = new MinePlayerAdapter();

    public void adaptAll(){
        Mines.getInstance().getDB().getConfigurationSection("players").getKeys(false).forEach(this::adapt);
    }

    public void adapt(String player){
        MinePlayer minePlayer = new MinePlayer(player, null, PickaxeCache.getInstance().get(player), false, false);
        MinePlayerCache.getInstance().create(minePlayer);
    }

}
