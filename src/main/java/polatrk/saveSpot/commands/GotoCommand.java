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
        BossBar bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        commandHandler.addActiveBossBars(bossBar);
        CoordInfo info = commandHandler.getSpotByNameAndPrivacy(args[1].equals("public"), args[2]);

        if(info == null) {
            player.sendMessage(ChatColor.RED + "No matching spots found.");
        }

        bossBar.addPlayer(player);

        PlayerTrackingTask task = new PlayerTrackingTask(info, player, bossBar);
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, 0L, 10L);
        task.setTaskId(taskId);
    }
}

