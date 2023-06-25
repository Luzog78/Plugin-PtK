package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.events.Events;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.SpecialChars;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

public class GRFreebooter extends GRole {
    public static class Info extends GRole.Info {

        public Info() {
            super(Roles.FREEBOOTER);
        }

        public static GRFreebooter.Info fromMap(Map<String, Object> map) {
            return new Info();
        }
    }

    public GRFreebooter() {
        super("Flibustier", "Le flibustier, bandit ou encore brigand, sait profiter de ses victoires ! "
                + "Il sait se faufiler et la chute de ses ennemis ne fait que le rendre plus fort. "
                + "Mais il doit faire attention à son égo pour ne pas finir à terre rapidement...");
        super.setBase(Heads.HEAD_BANDIT.getSkull());
        super.addPermaEffects(new Utils.PermaEffect(PotionEffectType.SPEED, 0, false));
        super.setAbility1(Items.builder(Material.FEATHER)
                .setName("§7Capacité - §6Promptitude Constante")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Le §7Flibustier§r est toujours hâté d'aider son roi.\n" +
                                        "Il se doit, en cas de problème, de se précipiter pour le sauver peu importe la situation.\n"
                                        + " \n"
                                        + "Il a donc pour cela l'effet §9Rapidité §9I§r de manière §7permanante§r pour le soutenir dans sa tâche.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility2(Items.builder(Material.EXP_BOTTLE)
                .setName("§7Capacité - §6Prise de Main")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "A chaque tuerie qu'il entreprend, le §7Flibustier§r gagne en aisance et en confiance.\n"
                                        + " \n"
                                        + "  §8>§r Sa vie §aaugmente§r de §2+0.5❤§r §7/kill§r.\n"
                                        + " \n"
                                        + "  §8" + SpecialChars.MISC_1 + "§r La vie ne peu pas dépasser §c15.0❤§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility3(Items.builder(Material.RAW_FISH)
                .setDurability((short) 3)
                .setName("§7Capacité - §6Chute d'Égo")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Mais s'il advient que le §7Flibustier§r n'arrive pas à entreprendre le massacre de ses rêves, "
                                        + "sa volonté de vivre s'ébranlera...\n"
                                        + " \n"
                                        + "  §8>§r Sa vie §cdiminue§r de §4-1.0❤§r §7/mort§r.\n"
                                        + "  §8>§r Sa vie §cdiminue§r de §4-2.0❤§r §7/friendly §7kill§r.\n"
                                        + " \n"
                                        + "  §8" + SpecialChars.MISC_1 + "§r La vie ne peu pas être inférieur à §c3.0❤§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }

    @Events.Event
    public void onDeath(GPlayer gp) {
        if (gp.getRoleInfo() != null) {
            double modifier = gp.getRoleInfo().getHealthModifier() - 2;
            if (modifier < -14) {
                modifier = -14;
            }
            gp.getRoleInfo().setHealthModifier(modifier);
        }
    }

    @Events.Event
    public void onKill(GPlayer killer, @Nullable GPlayer killed) {
        if (killer.getRoleInfo() != null) {
            double modifier = killer.getRoleInfo().getHealthModifier();
            if (killed == null || !Objects.equals(killer.getTeamId(), killed.getTeamId())) {
                modifier += 1;
            } else {
                modifier -= 4;
            }
            if (modifier < -14) {
                modifier = -14;
            }
            if (modifier > 10) {
                modifier = 10;
            }
            killer.getRoleInfo().setHealthModifier(modifier);
        }
    }
}
