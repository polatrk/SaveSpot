package polatrk.saveSpot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import polatrk.saveSpot.commands.*;

import java.util.*;

public class SaveSpotCommandHandler implements CommandExecutor {

    private final SaveSpot plugin;

    private final Map<Player, BossBar> activeBossBars = new HashMap<>();
    private final Map<Player, Integer> activeTasks = new HashMap<>();

    public SaveSpotCommandHandler(SaveSpot plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            GlobalUtils.sendSaveSpotMessage(sender, "Only players can run this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!isNomenclatureCorrect(args)) {
            GlobalUtils.sendSaveSpotMessage(
                player,
                ChatColor.RED + "Usage: /savespot <remove | cancel | save | goto | show> <private | public> <name>"
            );
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
            case "cancel":
                new CancelCommand(plugin, this).execute(player, args);
                break;
            default:
                GlobalUtils.sendSaveSpotMessage(player, ChatColor.RED + "Unknown action: " + action);
        }
        return true;
    }

    private boolean isNomenclatureCorrect(String[] args) {
        Set<String> validPrivacy = Set.of("private", "public");

        if (!plugin.getValidActions().contains(args[0]))
            return false;

        if (args[0].equals("save") || args[0].equals("goto") || args[0].equals("remove")) {
            if (args.length != 3) {
                return false;
            } else if (!validPrivacy.contains(args[1])) {
                return false;
            }
        }

        if (args[0].equals("cancel"))
            if (args.length > 1)
                return false;

        if (args[0].equals("show")) {
            if (args.length > 3 || args.length < 2) {
                return false;
            } else if (!validPrivacy.contains(args[1])) {
                return false;
            }
        }

        return true;
    }


    public void addActiveBossBar(Player player, BossBar bossBar) {
        activeBossBars.put(player, bossBar);
    }

    public void removeAllActiveBossBars() {
        for (Map.Entry<Player, BossBar> activeBossBarEntry : activeBossBars.entrySet()) {
            activeBossBarEntry.getValue().removePlayer(activeBossBarEntry.getKey());
        }
        activeBossBars.clear();
    }

    public void removeBossBarForPlayer(Player player) {
        activeBossBars.get(player).removePlayer(player);
        activeBossBars.remove(player);
    }

    public void addActiveTask(Player player, int taskId) {
        activeTasks.put(player, taskId);
    }

    public void cancelTaskForPlayer(Player player) {
        Bukkit.getScheduler().cancelTask(activeTasks.get(player));
        activeTasks.remove(player);
    }

    public void cancelGotoForPlayer(Player player) {
        if(activeTasks.containsKey(player))
            cancelTaskForPlayer(player);
        if(activeBossBars.containsKey(player))
            removeBossBarForPlayer(player);
    }

    public CoordInfo getSpotByNameAndPrivacy(boolean isPublic, String name) {
        for(CoordInfo info : plugin.savedSpots) {
            if(info.spotName.equals(name) && info.isPublic == isPublic) {
                return info;
            }
        }

        return null;
    }
}
