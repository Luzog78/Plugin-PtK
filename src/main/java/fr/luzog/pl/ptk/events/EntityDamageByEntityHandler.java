package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GPermissions;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.game.GTeam;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class EntityDamageByEntityHandler implements Listener {

    @EventHandler
    public static void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();

            GPlayer fp = GManager.getCurrentGame() == null ? null : GManager.getCurrentGame().getPlayer(p.getName(), false);
            /*
             *  FKTeam t;
             *  if (FKManager.getCurrentGame() != null && event.getEntity().hasMetadata(FKTeam.GUARDIAN_TAG)
             *          && fp != null && fp.getTeam() != null && (t = FKManager.getCurrentGame().getTeam(event.getEntity()
             *          .getMetadata(FKTeam.GUARDIAN_TAG).get(0).asString())) != null
             *          && (fp.getTeam().getId().equals(t.getId()) || fp.getTeam().getId().equals(FKTeam.GODS_ID))
             *          && p.isSneaking()) {
             *      t.setChestsRoom(t.getSpawn(), true, true);
             *      p.sendMessage("§aVous avez réinitialisé la position de votre gardien.");
             *      return;
             *  }
             */

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
                if ((event.getEntity().hasMetadata(GTeam.PLUNDER_STAND_TAG) && ((fp == null && !p.isOp())
                        || (fp != null && (fp.getTeam() == null || !fp.getTeam().getId().equals(GTeam.GODS_ID)))))
                        || (!event.getEntity().hasMetadata(GTeam.PLUNDER_STAND_TAG) && ((fp == null && !p.isOp())
                        || (fp != null && !fp.hasPermission(GPermissions.Type.MOBS,
                        event.getEntity().getLocation()))))) {
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
                if (event.getEntity() instanceof Player && ((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0)
                    fp.getStats().increaseKills();
            }
        }
    }

}
