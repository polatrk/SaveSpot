package polatrk.saveSpot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import polatrk.saveSpot.commands.GotoCommand;
import polatrk.saveSpot.commands.RemoveCommand;
import polatrk.saveSpot.commands.SaveCommand;
import polatrk.saveSpot.commands.ShowCommand;

import java.util.Set;

public class SaveSpotCommandHandler implements CommandExecutor {

    private final SaveSpot plugin;

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
                new GotoCommand(plugin).execute(player, args);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown action: " + action);
        }
        return true;
    }

    private boolean isValidAction(String action) {
        return Set.of("save", "remove", "show", "goto").contains(action.toLowerCase());
    }
}
