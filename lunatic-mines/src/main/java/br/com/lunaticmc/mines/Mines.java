package br.com.lunaticmc.mines;

import br.com.lunaticmc.mines.adapter.MinePlayerAdapter;
import br.com.lunaticmc.mines.adapter.PickaxeAdapter;
import br.com.lunaticmc.mines.cache.MinePlayerCache;
import br.com.lunaticmc.mines.cache.PickaxeCache;
import br.com.lunaticmc.mines.command.MineCommand;
import br.com.lunaticmc.mines.config.ConfigurationValues;
import br.com.lunaticmc.mines.enchantment.EnchantmentController;
import br.com.lunaticmc.mines.listener.PickaxeListener;
import br.com.lunaticmc.mines.listener.PlayerListener;
import br.com.lunaticmc.mines.menu.PickaxeMenu;
import br.com.lunaticmc.mines.model.PickaxeModel;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Mines extends JavaPlugin {

    @Getter private static Mines instance;
    @Getter private static Economy econ;

    private File file = null;
    private FileConfiguration fileConfiguration = null;

    @Override
    public void onEnable() {
        getLogger().info("Starting plugin...");
        instance = this;

        getLogger().info("Loading config...");
        saveDefaultConfig();
        ConfigurationValues.load(getConfig());
        getLogger().info("Config loaded successfully!");

        getLogger().info("Loading database...");
        File verifier = new File(getDataFolder(), "db.yml");
        if (!verifier.exists()) saveResource("db.yml", false);
        ConfigurationValues.setInstance(new ConfigurationValues());
        ConfigurationValues.getInstance().setJoinLocation(ConfigurationValues.getInstance().getLocation("join"));
        ConfigurationValues.getInstance().setLeaveLocation(ConfigurationValues.getInstance().getLocation("leave"));
        PickaxeAdapter.getInstance().adaptAll();
        MinePlayerAdapter.getInstance().adaptAll();
        getLogger().info("Database loaded successfully!");

        getLogger().info("Loading models...");
        PickaxeModel.getInstance().load();
        getLogger().info("Models loaded successfully!");

        getLogger().info("Registering commands...");
        getCommand("mina").setExecutor(new MineCommand());
        getLogger().info("Commands registered successfully!");

        getLogger().info("Registering listeners...");
        Bukkit.getPluginManager().registerEvents(new PickaxeListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        getLogger().info("Listeners registered!");

        getLogger().info("Loading menus...");
        PickaxeMenu.getInstance().load(getConfig());
        getLogger().info("Menus loaded successfully!");

        getLogger().info("Trying to hook into PlaceholderAPI...");
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            //TODO: Hook placeholderapi
            getLogger().info("PlaceholderAPI hooked successfully!");
        } else getLogger().warning("PlaceholderAPI can't be found.");

        getLogger().info("Hooking into Vault...");
        if (!setupEconomy() ) {
            getLogger().severe("Vault not found, disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Vault hooked successfully!");

        getLogger().info("Loading enchantments...");
        EnchantmentController.getInstance().load();
        getLogger().info("Enchantments loaded!");

        getLogger().info("Plugin started.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling plugin...");

        getLogger().info("Removing players from mine...");
        Bukkit.getOnlinePlayers().stream().filter(player -> MinePlayerCache.getInstance().get(player.getName()).isOnMine()).forEach(player -> MinePlayerCache.getInstance().get(player.getName()).leaveMine());
        getLogger().info("All players on mine has been removed!");

        getLogger().info("Saving data...");
        MinePlayerCache.getInstance().saveAll();
        PickaxeCache.getInstance().saveAll();
        getLogger().info("Data saved successfully!");

        getLogger().info("Unloading enchantments...");
        EnchantmentController.getInstance().unload();
        getLogger().info("Enchantments unloaded!");

        getLogger().info("Plugin disabled.");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
    }

    public FileConfiguration getDB() {
        if (this.fileConfiguration == null) {
            this.file = new File(getDataFolder(), "db.yml");
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        }
        return this.fileConfiguration;
    }

    public void saveDB() {
        try { getDB().save(this.file); } catch (Exception ignored) {}
    }

    public void reloadDB() {
        if (this.file == null) this.file = new File(getDataFolder(), "db.yml");
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        YamlConfiguration db = YamlConfiguration.loadConfiguration(this.file);
        this.fileConfiguration.setDefaults(db);
    }

}