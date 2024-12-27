package polatrk.saveSpot.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import polatrk.saveSpot.CoordInfo;
import polatrk.saveSpot.SaveSpot;

public class ShowCommand extends BaseCommand {
    public ShowCommand(SaveSpot plugin) {
        super(plugin);
    }

    public void execute(Player player, String[] args) {
        boolean isPublic = args[1].equalsIgnoreCase("public");
        String search = args.length > 2 ? args[2].toLowerCase() : "";

        player.sendMessage(ChatColor.GOLD + "======== Saved Spots ========");
        boolean found = false;

        String dimension = "";
        switch (player.getWorld().getEnvironment()) {
            case NETHER -> dimension = ChatColor.RED + "Nether";
            case NORMAL -> dimension = ChatColor.DARK_GREEN + "Overworld";
            case THE_END -> dimension = ChatColor.DARK_AQUA + "The End";
        }

        for (CoordInfo info : plugin.savedSpots) {
            if (info.isPublic == isPublic && info.spotName.toLowerCase().contains(search)
             && info.playerUUID.equals(player.getUniqueId())
             && info.dimension.equals(player.getWorld().getEnvironment())) {
                player.sendMessage(ChatColor.GOLD + "Dimension: " + dimension);
                player.sendMessage(ChatColor.AQUA + "Spot: " + info.spotName);
                player.sendMessage(ChatColor.YELLOW + "Coords: x=" + info.coords.x + ", y=" + info.coords.y + ", z=" + info.coords.z);
                found = true;
            }
        }

        if (!found) {
            player.sendMessage(ChatColor.RED + "No matching spots found.");
        }
        player.sendMessage(ChatColor.GOLD + "=============================");
    }
}
