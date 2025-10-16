package polatrk.saveSpot.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import polatrk.saveSpot.CoordInfo;
import polatrk.saveSpot.SaveSpot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowCommand extends BaseCommand {
    public ShowCommand(SaveSpot plugin) {
        super(plugin);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /savespot show <private|public> [name]");
            return;
        }

        boolean isPublic = args[1].equalsIgnoreCase("public");
        String search = args.length > 2 ? args[2].toLowerCase() : "";

        player.sendMessage(ChatColor.GOLD + "======== Saved Spots ========");
        boolean found = false;

        Map<World.Environment, List<CoordInfo>> sortedSpots = new HashMap<>();
        for (CoordInfo info : plugin.savedSpots) {
            boolean matchesSearch = info.spotName.toLowerCase().contains(search);

            if (info.isPublic == isPublic && matchesSearch) {
                if (!isPublic && !info.playerUUID.equals(player.getUniqueId())) continue;

                sortedSpots.computeIfAbsent(info.dimension, k -> new java.util.ArrayList<>()).add(info);

                found = true;
            }
        }

        for (Map.Entry<World.Environment, List<CoordInfo>> dimensionToInfos : sortedSpots.entrySet()) {
            player.sendMessage(ChatColor.GOLD + "==" + getDimension(dimensionToInfos.getKey()) + ChatColor.GOLD + "==");
            for (CoordInfo coordInfo : dimensionToInfos.getValue()) {
                player.sendMessage(ChatColor.AQUA + "• Spot: " + coordInfo.spotName);
                String coordMessage = ChatColor.YELLOW + " ↳ " + coordInfo.coords.x + ", " + coordInfo.coords.y + ", " + coordInfo.coords.z;
                if (isPublic) {
                    coordMessage += ChatColor.GRAY + " (" + plugin.getServer().getOfflinePlayer(coordInfo.playerUUID).getName() + ")";
                }
                player.sendMessage(coordMessage);
            }
        }

        if (!found) {
            player.sendMessage(ChatColor.RED + "No matching spots found.");
        }

        player.sendMessage(ChatColor.GOLD + "=============================");
    }

    private String getDimension(World.Environment dimension) {
        return switch (dimension) {
            case NETHER -> ChatColor.RED + "Nether";
            case NORMAL -> ChatColor.DARK_GREEN + "Overworld";
            case THE_END -> ChatColor.DARK_AQUA + "The End";
            case CUSTOM -> "UNKNOWN DIMENSION";
        };
    }
}

//        player.sendMessage(ChatColor.GOLD + "Dimension: " + dimension);
//        player.sendMessage(ChatColor.AQUA + "Spot: " + info.spotName);
//        player.sendMessage(ChatColor.YELLOW + "Coords: x=" + info.coords.x + ", y=" + info.coords.y + ", z=" + info.coords.z);
//        if (isPublic) {
//            player.sendMessage(ChatColor.GRAY + "By: " + plugin.getServer().getOfflinePlayer(info.playerUUID).getName());
//        }
//        player.sendMessage("");