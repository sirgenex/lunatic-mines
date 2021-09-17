package br.com.lunaticmc.mines.command;

import br.com.lunaticmc.mines.cache.MinePlayerCache;
import br.com.lunaticmc.mines.config.ConfigurationValues;
import br.com.lunaticmc.mines.object.MinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MineCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        ConfigurationValues c = ConfigurationValues.getInstance();
        if(sender instanceof Player) {
            Player p = (Player) sender;
            MinePlayer player = MinePlayerCache.getInstance().get(p.getName());
            if(args.length == 0) {
                if (!MinePlayerCache.getInstance().isCreated(p.getName())) MinePlayerCache.getInstance().create(player);
                if (player.isOnMine()) player.leaveMine();
                else player.joinMine();
            }else{
                if(args[0].equalsIgnoreCase("entrada")){
                    if(!p.hasPermission(c.getPermission())){
                        player.sendMessage(c.getNoPermissionMessage());
                        return true;
                    }
                    c.setJoinLocation(p.getLocation());
                    c.setLocation("join", p.getLocation());
                    player.sendMessage(c.getJoinSetMessage());
                    return true;
                }
                if(args[0].equalsIgnoreCase("saida")){
                    if(!p.hasPermission(c.getPermission())){
                        player.sendMessage(c.getNoPermissionMessage());
                        return true;
                    }
                    c.setLeaveLocation(p.getLocation());
                    c.setLocation("leave", p.getLocation());
                    player.sendMessage(c.getLeaveSetMessage());
                    return true;
                }
            }
        }else sender.sendMessage("§cComando apenas para jogadores.");

        return false;
    }
}
