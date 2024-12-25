package polatrk.saveSpot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveSpotTabCompleter implements TabCompleter {

    private final SaveSpot plugin;

    public SaveSpotTabCompleter(SaveSpot plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return plugin.getValidActions();

        if(args[0].equals("cancel"))
            return new ArrayList<>();

        if (args.length == 2)
            return Arrays.asList("public", "private");

        if(args.length == 3)
            return List.of("<SpotName>");

        return new ArrayList<>();
    }
}
