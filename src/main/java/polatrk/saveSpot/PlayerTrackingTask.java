package polatrk.saveSpot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
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

        double distanceCurrentToTarget = CoordInfo.Vector3.Distance(currentPlayerPos, targetInfo.coords);
        double distanceStartToTarget = CoordInfo.Vector3.Distance(startPlayerPos, targetInfo.coords);
        double progress = Math.max(0.0, Math.min(1.0, 1 - (distanceCurrentToTarget / distanceStartToTarget)));

        String coordsDist = String.valueOf(Math.abs(currentPlayerPos.x - targetInfo.coords.x)) + " " +
                            String.valueOf(Math.abs(currentPlayerPos.y - targetInfo.coords.y)) + " " +
                            String.valueOf(Math.abs(currentPlayerPos.z - targetInfo.coords.z));


        double desiredDirection = calculateYaw(currentPlayerPos, targetInfo.coords);
        double difference = player.getLocation().getYaw() - desiredDirection;
        if (difference > 180) {
            difference -= 360;
        } else if (difference < -180) {
            difference += 360;
        }

        String direction = "error";
        if (difference <= 22.5 && difference >= -22.5)
            direction = "↑";
        else if (difference > 22.5 && difference <= 67.5)
            direction = "↖";
        else if (difference > 67.5 && difference <= 112.5)
            direction = "←";
        else if (difference > 112.5 && difference <= 157.5)
            direction = "↙";
        else if (difference > 157.5 || difference <= -157.5)
            direction = "↓";
        else if (difference > -157.5 && difference <= -112.5)
            direction = "↘";
        else if (difference > -112.5 && difference <= -67.5)
            direction = "→";
        else if (difference > -67.5 && difference <= -22.5)
            direction = "↗";



        String barTitle = ChatColor.GOLD + "Direction to "
                + ChatColor.AQUA + targetInfo.spotName + ": "
                + ChatColor.YELLOW + coordsDist
                + ChatColor.GRAY + " | "
                + ChatColor.YELLOW + ChatColor.BOLD + direction;

        bossBar.setColor(progress >= 0.33 ? (progress >= 0.66 ? BarColor.GREEN : BarColor.YELLOW) : BarColor.RED);

        bossBar.setTitle(barTitle);
        bossBar.setProgress(progress);

        if(CoordInfo.Vector3.Distance(currentPlayerPos, targetInfo.coords) < 10.0) {
            GlobalUtils.sendSaveSpotMessage(player, ChatColor.GREEN + "You arrived at spot.");
            bossBar.removeAll();
            if(taskId > -1)
                Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    public static double calculateYaw(CoordInfo.Vector3 currentPos, CoordInfo.Vector3 targetPos) {
        double dx = targetPos.x - currentPos.x;
        double dz = targetPos.z - currentPos.z;

        double yaw = Math.toDegrees(Math.atan2(-dx, dz));

        if (yaw < -180) {
            yaw += 360;
        } else if (yaw > 180) {
            yaw -= 360;
        }

        return yaw;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
