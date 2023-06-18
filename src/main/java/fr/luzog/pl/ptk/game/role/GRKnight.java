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
        super("Knight", "The knight is the king's bodyguard. He has to protect his king and kill the other kings.");
        super.setBase(Heads.MISC_CROWN_ICON_BLACK.getSkull());
        super.setHealthModifier(24);
        super.addPermaEffects(new Utils.PermaEffect(PotionEffectType.SPEED, 0, false));
    }
}
