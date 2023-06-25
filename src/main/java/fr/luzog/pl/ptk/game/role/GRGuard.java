package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class GRGuard extends GRole {
    public static class Info extends GRole.Info {
        public Info() {
            super(Roles.GUARD);
        }

        public static GRGuard.Info fromMap(Map<String, Object> map) {
            return new GRGuard.Info();
        }
    }

    public GRGuard() {
        super("Garde", "Le rôle de garde propose un kit amélioré offrant une bonne résistance dès le début de jeu.");
        super.setBase(Heads.HEAD_LOW_CRACKLED_IRON_GOLEM.getSkull());
        super.addPermaEffects(new Utils.PermaEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, false));
        super.setAbility1(Items.builder(Material.IRON_PICKAXE)
                .setName("§7Capacité - §6Kit Robuste")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Ce kit est l'équivalent d'un kit de base §7amélioré§r.\n"
                                        + "Il permet une bonne resistance précoce dès le début de jeu.\n"
                                        + " \n"
                                        + "Le joueur obtient alors :\n"
                                        + "  §8>§r §e1§r casque en fer.\n"
                                        + "  §8>§r §e1§r plastron en fer.\n"
                                        + "  §8>§r §e1§r jambières en fer.\n"
                                        + "  §8>§r §e1§r bottes en fer.\n"
                                        + "  §8>§r §e1§r épée en fer.\n"
                                        + "  §8>§r §e1§r pioche en fer.\n"
                                        + "  §8>§r §e1§r hache en fer.\n"
                                        + "  §8>§r §e1§r pelle en fer.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility2(Items.builder(Material.IRON_CHESTPLATE)
                .setName("§7Capacité - §6Effet Golem")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Le §7garde§r, afin de bien tanker, se voit garant d'un effet de "
                                        + "§6Résistance §6I§r de manière §7permanente§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility3(Items.builder(Material.DAYLIGHT_DETECTOR)
                .setName("§7Capacité - §6Aisance diurne")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Durant §ele §ejour§r, le §7garde§r se déplace bien plus facilement.\n"
                                        + "Il obtient alors §aVitesse §aI§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }
}
