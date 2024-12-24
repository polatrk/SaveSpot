package polatrk.saveSpot.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import polatrk.saveSpot.CoordInfo;
import polatrk.saveSpot.CoordUtils;
import polatrk.saveSpot.SaveSpot;

public class SaveCommand extends BaseCommand{

    public SaveCommand(SaveSpot plugin) {
        super(plugin);
    }

    public void execute(Player player, String[] args) {
        CoordInfo newCoordInfo = CoordUtils.generateCoordInfo(args, player);
        if (CoordUtils.isDuplicate(plugin.savedSpots, newCoordInfo)) {
            String privacy = newCoordInfo.isPublic ? "public" : "private";
            player.sendMessage(ChatColor.RED + "Spot name already exists in " + privacy + " context.");
        } else {
            plugin.savedSpots.add(newCoordInfo);
            player.sendMessage(ChatColor.GREEN + "Spot successfully saved.");
        }
    }
}
