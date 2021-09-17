package br.com.lunaticmc.mines.breaker.impl;

import br.com.lunaticmc.mines.breaker.ICustomBlockBreak;
import br.com.lunaticmc.mines.config.ConfigurationValues;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ExplosiveBreaker extends ICustomBlockBreak {

    @Override
    public int handle(Block block, Player p) {
        int blocks = 0;
        int radius = ConfigurationValues.getInstance().getExplosiveRadius();
        Location center = block.getLocation();
        Location min = new Location(center.getWorld(), center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        Location max = new Location(center.getWorld(), center.getX() + radius, center.getY() + radius, center.getZ() + radius);
        for (int y = (int) min.getY(); y < (int) max.getY(); y++) {
            for (int x = (int) min.getX(); x < (int) max.getX(); x++) {
                for (int z = (int) min.getZ(); z < (int) max.getZ(); z++) {
                    Location cord = new Location(center.getWorld(), x, y, z);
                    Block b = cord.getBlock();
                    if (b.getType().equals(ConfigurationValues.getInstance().getBlockType())) {
                        b.setType(Material.AIR);
                        blocks++;
                    }
                }
            }
        }
        return blocks;
    }

}
