package polatrk.saveSpot.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import polatrk.saveSpot.GlobalUtils;
import polatrk.saveSpot.SaveSpot;
import polatrk.saveSpot.SaveSpotCommandHandler;

public class CancelCommand extends BaseCommand{
    private SaveSpotCommandHandler commandHandler;

    public CancelCommand(SaveSpot plugin) {
        super(plugin);
    }

    public CancelCommand(SaveSpot plugin, SaveSpotCommandHandler commandHandler) {
        super(plugin);
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(Player player, String[] args) {
        commandHandler.cancelGotoForPlayer(player);
        GlobalUtils.sendSaveSpotMessage(player, ChatColor.GREEN + "Destination cancelled.");
    }
}
