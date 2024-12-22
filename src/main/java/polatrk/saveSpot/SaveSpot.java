package polatrk.saveSpot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SaveSpot extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("powering up");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("shuttingdown");
    }
}
