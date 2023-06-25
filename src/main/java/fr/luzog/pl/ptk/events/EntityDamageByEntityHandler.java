package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GPermissions;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.game.GTeam;
import fr.luzog.pl.ptk.game.role.*;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

public class EntityDamageByEntityHandler {

    @Events.Event
    public static void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            String damager;
            if(event.getDamager() instanceof Player) {
                GPlayer fp = GManager.getCurrentGame() == null ? null
                        : GManager.getCurrentGame().getPlayer(event.getDamager().getName(), false);
                damager = "§f" + (fp == null ? ((Player) event.getDamager()).getDisplayName() : fp.getDisplayName());
            } else {
                Utils.Pair<String, String> pair = Events.entitiesFrenchNames.get(event.getDamager().getType());
                damager = "§7" + pair.getKey() + " " + pair.getValue();
            }
            player.setMetadata(Events.lastDamagerMeta, new FixedMetadataValue(Main.instance, damager));
        }

        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();

            if (EntityDamageHandler.spawnProtected.contains(p.getName())) {
                EntityDamageHandler.spawnProtected.remove(p.getName());
                p.sendMessage(EntityDamageHandler.spawnProtectionExpirationMessage);
            }

            GPlayer fp = GManager.getCurrentGame() == null ? null : GManager.getCurrentGame().getPlayer(p.getName(), false);

            if (event.getEntity() instanceof Player) {
                Player e = (Player) event.getEntity();
                GPlayer fe = GManager.getCurrentGame() == null ? null : GManager.getCurrentGame().getPlayer(e.getName(), false);

                if (fe == null || fp == null) {
                    if (!p.isOp() || p.getGameMode() != GameMode.CREATIVE) {
                        event.setCancelled(true);
                        return;
                    }
                } else if (!fp.hasPermission(fp.getTeam().getPlayers().contains(fe) ?
                        GPermissions.Type.FRIENDLY_FIRE : GPermissions.Type.PVP, e.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            } else {
                if ((fp == null && !p.isOp())
                        || (fp != null && !fp.hasPermission(GPermissions.Type.MOBS,
                        event.getEntity().getLocation()))) {
                    event.setCancelled(true);
                    return;
                }

                event.getEntity().setMetadata(Events.lastDamageLootingLevelTag,
                        new FixedMetadataValue(Main.instance, p.getItemInHand()
                                .getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)));
                event.getEntity().setMetadata(Events.lastDamageSilkTouchTag,
                        new FixedMetadataValue(Main.instance, p.getItemInHand()
                                .getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0));
            }

            if (fp != null) {
                fp.getStats().increaseDamageDealt(event.getFinalDamage());
                if (event.getEntity() instanceof Player && event.getCause() == EntityDamageByEntityEvent.DamageCause.PROJECTILE)
                    fp.getStats().increaseArrowsHit();
                if (event.getEntity() instanceof Player && !EntityDamageHandler.spawnProtected.contains(event.getEntity().getName())
                        && ((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0) {
                    fp.getStats().increaseKills();
                    fp.saveStats();
                    if (fp.getRoleInfo() instanceof GRWizzard.Info) {
                        ((GRWizzard) fp.getRoleInfo().getRoleType().getRole()).onKill(fp);
                    }
                    if (fp.getRoleInfo() instanceof GRFreebooter.Info) {
                        GPlayer fe = GManager.getCurrentGame() == null ? null : GManager.getCurrentGame().getPlayer(event.getEntity().getName(), false);
                        ((GRFreebooter) fp.getRoleInfo().getRoleType().getRole()).onKill(fp, fe);
                    }
                }
            }
        }
    }

}
