package polatrk.saveSpot;

import org.bukkit.entity.Player;

import java.util.List;

public class CoordUtils {

    public static CoordInfo generateCoordInfo(String[] args, Player player) {
        boolean isPublic = args[1].equalsIgnoreCase("public");
        String spotName = args[2];
        CoordInfo.Vector3 coords = new CoordInfo.Vector3(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        );
        return new CoordInfo(player.getUniqueId(), isPublic, spotName, coords, player.getWorld().getEnvironment());
    }

    public static boolean isDuplicate(List<CoordInfo> savedSpots, CoordInfo target) {
        return savedSpots.stream().anyMatch(info -> isMatching(info, target));
    }

    public static boolean isMatching(CoordInfo info, CoordInfo target) {
        return info.isPublic == target.isPublic &&
                info.spotName.equalsIgnoreCase(target.spotName) &&
                info.playerUUID.equals(target.playerUUID);
    }
}
