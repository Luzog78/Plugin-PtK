package fr.luzog.pl.ptk.commands.Fk;

import fr.luzog.pl.fkx.utils.CmdUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class FKCTitle {
    public static final String syntaxe = "/fk";

    public static boolean onCommand(CommandSender sender, Command command, String msg, String[] args) {
        CmdUtils u = new CmdUtils(sender, command, msg, args, syntaxe);

        u.send("§6", "Commande pas encore disponible... Possiblement pour cause de.. flemme.. oui, probable '^^"
                + "\n Mais surement présente dans les prochaines versions...  §axp");

        return false;
    }

    public static List<String> onTabComplete(CommandSender sender, Command command, String msg, String[] args) {
        return new ArrayList<>();
    }
}
