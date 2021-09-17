package br.com.lunaticmc.mines.breaker.impl;

import br.com.lunaticmc.mines.breaker.ICustomBlockBreak;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class DefaultBreaker extends ICustomBlockBreak {

    @Override
    public int handle(Block block, Player p) {
        return 1;
    }
}
