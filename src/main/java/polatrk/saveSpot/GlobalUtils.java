package polatrk.saveSpot;

import org.bukkit.command.CommandSender;

public class GlobalUtils {
    public static void sendSaveSpotMessage(CommandSender target, String message) {
        target.sendMessage("[SaveSpot]: " + message);
    }

}
