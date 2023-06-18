package fr.luzog.pl.ptk.commands.Game;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.role.GRole;
import fr.luzog.pl.ptk.guis.GuiRoles;
import fr.luzog.pl.ptk.utils.CmdUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GCRoles {
    public static final String syntaxe = "/" + Main.CMD + " roles [<role> | -p <page>]";

    public static boolean onCommand(CommandSender sender, Command command, String msg, String[] args) {
        CmdUtils u = new CmdUtils(sender, command, msg, args, syntaxe);

        if (args.length == 0)
            return false;

        else if (sender instanceof Player) {
            if(args.length == 1) {
                Bukkit.dispatchCommand(sender, Main.CMD + " roles -p 0");
            } else if (args[1].equalsIgnoreCase("-p")) {
                if (args.length == 2) {
                    Bukkit.dispatchCommand(sender, Main.CMD + " roles -p 0");
                } else {
                    try {
                        int page = Integer.parseInt(args[2]);
                        u.getPlayer().openInventory(GuiRoles.getMainInventory(
                                Main.CMD, Main.CMD + " roles -p", page));
                    } catch (NumberFormatException e) {
                        u.err(CmdUtils.err_number_format + " (" + args[2] + ")");
                    }
                }
            } else {
                GRole.Roles r = GRole.Roles.fromId(args[1]);
                if (r != null) {
                    u.getPlayer().openInventory(GuiRoles.getRoleInventory(r, null,
                            Main.CMD + " roles " + r.getId(), Main.CMD + " roles"));
                } else {
                    u.err("Rôle '" + args[1] + "' introuvable.");
                }
            }
        } else {
            u.err(CmdUtils.err_not_player);
        }

        return false;
    }

    public static List<String> onTabComplete(CommandSender sender, Command command, String msg, String[] args) {
        return new ArrayList<String>() {{
            if (args.length == 2) {
                for (GRole.Roles r : GRole.Roles.values()) {
                    add(r.getId());
                }
            }
        }};
    }
}
