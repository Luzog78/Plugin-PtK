package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.commands.Cheat.Freeze;
import fr.luzog.pl.ptk.utils.CustomNBT;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.text.DecimalFormat;

public class PlayerInteractEntityHandler {

    @Events.Event
    public static void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        if (!(event.getRightClicked() instanceof LivingEntity))
            return;
        LivingEntity e = (LivingEntity) event.getRightClicked();

        if (new CustomNBT(p.getItemInHand()).getBoolean("Mjolnir") && !p.isSneaking()) {
            p.getWorld().strikeLightning(e.getLocation());
            return;
        }
    }

}
