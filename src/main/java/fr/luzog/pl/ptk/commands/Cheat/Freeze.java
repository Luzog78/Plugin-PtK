package fr.luzog.pl.ptk.commands.Cheat;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.events.Events;
import fr.luzog.pl.ptk.utils.CmdUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Freeze implements CommandExecutor, TabCompleter {
    public static final String syntaxe = "/freeze [(on | off)] <players...>";

    public static List<String> frozen = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        CmdUtils u = new CmdUtils(sender, cmd, msg, args, syntaxe);

        if (args.length == 0 || args[0].equals("?") || args[0].equalsIgnoreCase("help"))
            u.synt();
        else
            CmdUtils.getPlayersFromArray(args, args[0].equalsIgnoreCase("on")
                    || args[0].equalsIgnoreCase("off") ? 1 : 0).forEach(player -> {
                boolean f = args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off") ?
                        args[0].equalsIgnoreCase("on") : !isFrozen(player.getName());
                freeze(player.getName(), f);
                u.succ("§6" + player.getDisplayName(), "§rest à présent§9", (f ? "gelé" : "décongelé"), "§r!");
            });

        return false;
    }

    public static boolean isFrozen(String name) {
        for (String s : frozen)
            if (s.equalsIgnoreCase(name))
                return true;
        return false;
    }

    public static void freeze(String name, boolean frozen) {
        Freeze.frozen.removeIf(s -> s.equalsIgnoreCase(name));
        if (frozen)
            Freeze.frozen.add(name);
    }

    public static void frozenWarning(Player player) {
        player.sendMessage(Main.PREFIX + "§cVous ne pouvez pas effectuer cette action.");
    }

    @Events.Event
    public static void onPlayerMove(PlayerMoveEvent e) {
        if (isFrozen(e.getPlayer().getName()) && (e.getFrom().getX() != e.getTo().getX()
                || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getFrom().getZ())) {
            frozenWarning(e.getPlayer());
            e.getPlayer().teleport(e.getFrom());
        }
    }

    @Events.Event
    public static void onPlayerInteract(PlayerInteractEvent e) {
        if (isFrozen(e.getPlayer().getName())) {
            frozenWarning(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Events.Event
    public static void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        if (isFrozen(e.getPlayer().getName())) {
            frozenWarning(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Events.Event
    public static void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if (isFrozen(e.getPlayer().getName())) {
            frozenWarning(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Events.Event
    public static void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (isFrozen(e.getPlayer().getName())) {
            frozenWarning(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Events.Event
    public static void onPlayerDropItem(PlayerDropItemEvent e) {
        if (isFrozen(e.getPlayer().getName())) {
            frozenWarning(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Events.Event
    public static void onPlayerBedEnter(PlayerBedEnterEvent e) {
        if (isFrozen(e.getPlayer().getName())) {
            frozenWarning(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Events.Event
    public static void onBucketEmpty(PlayerBucketEmptyEvent e) {
        if (isFrozen(e.getPlayer().getName())) {
            frozenWarning(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Events.Event
    public static void onBucketFill(PlayerBucketFillEvent e) {
        if (isFrozen(e.getPlayer().getName())) {
            frozenWarning(e.getPlayer());
            e.setCancelled(true);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
        return new ArrayList<String>() {{
            new ArrayList<String>() {{
                addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
                if (args.length == 1)
                    addAll(Arrays.asList("on", "off"));
            }}.forEach(p -> {
                if (p.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                    add(p);
            });
        }};
    }

}
