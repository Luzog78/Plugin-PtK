package fr.luzog.pl.ptk.commands.Fk;

import fr.luzog.pl.fkx.fk.FKManager;
import fr.luzog.pl.fkx.fk.FKOptions;
import fr.luzog.pl.fkx.fk.GUIs.GuiActivations;
import fr.luzog.pl.fkx.utils.CmdUtils;
import fr.luzog.pl.fkx.utils.SpecialChars;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FKCActivations {
    public static final String syntaxe = "/fk activations [help | list | check | <act> [get | <act> set (on | off | <day>)]]";

    public static boolean onCommand(CommandSender sender, Command command, String msg, String[] args) {
        CmdUtils u = new CmdUtils(sender, command, msg, args, syntaxe);

        if (args.length == 0)
            return false;

        if (args.length == 1)
            if (sender instanceof Player)
                u.getPlayer().openInventory(GuiActivations.getMainInventory("fk"));
            else
                u.err(CmdUtils.err_not_player);

        else if (args[1].equalsIgnoreCase("help") || args[1].equals("?"))
            u.synt();

        else if (args[1].equalsIgnoreCase("list")) {
            u.succ("Activations :");
            for (FKOptions.FKOption opt : FKManager.getCurrentGame().getOptions().getOptions())
                u.succ(" - " + format(opt));
        } else if (args[1].equalsIgnoreCase("check")) {
            FKManager.getCurrentGame().checkActivations(true);
            u.succ("Les activations sont à jour.");
            u.succ("Activations :");
            for (FKOptions.FKOption opt : FKManager.getCurrentGame().getOptions().getOptions())
                u.succ(" - " + format(opt));
        } else {
            FKOptions.FKOption opt = args[1].equalsIgnoreCase("pvp") ? FKManager.getCurrentGame().getOptions().getPvp()
                    : args[1].equalsIgnoreCase("nether") ? FKManager.getCurrentGame().getOptions().getNether()
                    : args[1].equalsIgnoreCase("assaults") ? FKManager.getCurrentGame().getOptions().getAssaults()
                    : args[1].equalsIgnoreCase("end") ? FKManager.getCurrentGame().getOptions().getEnd() : null;
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
                    u.getPlayer().openInventory(GuiActivations.getOptionInventory(optType.getMaterial(), opt, "fk activations"));
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
                        FKManager.getCurrentGame().checkActivations(true);
                    } catch (NumberFormatException e) {
                        u.err(CmdUtils.err_number_format);
                        return false;
                    }
                u.succ("Activation :\n§r - " + format(opt));
                FKManager.getCurrentGame().saveOptions();
            } else
                u.synt();
        }

        return false;
    }

    public static String format(FKOptions.FKOption opt) {
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
                    add((i + FKManager.getCurrentGame().getDay()) + "");
            }
        }};
    }
}
