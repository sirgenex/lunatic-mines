package br.com.lunaticmc.mines.listener;

import br.com.lunaticmc.blocks.event.BlockChangeEvent;
import br.com.lunaticmc.blocks.object.operation.OperationType;
import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.breaker.impl.DefaultBreaker;
import br.com.lunaticmc.mines.breaker.impl.ExplosiveBreaker;
import br.com.lunaticmc.mines.cache.MinePlayerCache;
import br.com.lunaticmc.mines.cache.PickaxeCache;
import br.com.lunaticmc.mines.config.ConfigurationValues;
import br.com.lunaticmc.mines.enchantment.EnchantmentController;
import br.com.lunaticmc.mines.breaker.ICustomBlockBreak;
import br.com.lunaticmc.mines.enchantment.list.Explosive;
import br.com.lunaticmc.mines.manager.ActionbarManager;
import br.com.lunaticmc.mines.manager.BlocksManager;
import br.com.lunaticmc.mines.manager.FormatManager;
import br.com.lunaticmc.mines.manager.SellManager;
import br.com.lunaticmc.mines.menu.PickaxeMenu;
import br.com.lunaticmc.mines.model.PickaxeModel;
import br.com.lunaticmc.mines.object.MinePlayer;
import br.com.lunaticmc.mines.object.Pickaxe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.ThreadLocalRandom;

public class PickaxeListener implements Listener {

    @EventHandler
    public void openMenu(PlayerInteractEvent e){
        Player p = e.getPlayer();
        MinePlayer player = MinePlayerCache.getInstance().get(p.getName());
        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            ItemStack pickaxe = p.getItemInHand();
            if(pickaxe == null) return;
            if(pickaxe.getType().equals(Material.DIAMOND_PICKAXE) && player.isOnMine()){
                e.setCancelled(true);
                PickaxeMenu.getInstance().open(p, pickaxe);
            }
        }
    }

    @EventHandler
    public void interactMenu(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        MinePlayer player = MinePlayerCache.getInstance().get(p.getName());
        Pickaxe pickaxe = player.getPickaxe();
        if(e.getInventory().getTitle().equals(PickaxeMenu.getInstance().getTitle())){
            e.setCancelled(true);
            EnchantmentController.getInstance().enchants.keySet().forEach(type -> {
                if(e.getSlot() == EnchantmentController.getInstance().getSlot(type)) {
                    double price = pickaxe.getPriceByEnchantmentType(type);
                    if (pickaxe.getLevelByEnchantmentType(type) < EnchantmentController.getInstance().getMaxLevel(type)) {
                        if (BlocksManager.getInstance().has(p.getName(), price)) {
                            EnchantmentController.getInstance().apply(e.getInventory(), PickaxeCache.getInstance().get(p.getName()), type, price);
                            p.updateInventory();
                        } else player.sendMessage(ConfigurationValues.getInstance().getWithoutBlocksMessage());
                    } else player.sendMessage(ConfigurationValues.getInstance().getMaxEnchantMessage());
                }
            });
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        MinePlayer player = MinePlayerCache.getInstance().get(p.getName());
        if(!player.isOnMine()) return;
        if(!e.getBlock().getType().equals(ConfigurationValues.getInstance().getBlockType())) return;
        ICustomBlockBreak breaker = new DefaultBreaker();
        if(player.getPickaxe().getExplosive() >= 1) {
            double number = ThreadLocalRandom.current().nextDouble(0, Explosive.getEnchantment().getRandomPercentage(p));
            if (number <= 1) breaker = new ExplosiveBreaker();
        }
        int blocks = breaker.handle(e.getBlock(), p);
        if(blocks > 1){
            BlocksManager.getInstance().add(p.getName(), blocks-1);
        }
        double value = SellManager.getValue(p)*blocks;
        Mines.getEcon().depositPlayer(p, value);
        ActionbarManager.getInstance().send(p, ConfigurationValues.getInstance().getBar().replace("&", "§").replace("{blocks}", String.valueOf(blocks)).replace("{value}", String.valueOf(value)));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDrop(EntitySpawnEvent e){
        if(e.getEntity() instanceof Item){
            ItemStack item = ((Item)e.getEntity()).getItemStack();
            if(item.getType().equals(ConfigurationValues.getInstance().getBlockType())){
                if(e.getLocation().getWorld().equals(ConfigurationValues.getInstance().getJoinLocation().getWorld())){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockChange(BlockChangeEvent e){
        Player p = Bukkit.getPlayer(e.getPlayer());
        if(p != null) {
            if (e.getOperation().equals(OperationType.ADD)) {
                ItemStack pickaxe = p.getItemInHand();
                if (pickaxe == null) return;
                if (pickaxe.getType().equals(Material.DIAMOND_PICKAXE)) {
                    ItemMeta meta = pickaxe.getItemMeta();
                    meta.setDisplayName(PickaxeModel.getInstance().getName().replace("&", "§").replace("{blocks}", FormatManager.getInstance().format(BlocksManager.getInstance().getTotal(p.getName()))));
                    pickaxe.setItemMeta(meta);
                }
            }
        }
    }

}
