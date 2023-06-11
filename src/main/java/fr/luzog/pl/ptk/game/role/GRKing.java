package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class GRKing extends GRole {
    public static class Info extends GRole.Info {
        public Info() {
            super(Roles.KING);
        }

        public static Info fromMap(Map<String, Object> map) {
            return new Info();
        }
    }

    public GRKing() {
        super("King", "The king is the leader of the team. He has to protect his team and kill the other kings.");
        super.setHealthModifier(40);
        super.setEnchantLimit(Enchantment.DAMAGE_ALL, 4);
        super.addPermaEffects(new Utils.PermaEffect(PotionEffectType.NIGHT_VISION, 0, false),
                new Utils.PermaEffect(PotionEffectType.FIRE_RESISTANCE, 0, false),
                new Utils.PermaEffect(PotionEffectType.REGENERATION, 0, false));
    }
}
