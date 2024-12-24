package polatrk.saveSpot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class PlayerTrackingTask implements Runnable{

    private Player player;
    private CoordInfo targetInfo;
    private BossBar bossBar;
    private CoordInfo.Vector3 startPlayerPos;
    private int taskId = -1;

    public PlayerTrackingTask(CoordInfo targetInfo, Player player, BossBar bossBar) {
        this.player = player;
        this.targetInfo = targetInfo;
        this.bossBar = bossBar;
        this.startPlayerPos = new CoordInfo.Vector3(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        );
    }

    @Override
    public void run() {
        CoordInfo.Vector3 currentPlayerPos = new CoordInfo.Vector3(
                player.getLocation().getBlockX(),
                player.getLocation().getBlockY(),
                player.getLocation().getBlockZ()
        );

        double distanceStartToCurrent = CoordInfo.Vector3.Distance(startPlayerPos, currentPlayerPos);
        double distanceStartToTarget = CoordInfo.Vector3.Distance(startPlayerPos, targetInfo.coords);
        double progress = Math.max(0.0, Math.min(1.0, distanceStartToCurrent / distanceStartToTarget));

        String coordsDist = String.valueOf(Math.abs(currentPlayerPos.x - targetInfo.coords.x)) + " " +
                            String.valueOf(Math.abs(currentPlayerPos.y - targetInfo.coords.y)) + " " +
                            String.valueOf(Math.abs(currentPlayerPos.z - targetInfo.coords.z));
        String barTitle = "Direction to: " + targetInfo.spotName + ". / " + coordsDist;

        bossBar.setTitle(barTitle);
        bossBar.setProgress(progress);

        if(CoordInfo.Vector3.Distance(currentPlayerPos, targetInfo.coords) < 10.0) {
            player.sendMessage(ChatColor.GREEN + "You arrived at spot.");
            bossBar.removeAll();
            if(taskId > -1)
                Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
