package polatrk.saveSpot.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import polatrk.saveSpot.CoordInfo;
import polatrk.saveSpot.CoordUtils;
import polatrk.saveSpot.GlobalUtils;
import polatrk.saveSpot.SaveSpot;

public class SaveCommand extends BaseCommand{

    public SaveCommand(SaveSpot plugin) {
        super(plugin);
    }

    public void execute(Player player, String[] args) {
        CoordInfo newCoordInfo = CoordUtils.generateCoordInfo(args, player);
        String privacy = newCoordInfo.isPublic ? "public" : "private";
        if (CoordUtils.isDuplicate(plugin.savedSpots, newCoordInfo)) {
            GlobalUtils.sendSaveSpotMessage(
                    player,
                    ChatColor.RED + "Spot name already exists in " + privacy + " context."
            );

        } else {
            plugin.saveNewSpot(newCoordInfo);
            GlobalUtils.sendSaveSpotMessage(player, ChatColor.GREEN + privacy + " spot saved.");
        }
    }
}
