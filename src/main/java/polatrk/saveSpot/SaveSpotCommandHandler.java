package polatrk.saveSpot;

import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import polatrk.saveSpot.commands.GotoCommand;
import polatrk.saveSpot.commands.RemoveCommand;
import polatrk.saveSpot.commands.SaveCommand;
import polatrk.saveSpot.commands.ShowCommand;

import java.util.*;

public class SaveSpotCommandHandler implements CommandExecutor {

    private final SaveSpot plugin;

    private final List<BossBar> activeBossBars = new ArrayList<>();

    public SaveSpotCommandHandler(SaveSpot plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2 || !isValidAction(args[0])) {
            player.sendMessage(ChatColor.RED + "Usage: /savespot <goto|show|save|remove> <private|public> <name>");
            return true;
        }

        String action = args[0].toLowerCase();
        switch (action) {
            case "save":
                new SaveCommand(plugin).execute(player, args);
                break;
            case "remove":
                new RemoveCommand(plugin).execute(player, args);
                break;
            case "show":
                new ShowCommand(plugin).execute(player, args);
                break;
            case "goto":
                new GotoCommand(plugin, this).execute(player, args);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown action: " + action);
        }
        return true;
    }

    private boolean isValidAction(String action) {
        return Set.of("save", "remove", "show", "goto").contains(action.toLowerCase());
    }


    public void addActiveBossBars(BossBar bossBar) {
        activeBossBars.add(bossBar);
    }

    public void removeAllActiveBossBars() {
        for (BossBar bossBar : activeBossBars) {
            bossBar.removeAll();
        }
        activeBossBars.clear();
    }

    public CoordInfo getSpotByNameAndPrivacy(boolean isPublic, String name) {
        plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + name + (isPublic ? "public" : "private"));
        for(CoordInfo info : plugin.savedSpots) {
            if(info.spotName.equals(name) && info.isPublic == isPublic) {
                plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "Spot found.");
                return info;
            }
        }

        plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "No spot found.");
        return null;
    }
}
