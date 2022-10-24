package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.game.GTeam;
import fr.luzog.pl.ptk.utils.CustomNBT;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Objects;

public class PlayerInteractHandler {

    @Events.Event
    public static void onInteract(PlayerInteractEvent e) {
        GPlayer fp;
        if (GManager.getCurrentGame() == null
                || (fp = GManager.getCurrentGame().getPlayer(e.getPlayer().getName(), false)) == null)
            return;

        if (GManager.getCurrentGame().getState() != GManager.State.RUNNING
                && !Objects.equals(fp.getTeamId(), GTeam.GODS_ID)) {
            e.setCancelled(true);
            return;
        }

        Action a = e.getAction();
        Player p = e.getPlayer();

        if (a == Action.RIGHT_CLICK_BLOCK && e.hasBlock())
            if (e.getClickedBlock().getType() == Material.TNT
                    && !GManager.getCurrentGame().getOptions().getAssaults().isActivated()) {
                p.sendMessage("§cLes assauts ne sont pas activés.");
                e.setCancelled(true);
                return;
            } else if (e.getClickedBlock().getType() == Material.CHEST) {
                if (Objects.equals(fp.getTeamId(), GTeam.SPECS_ID)
                        && p.getGameMode() != org.bukkit.GameMode.SPECTATOR) {
                    p.sendMessage("§cVous ne pouvez pas ouvrir de coffre.");
                    e.setCancelled(true);
                    return;
                }
                boolean sneak = e.getPlayer().isSneaking();
                Block b = e.getClickedBlock();
                Location l = Utils.normalize(b.getLocation(), true);

                for (GTeam t : GManager.getCurrentGame().getTeams())
                    if (t.isInside(b.getLocation()))
                        if (Objects.equals(fp.getTeamId(), GTeam.GODS_ID)
                                || Objects.equals(fp.getTeamId(), t.getId())) {
                            break;
                        } else if (!GManager.getCurrentGame().getOptions().getAssaults().isActivated()) {
                            p.sendMessage("§7§oVous ouvrez un coffre §6" + t.getName()
                                    + "§7§o, prenez garde avant le début des assauts...");
                            break;
                        } else {
                            p.sendMessage("§cVous ne pouvez pas ouvrir le coffre car il appartient à l'équipe §6" + t.getName() + "§c.");
                            e.setCancelled(true);
                            return;
                        }
            }

        fp.getStats().increaseInteractions();

        if (!e.hasItem())
            return;

        ItemStack is = e.getItem();
        CustomNBT nbt = new CustomNBT(is);

        if (nbt.hasKey(Events.canInteractTag) && !nbt.getBoolean(Events.canInteractTag)) {
            e.setCancelled(true);
            p.sendMessage(Main.PREFIX + "§cImpossible d'interagir avec l'item.");
            return;
        }

        if (nbt.getBoolean("Mjolnir") && !p.isSneaking()) {
            p.getWorld().strikeLightning(p.getTargetBlock(new HashSet<Material>() {{
                add(Material.AIR);
            }}, 300).getLocation().add(0.0, 1, 0.0));
            return;
        }
    }

}
