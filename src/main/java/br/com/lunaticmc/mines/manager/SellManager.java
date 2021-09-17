package br.com.lunaticmc.mines.manager;

import br.com.lunaticmc.mines.config.ConfigurationValues;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicReference;

public class SellManager {

    public static Double getValue(Player p){
        AtomicReference<Double> value = new AtomicReference<>((double) 0);
        ConfigurationValues.getPrices().forEach((key, value1) -> {
            if (key.equalsIgnoreCase("default") && value.get() == 0) value.set(value1);
            if (p.hasPermission(key)) value.set(value1);
        });
        return value.get();
    }

}
