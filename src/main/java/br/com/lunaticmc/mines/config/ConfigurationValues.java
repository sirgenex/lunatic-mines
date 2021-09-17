package br.com.lunaticmc.mines.config;

import br.com.lunaticmc.mines.Mines;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

@Getter
public class ConfigurationValues {

    @Setter @Getter private static ConfigurationValues instance;

    private final FileConfiguration c = Mines.getInstance().getConfig();
    private final FileConfiguration db = Mines.getInstance().getDB();

    private final String permission = c.getString("permission");
    private final String bar = c.getString("actionbar");

    @Setter private Location joinLocation;
    @Setter private Location leaveLocation;

    private final Integer explosiveRadius = c.getInt("enchantments.explosive.radius");
    private final Integer pickaxeSlot = c.getInt("slot");

    private final Material blockType = Material.IRON_ORE;

    @Getter private static final HashMap<String, Double> prices = new HashMap<>();

    private final List<String> joinMessage = c.getStringList("messages.join");
    private final List<String> leaveMessage = c.getStringList("messages.leave");
    private final List<String> leaveSetMessage = c.getStringList("messages.leave_set");
    private final List<String> joinSetMessage = c.getStringList("messages.join_set");
    private final List<String> noPermissionMessage = c.getStringList("messages.without_permission");
    private final List<String> withoutBlocksMessage = c.getStringList("messages.without_blocks");
    private final List<String> maxEnchantMessage = c.getStringList("messages.max_enchant");

    public void setLocation(String path, Location l){
        db.set("locations."+path+".world", l.getWorld().getName());
        db.set("locations."+path+".x", l.getX());
        db.set("locations."+path+".y", l.getY());
        db.set("locations."+path+".z", l.getZ());
        db.set("locations."+path+".yaw", l.getYaw());
        db.set("locations."+path+".pitch", l.getPitch());
        Mines.getInstance().saveDB();
        Mines.getInstance().reloadDB();
    }

    public Location getLocation(String path){
        String w = db.getString("locations."+path+".world");
        double x = db.getDouble("locations."+path+".x");
        double y = db.getDouble("locations."+path+".y");
        double z = db.getDouble("locations."+path+".z");
        float yaw = db.getLong("locations."+path+".yaw");
        float pitch = db.getLong("locations."+path+".pitch");
        return db.getString("locations."+path) == null ? null : new Location(Bukkit.getWorld(w), x, y, z, yaw, pitch);
    }

    public static void load(FileConfiguration c){
        c.getConfigurationSection("sell-price").getKeys(true).forEach(permission -> prices.put(permission, c.getDouble("sell-price."+permission)));
    }

}
