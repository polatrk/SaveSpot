package polatrk.saveSpot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public final class SaveSpot extends JavaPlugin {

    public ArrayList<CoordInfo> savedSpots = new ArrayList<>();

    @Override
    public void onEnable() {
        try (FileInputStream fileIn = new FileInputStream("savedSpots.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            savedSpots = (ArrayList<CoordInfo>) in.readObject();
        } catch (Exception e) {
            savedSpots = new ArrayList<>();
            Bukkit.getLogger().info("failed to load savedCoords: " + e);
        }

        getCommand("savespot").setTabCompleter(new SaveSpotTabCompleter());
    }

    @Override
    public void onDisable() {
        try (FileOutputStream fileOut = new FileOutputStream("savedSpots.dat");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(savedSpots);
        } catch (Exception e) {
            Bukkit.getLogger().info("failed to save savedCoords: " + e);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if(!label.equalsIgnoreCase("savespot"))
            return false;

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command");
            return true;
        }

        Player player = (Player) sender;

        // FOR DEBUG
        if(strings.length > 0) {
            if(strings[0].equals("show")) {
                for(CoordInfo info : savedSpots) {
                    getServer().broadcastMessage("---------------------------------");
                    getServer().broadcastMessage(info.spotName);
                    getServer().broadcastMessage(String.valueOf(info.coords.x) + "/" + String.valueOf(info.coords.y)+ "/" + String.valueOf(info.coords.z));
                    getServer().broadcastMessage(info.isPublic ? "public" : "private");
                    getServer().broadcastMessage(info.playerUUID.toString());
                    getServer().broadcastMessage("---------------------------------");
                }
                return true;
            }

        }

        if(!isNomenclatureCorrect(strings)) {
            player.sendMessage(ChatColor.RED + "Usage: /savespot <save|remove> <private|public> <name>");
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        boolean isPublic = strings[1].equals("public");
        String spotName = strings[2];
        CoordInfo.Vector3 coords = new CoordInfo.Vector3(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        );
        CoordInfo newCoordInfo = new CoordInfo(playerUUID, isPublic, spotName, coords);

        player.sendMessage(strings[0] + "/" + spotName);

        if(strings[0].equals("save"))
            saveNewSpot(newCoordInfo, player);
        else
            deleteSpot(newCoordInfo, player);

        return true;
    }

    private void deleteSpot(CoordInfo newCoordInfo, Player player) {
        Iterator<CoordInfo> iterator = savedSpots.iterator();
        while (iterator.hasNext()) {
            CoordInfo info = iterator.next();
            if (info.isPublic == newCoordInfo.isPublic &&
                info.spotName.equals(newCoordInfo.spotName) &&
                info.playerUUID.equals(player.getUniqueId())) {

                iterator.remove();
                player.sendMessage(ChatColor.GREEN + "Spot successfully removed.");
                return;
            }
        }

        player.sendMessage(ChatColor.RED + "Spot doesn't exist with specified properties or you don't own it.");
    }


    private void saveNewSpot(CoordInfo newCoordInfo, Player player) {
        if(isDuplicate(newCoordInfo, player)) {
            String privacyContext = newCoordInfo.isPublic ? "public" : "private";
            player.sendMessage(ChatColor.RED + "Spot name already exist in " + privacyContext + " context.");
            return;
        }

        savedSpots.add(newCoordInfo);
        player.sendMessage(ChatColor.GREEN + "Spot successfully saved.");
    }

    private boolean isDuplicate(CoordInfo newCoordInfo, Player player) {
        for(CoordInfo info : savedSpots) {
            if(newCoordInfo.isPublic) {
                if(info.isPublic)
                    if(info.spotName.equals(newCoordInfo.spotName))
                        return true;
            }
            else { // if newCoordInfo is private
                if(!info.isPublic)
                    if(info.playerUUID.equals(newCoordInfo.playerUUID))
                        if(info.spotName.equals(newCoordInfo.spotName))
                            return true;
            }
        }

        return false;
    }

    private boolean isNomenclatureCorrect(String[] strings) {
        if(strings.length < 3)
            return false;

        Set<String> validActions = Set.of("save", "remove");
        Set<String> validVisibility = Set.of("public", "private");

        return validActions.contains(strings[0]) && validVisibility.contains(strings[1]);
    }
}