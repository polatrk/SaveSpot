package polatrk.saveSpot;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;

public final class SaveSpot extends JavaPlugin {

    public ArrayList<CoordInfo> savedSpots = new ArrayList<>();
    private SaveSpotCommandHandler commandHandler;
    @Override
    public void onEnable() {
        loadSavedSpots();
        commandHandler = new SaveSpotCommandHandler(this);
        getCommand("savespot").setExecutor(commandHandler);
        getCommand("savespot").setTabCompleter(new SaveSpotTabCompleter());
    }

    @Override
    public void onDisable() {
        saveSavedSpots();

        Bukkit.getScheduler().cancelTasks(this);

        commandHandler.removeAllActiveBossBars();
    }

    private void loadSavedSpots() {
        try (FileInputStream fileIn = new FileInputStream("savedSpots.dat");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            savedSpots = (ArrayList<CoordInfo>) in.readObject();
        } catch (Exception e) {
            savedSpots = new ArrayList<>();
            Bukkit.getLogger().info("Failed to load savedCoords: " + e);
        }
    }

    private void saveSavedSpots() {
        try (FileOutputStream fileOut = new FileOutputStream("savedSpots.dat");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(savedSpots);
        } catch (Exception e) {
            Bukkit.getLogger().info("Failed to save savedCoords: " + e);
        }
    }
}
