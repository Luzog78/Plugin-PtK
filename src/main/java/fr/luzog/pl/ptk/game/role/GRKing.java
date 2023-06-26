package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Material;
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
        super("Roi ⚜", "Le Roi est le leader de l'équipe. Il doit protéger son équipe et tuer les autres rois.");
        super.setBase(Heads.MISC_CROWN_ICON_YELLOW.getSkull());
        super.setHealth(40);
        super.setArmorLimit(0b0000_0000_0000_0000);
        super.setEnchantLimit(Enchantment.DAMAGE_ALL, 4);
        super.addPermaEffects(new Utils.PermaEffect(PotionEffectType.NIGHT_VISION, 0, false),
                new Utils.PermaEffect(PotionEffectType.FIRE_RESISTANCE, 0, false),
                new Utils.PermaEffect(PotionEffectType.REGENERATION, 0, false));
        super.setAbility1(Items.builder(Material.DIAMOND_CHESTPLATE)
                .setName("§7Capacité - §6Grâce Divine")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Le §6Roi §6⚜§r, pour rester en sécurité, a été gracié par les dieux de moultes effets.\n"
                                        + " \n"
                                        + "Il possède donc les effets :\n"
                                        + "  §8>§r §1Vision §1Nocture§r de manière §7permanente§r.\n"
                                        + "  §8>§r §4Régénération §4I§r de manière §7permanente§r.\n"
                                        + "  §8>§r §6Résistance §6au §6Feu§r de manière §7permanente§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility2(Items.builder(Material.BED)
                .setName("§7Capacité - §6Réanimation")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Tant que le §6Roi §6⚜§r est §e§nen §e§nvie§r, les joueurs de son équipe peuvent subir " +
                                        "des dommages sans crainte car à leur mort, ils se font §d§oréanimer§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }
}
