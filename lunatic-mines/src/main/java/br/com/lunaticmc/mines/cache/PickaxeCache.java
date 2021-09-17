package br.com.lunaticmc.mines.cache;

import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.model.PickaxeModel;
import br.com.lunaticmc.mines.object.Pickaxe;
import lombok.Getter;

import java.util.HashMap;

public class PickaxeCache {

    @Getter private static final PickaxeCache instance = new PickaxeCache();

    private final HashMap<String, Pickaxe> cache = new HashMap<>();

    public void create(Pickaxe model){
        cache.put(model.getName(), model);
    }

    public Pickaxe get(String player){
        return cache.getOrDefault(player, new Pickaxe(player, PickaxeModel.getInstance().getPickaxe(), 0, 0, 0));
    }

    public void saveAll(){
        cache.values().forEach(model -> model.save(Mines.getInstance().getDB()));
        Mines.getInstance().saveDB();
        Mines.getInstance().reloadDB();
    }

}
