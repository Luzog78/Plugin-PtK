package fr.luzog.pl.ptk.commands.Other;

import fr.luzog.pl.ptk.game.ZRole;
import fr.luzog.pl.ptk.utils.CmdUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Test implements CommandExecutor, TabCompleter {

    boolean b = false;

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        CmdUtils u = new CmdUtils(sender, cmd, msg, args, "/test");

        (b ? ZRole.WITCH.getKit() : ZRole.WIZARD.getKit()).forEach(i -> u.getPlayer().getInventory().addItem(i.build()));
        b = !b;

        u.succ("Test!");

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
        return new ArrayList<>();
    }

}
