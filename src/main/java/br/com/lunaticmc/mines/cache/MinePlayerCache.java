package br.com.lunaticmc.mines.cache;

import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.object.MinePlayer;
import lombok.Getter;

import java.util.HashMap;

public class MinePlayerCache {

    @Getter private static final MinePlayerCache instance = new MinePlayerCache();

    private final HashMap<String, MinePlayer> cache = new HashMap<>();

    public void create(MinePlayer model){
        cache.put(model.getName(), model);
    }

    public boolean isCreated(String player){
        return cache.containsKey(player);
    }

    public MinePlayer get(String player){
        return cache.getOrDefault(player, new MinePlayer(player, null, PickaxeCache.getInstance().get(player), false, false));
    }

    public void saveAll(){
        cache.values().forEach(model -> model.save(Mines.getInstance().getDB()));
        Mines.getInstance().saveDB();
        Mines.getInstance().reloadDB();
    }

}
