package polatrk.saveSpot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import polatrk.saveSpot.*;

public class GotoCommand extends BaseCommand {
    private SaveSpotCommandHandler commandHandler;

    public GotoCommand(SaveSpot plugin) {
        super(plugin);
    }

    public GotoCommand(SaveSpot plugin, SaveSpotCommandHandler commandHandler) {
        super(plugin);
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(Player player, String[] args) {
        commandHandler.cancelGotoForPlayer(player);

        BossBar bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        commandHandler.addActiveBossBar(player, bossBar);
        CoordInfo info = commandHandler.getSpotByNameAndPrivacy(args[1].equals("public"), args[2]);

        if(info == null) {
            GlobalUtils.sendSaveSpotMessage(player, ChatColor.RED + "No matching spot found.");
            return;
        }

        if(!info.dimension.equals(player.getWorld().getEnvironment())) {
            GlobalUtils.sendSaveSpotMessage(player, ChatColor.RED + "Spot found in another dimension.");
            return;
        }

        bossBar.addPlayer(player);

        PlayerTrackingTask task = new PlayerTrackingTask(info, player, bossBar);
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, 0L, 10L);
        task.setTaskId(taskId);

        commandHandler.addActiveTask(player, taskId);
    }
}

