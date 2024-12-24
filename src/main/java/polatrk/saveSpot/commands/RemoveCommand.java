package polatrk.saveSpot.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import polatrk.saveSpot.CoordInfo;
import polatrk.saveSpot.CoordUtils;
import polatrk.saveSpot.SaveSpot;

import java.util.Iterator;

public class RemoveCommand extends BaseCommand{

    public RemoveCommand(SaveSpot plugin) {
        super(plugin);
    }

    public void execute(Player player, String[] args) {
        CoordInfo target = CoordUtils.generateCoordInfo(args, player);
        Iterator<CoordInfo> iterator = plugin.savedSpots.iterator();
        while (iterator.hasNext()) {
            CoordInfo info = iterator.next();
            if (CoordUtils.isMatching(info, target)) {
                iterator.remove();
                player.sendMessage(ChatColor.GREEN + "Spot successfully removed.");
                return;
            }
        }
        player.sendMessage(ChatColor.RED + "Spot don't exists or you don't owns it.");
    }
}
