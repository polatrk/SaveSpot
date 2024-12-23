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
        if (args.length == 1) {
            return Arrays.asList("save", "remove", "modify");
        }

        if (args.length == 2) {
            return Arrays.asList("private", "public");
        }

        if (args.length == 3) {
            return List.of("<name>");
        }

        return new ArrayList<>();
    }
}
