package polatrk.saveSpot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveSpotTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return Arrays.asList("remove", "save", "show", "goto");

        if (args.length == 2)
            return Arrays.asList("public", "private");

        if(args.length == 3)
            return List.of("<SpotName>");

        return new ArrayList<>();
    }
}
