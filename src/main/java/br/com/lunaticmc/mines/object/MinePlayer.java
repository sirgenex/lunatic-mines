package br.com.lunaticmc.mines.object;

import br.com.lunaticmc.mines.Mines;
import br.com.lunaticmc.mines.config.ConfigurationValues;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MinePlayer {

    private String name;

    private ItemStack[] contents;

    private Pickaxe pickaxe;

    private boolean onMine, teleporting;

    public void joinMine(){
        Player p = Bukkit.getPlayer(name);
        ConfigurationValues c = ConfigurationValues.getInstance();
        if(c.getJoinLocation() != null){
            setContents(p.getInventory().getContents());
            p.getInventory().clear();
            setOnMine(true);
            setTeleporting(true);
            p.teleport(c.getJoinLocation());
            setTeleporting(false);
            p.getInventory().setHeldItemSlot(c.getPickaxeSlot());
            p.setItemInHand(getPickaxe().getItem());
            sendMessage(c.getJoinMessage());
        }else p.sendMessage("§cA localização de entrada não está definida, peça para um diretor defini-la.");
    }

    public void leaveMine() {
        Player p = Bukkit.getPlayer(name);
        ConfigurationValues c = ConfigurationValues.getInstance();
        if(c.getLeaveLocation() != null) {
            p.getInventory().clear();
            p.getInventory().setContents(getContents());
            setContents(null);
            setOnMine(false);
            setTeleporting(true);
            p.teleport(c.getLeaveLocation());
            setTeleporting(false);
            sendMessage(c.getLeaveMessage());
            pickaxe.save(Mines.getInstance().getDB());
        }else p.sendMessage("§cA localização de saída não está definida, peça para um diretor defini-la.");
    }

    public void sendMessage(List<String> message){
        Player p = Bukkit.getPlayer(name);
        message.forEach(msg -> p.sendMessage(msg.replace("&", "§")));
    }

    public void save(FileConfiguration file) {
        if (getContents() != null) {
            for (int i = 0; i < getContents().length; i++) {
                if (getContents()[i] != null) file.set("players." + getName() + ".inventory." + i, getContents()[i]);
            }
        }
    }
}
