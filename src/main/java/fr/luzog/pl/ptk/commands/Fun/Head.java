package fr.luzog.pl.ptk.commands.Fun;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.guis.GuiHeads;
import fr.luzog.pl.ptk.utils.CmdUtils;
import fr.luzog.pl.ptk.utils.Color;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Bukkit;
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
    public static final String syntaxe = "/head [-self | -gui | <owner>[#<notes>] | -b <base64>]";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        CmdUtils u = new CmdUtils(sender, cmd, msg, args, syntaxe);

        if (args.length == 0 || args[0].equalsIgnoreCase("help") || args[0].equals("?"))
            Bukkit.dispatchCommand(sender, msg + " -gui");

        else if (args[0].equalsIgnoreCase("-self")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                ItemStack is = Heads.getSkullOf(p.getName());
                p.getInventory().addItem(is).forEach((i, it) ->
                        p.getWorld().dropItem(p.getLocation(), it));
            } else {
                u.err(CmdUtils.err_not_player);
            }
        } else if (args[0].equalsIgnoreCase("-gui")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    Bukkit.dispatchCommand(sender, msg + " -gui 0");
                } else {
                    try {
                        int page = Integer.parseInt(args[1]);
                        p.openInventory(GuiHeads.getMainInventory("null", "head -gui", page));
                    } catch (NumberFormatException e) {
                        u.err(CmdUtils.err_number_format + " (" + args[1] + ")");
                    }
                }
            } else {
                u.err(CmdUtils.err_not_player);
            }
        } else {
            ItemStack is;
            if (args[0].equalsIgnoreCase("-b") && args.length >= 2) {
                is = Heads.getCustomSkull(args[1]);
            } else {
                is = Heads.getSkullOf(args[0].contains("#") ? args[0].split("#")[0] : args[0]);
            }
            if (sender instanceof Player) {
                u.getPlayer().getInventory().addItem(is).forEach((i, it) ->
                        u.getPlayer().getWorld().dropItem(u.getPlayer().getLocation(), it));
                u.succ("Vous avez récupéré une tête !");
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
            if (args.length == 1) {
                add("-self");
                add("-gui");
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
