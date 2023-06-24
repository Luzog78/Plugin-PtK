package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class GRKnight extends GRole {
    public static class Info extends GRole.Info {
        public Info() {
            super(Roles.KNIGHT);
        }

        public static GRKnight.Info fromMap(Map<String, Object> map) {
            return new GRKnight.Info();
        }
    }

    public GRKnight() {
        super("Chevalier", "Le chevalier est le garde du corps du roi. Il doit protéger son roi et tuer les autres rois.");
        super.setBase(Heads.MISC_CROWN_ICON_BLACK.getSkull());
        super.setHealth(24);
        super.addPermaEffects(new Utils.PermaEffect(PotionEffectType.SPEED, 0, false));
    }
}
