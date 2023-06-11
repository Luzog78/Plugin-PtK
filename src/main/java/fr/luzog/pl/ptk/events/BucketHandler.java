package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GPermissions;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.game.GTeam;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import java.util.Objects;

public class BucketHandler {

    @Events.Event
    public static void onEmpty(PlayerBucketEmptyEvent e) {
        GManager gm = GManager.getCurrentGame();
        GPlayer fp;
        if (gm == null || (fp = gm.getPlayer(e.getPlayer().getName(), false)) == null || fp.getTeam() == null) {
            e.setCancelled(true);
            return;
        }

        if ((fp.getManager().getState() == GManager.State.PAUSED
                && !Objects.equals(fp.getTeamId(), GTeam.GODS_ID))
                || !fp.hasPermission(Events.specialMat.contains(e.getBucket()) ? GPermissions.Type.PLACESPE :
                GPermissions.Type.PLACE, Utils.normalize(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation()))
                || fp.getManager().getTeams().stream().anyMatch(t -> t.isInside(e.getBlockClicked().getLocation())
                && !Objects.equals(t.getId(), fp.getTeamId()) && !Objects.equals(fp.getTeamId(), GTeam.GODS_ID))) {
            e.setCancelled(true);
            return;
        }
    }

    @Events.Event
    public static void onFill(PlayerBucketFillEvent e) {
        GManager gm = GManager.getCurrentGame();
        GPlayer fp;
        if (gm == null || (fp = gm.getPlayer(e.getPlayer().getName(), false)) == null || fp.getTeam() == null) {
            e.setCancelled(true);
            return;
        }

        if ((fp.getManager().getState() == GManager.State.PAUSED
                && !Objects.equals(fp.getTeamId(), GTeam.GODS_ID))
                || !fp.hasPermission(Events.specialMat.contains(e.getBucket()) ? GPermissions.Type.BREAKSPE :
                GPermissions.Type.BREAK, Utils.normalize(e.getBlockClicked().getRelative(e.getBlockFace()).getLocation()))
                || fp.getManager().getTeams().stream().anyMatch(t -> t.isInside(e.getBlockClicked().getLocation())
                && !Objects.equals(t.getId(), fp.getTeamId()) && !Objects.equals(fp.getTeamId(), GTeam.GODS_ID))) {
            e.setCancelled(true);
            return;
        }
    }

}
