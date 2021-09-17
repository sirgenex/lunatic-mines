package br.com.lunaticmc.mines.listener;

import br.com.lunaticmc.mines.cache.MinePlayerCache;
import br.com.lunaticmc.mines.config.ConfigurationValues;
import br.com.lunaticmc.mines.object.MinePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent e){
        MinePlayer player = MinePlayerCache.getInstance().get(e.getPlayer().getName());
        if(player.isOnMine() &&
                !player.isTeleporting() &&
                !e.getPlayer().hasPermission(ConfigurationValues.getInstance().getPermission()))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e){
        MinePlayer player = MinePlayerCache.getInstance().get(e.getWhoClicked().getName());
        if(!player.isOnMine()) return;
        if(e.getHotbarButton() == ConfigurationValues.getInstance().getPickaxeSlot()) e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if(item == null) return;
        if(item.getType().equals(Material.DIAMOND_PICKAXE)) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent e){
        MinePlayer player = MinePlayerCache.getInstance().get(e.getPlayer().getName());
        if(!player.isOnMine()) return;
        ItemStack item = e.getItemDrop().getItemStack();
        if(item == null) return;
        if(item.getType().equals(Material.DIAMOND_PICKAXE)) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        MinePlayer player = MinePlayerCache.getInstance().get(p.getName());
        if(player.isOnMine()) player.leaveMine();
    }

}