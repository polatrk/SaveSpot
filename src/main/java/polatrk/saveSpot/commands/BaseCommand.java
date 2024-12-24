package polatrk.saveSpot.commands;

import org.bukkit.entity.Player;
import polatrk.saveSpot.SaveSpot;

public abstract class BaseCommand {
    protected final SaveSpot plugin;

    public BaseCommand(SaveSpot plugin) {
        this.plugin = plugin;
    }

    public abstract void execute(Player player, String[] args);
}
