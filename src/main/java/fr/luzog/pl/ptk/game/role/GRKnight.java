package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Material;
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
        super.setAbility1(Items.builder(Material.FEATHER)
                .setName("§7Capacité - §6Promptitude Constante")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Le §7Chevalier§r est toujours hâté d'aider son roi.\n" +
                                        "Il se doit, en cas de problème, de se précipiter pour le sauver peu importe la situation.\n"
                                        + " \n"
                                        + "Il a donc pour cela l'effet §9Rapidité I§r de manière §7permanante§r pour le soutenir dans sa tâche.\n",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }
}
