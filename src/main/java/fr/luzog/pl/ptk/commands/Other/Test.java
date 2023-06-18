package fr.luzog.pl.ptk.commands.Other;

import fr.luzog.pl.ptk.game.role.GRole;
import fr.luzog.pl.ptk.guis.GuiRoles;
import fr.luzog.pl.ptk.utils.CmdUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Test implements CommandExecutor, TabCompleter {

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        CmdUtils u = new CmdUtils(sender, cmd, msg, args, "/test");

        u.getPlayer().openInventory(GuiRoles.getRoleInventory(GRole.Roles.KING, null, "test", null));

        u.succ("Test!");

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
        return new ArrayList<>();
    }

}
