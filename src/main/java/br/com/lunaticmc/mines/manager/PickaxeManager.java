package br.com.lunaticmc.mines.manager;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PickaxeManager {

    @Getter private static final PickaxeManager instance = new PickaxeManager();

    public ArrayList<String> getReplacedLore(List<String> original, int efficiency, int fortune, int explosive){
        ArrayList<String> lore = new ArrayList<>();
        original.forEach(line -> lore.add(line.replace("&", "§")
        .replace("{efficiency}", String.valueOf(efficiency))
        .replace("{fortune}", String.valueOf(fortune))
        .replace("{explosive}", String.valueOf(explosive))));
        return lore;
    }

    public String getReplacedName(String name, int blocks){
        return name.replace("&", "§")
                .replace("{blocks}", FormatManager.getInstance().format(blocks));
    }

}
