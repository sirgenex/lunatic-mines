package br.com.lunaticmc.mines.manager;

import br.com.lunaticmc.blocks.object.controller.BlockPlayerController;
import lombok.Getter;

public class BlocksManager {

    @Getter private static final BlocksManager instance = new BlocksManager();

    public int get(String p){
        return (int) BlockPlayerController.getInstance().get(p).getBalance();
    }

    public boolean has(String p, double amount){
        return BlockPlayerController.getInstance().get(p).has(amount);
    }

    public void remove(String p, double amount){
        BlockPlayerController.getInstance().get(p).remove(amount);
    }

    public int getTotal(String p){
        return (int) BlockPlayerController.getInstance().get(p).getTotal();
    }

    public void add(String p, double amount) {
        BlockPlayerController.getInstance().get(p).add(amount);
    }

}
