package br.com.lunaticmc.mines.breaker;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Random;

public abstract class ICustomBlockBreak {

    @Getter private final Random random = new Random();

    public int handle(Block block, Player p) {
        return 0;
    }

}
