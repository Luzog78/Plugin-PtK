package fr.luzog.pl.ptk.commands.Game;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GOptions;
import fr.luzog.pl.ptk.guis.GuiActivations;
import fr.luzog.pl.ptk.utils.CmdUtils;
import fr.luzog.pl.ptk.utils.SpecialChars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GCActivations {
    public static final String syntaxe = "/" + Main.CMD + " activations [help | list | check | <act> [get | <act> set (on | off | <day>)]]";

    public static boolean onCommand(CommandSender sender, Command command, String msg, String[] args) {
        CmdUtils u = new CmdUtils(sender, command, msg, args, syntaxe);

        if (args.length == 0)
            return false;

        if (args.length == 1)
            if (sender instanceof Player)
                u.getPlayer().openInventory(GuiActivations.getMainInventory(Main.CMD));
            else
                u.err(CmdUtils.err_not_player);

        else if (args[1].equalsIgnoreCase("help") || args[1].equals("?"))
            u.synt();

        else if (args[1].equalsIgnoreCase("list")) {
            u.succ("Activations :");
            for (GOptions.GOption opt : GManager.getCurrentGame().getOptions().getOptions())
                u.succ(" - " + format(opt));
        } else if (args[1].equalsIgnoreCase("check")) {
            GManager.getCurrentGame().checkActivations(true);
            u.succ("Les activations sont à jour.");
            u.succ("Activations :");
            for (GOptions.GOption opt : GManager.getCurrentGame().getOptions().getOptions())
                u.succ(" - " + format(opt));
        } else {
            GOptions.GOption opt = args[1].equalsIgnoreCase("pvp") ? GManager.getCurrentGame().getOptions().getPvp()
                    : args[1].equalsIgnoreCase("nether") ? GManager.getCurrentGame().getOptions().getNether()
                    : args[1].equalsIgnoreCase("assaults") ? GManager.getCurrentGame().getOptions().getAssaults()
                    : args[1].equalsIgnoreCase("end") ? GManager.getCurrentGame().getOptions().getEnd() : null;
            GuiActivations.OptionType optType = args[1].equalsIgnoreCase("pvp") ? GuiActivations.OptionType.PVP
                    : args[1].equalsIgnoreCase("nether") ? GuiActivations.OptionType.NETHER
                    : args[1].equalsIgnoreCase("assaults") ? GuiActivations.OptionType.ASSAULTS
                    : args[1].equalsIgnoreCase("end") ? GuiActivations.OptionType.END : null;
            if (opt == null || optType == null) {
                u.err("Cette activation n'existe pas.");
                return false;
            }
            if (args.length == 2)
                if (sender instanceof Player)
                    u.getPlayer().openInventory(GuiActivations.getOptionInventory(optType.getMaterial(), opt, Main.CMD + " activations"));
                else
                    u.err(CmdUtils.err_not_player);
            else if (args[2].equalsIgnoreCase("get"))
                u.succ("Activation :\n§r - " + format(opt));
            else if (args.length >= 4 && args[2].equalsIgnoreCase("set")) {
                if (args[3].equalsIgnoreCase("on"))
                    opt.activate(true);
                else if (args[3].equalsIgnoreCase("off"))
                    opt.deactivate(true);
                else
                    try {
                        opt.setActivationDay(Integer.parseInt(args[3]));
                        GManager.getCurrentGame().checkActivations(true);
                    } catch (NumberFormatException e) {
                        u.err(CmdUtils.err_number_format);
                        return false;
                    }
                u.succ("Activation :\n§r - " + format(opt));
                GManager.getCurrentGame().saveOptions();
            } else
                u.synt();
        }

        return false;
    }

    public static String format(GOptions.GOption opt) {
        return opt.getName() + "§r :  " + (opt.isActivated() ? "§2" + SpecialChars.YES : "§c" + SpecialChars.NO) + "  §7§o(Jour " + opt.getActivationDay() + ")";
    }

    public static List<String> onTabComplete(CommandSender sender, Command command, String msg, String[] args) {
        return new ArrayList<String>() {{
            if (args.length == 2) {
                add("list");
                add("pvp");
                add("assaults");
                add("nether");
                add("end");
            } else if (args.length == 3) {
                add("get");
                add("set");
            } else if (args.length == 4 && (args[2].equalsIgnoreCase("get") || args[2].equalsIgnoreCase("set"))) {
                add("on");
                add("off");
                for (int i = 0; i <= 6; i++)
                    add((i + GManager.getCurrentGame().getDay()) + "");
            }
        }};
    }
}
