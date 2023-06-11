package fr.luzog.pl.ptk.commands.Fun;

import fr.luzog.pl.ptk.utils.CmdUtils;
import fr.luzog.pl.ptk.utils.Color;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Head implements CommandExecutor, TabCompleter {
    public static final String syntaxe = "/head [<owner>[#<notes>] | -b <base64>]";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        CmdUtils u = new CmdUtils(sender, cmd, msg, args, syntaxe);

        if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equals("?"))
            u.synt();

        else {
            ItemStack is;
            if (args[0].equalsIgnoreCase("-b") && args.length >= 2) {
                is = Heads.getCustomSkull(args[1]);
            } else {
                is = Heads.getSkullOf(args[0].contains("#") ? args[0].split("#")[0] : args[0]);
            }
            if (sender instanceof Player) {
                if (u.getPlayer().getInventory().firstEmpty() != -1) {
                    u.getPlayer().getInventory().addItem(is);
                    u.succ("Vous avez récupéré une tête !");
                } else {
                    u.err(CmdUtils.err_inventory_full);
                }
            } else if (sender instanceof BlockCommandSender) {
                BlockCommandSender bcs = (BlockCommandSender) sender;
                Location loc = bcs.getBlock().getLocation().clone().add(0, 1, 0);
                loc.getWorld().dropItem(loc, is);
                System.out.println(Color.GREEN + "Une tête a été drop ! " + Color.BLACK + "(" + Color.WHITE
                        + Utils.locToString(loc, true, false, true) + Color.BLACK + ")" + Color.RESET);
            } else {
                u.err(CmdUtils.err_not_player);
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
        ArrayList<String> temp = new ArrayList<>(new HashSet<String>() {{
            if(args.length == 1) {
                add("-b");
                addAll(Arrays.stream(Heads.values()).filter(h -> !h.isCustom()).map(h ->
                        h.getData() + "#" + h.getName().replace(" ", "_")).collect(Collectors.toList()));
            } else if (args.length == 2) {
                addAll(Arrays.stream(Heads.values()).filter(Heads::isCustom)
                        .map(Heads::getData).collect(Collectors.toList()));
            }
        }});
        temp.removeIf(s -> !s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()));
        return temp;
    }
}
