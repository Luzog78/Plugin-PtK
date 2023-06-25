package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.events.Events;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class GRWizzard extends GRole {
    public static class Info extends GRole.Info {
        public Info() {
            super(Roles.WIZZARD);
        }

        public static GRWizzard.Info fromMap(Map<String, Object> map) {
            return new GRWizzard.Info();
        }
    }

    public GRWizzard() {
        super("Sorcier", "Le sorcier, magicien du feu, est le premier personnage à être en capacité de concocter des potions. Utilisez son alambic pour prendre l'avantage !");
        super.setBase(Heads.HEAD_MAGE.getSkull());
        super.addPermaEffects(new Utils.PermaEffect(PotionEffectType.FIRE_RESISTANCE, 0, false));
        super.setDaysRunnable(2, (players) -> {
            players.forEach((player) -> {
                player.addWaitingItem(true, new ItemStack(Material.BREWING_STAND_ITEM), new ItemStack(Material.POTION, 1, (short) 8226),
                        new ItemStack(Material.POTION, 1, (short) 8226), new ItemStack(Material.POTION, 1, (short) 16421),
                        new ItemStack(Material.POTION, 1, (short) 16421), new ItemStack(Material.POTION, 1, (short) 16421));
            });
        });
        super.setAbility1(Items.builder(Material.BREWING_STAND_ITEM)
                .setName("§7Capacité - §6Kit Alchimiste")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Dès le premier jour, le §7Sorcier§r reçoit son kit d'alchimie.\n"
                                        + " \n"
                                        + "Ce kit représente un bel avantage en début de jeu (en tous cas, avant l'ouverture du nether)...\n"
                                        + " \n"
                                        + "Le joueur obtient alors :\n"
                                        + "  §8>§r §e1§r alambic.\n"
                                        + "  §8>§r §e2§r potions de §aVitesse §aII§r buvables.\n"
                                        + "  §8>§r §e3§r potions de §4Guérison §4II§r jetables.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility2(Items.builder(Material.GOLD_SWORD)
                .setName("§7Capacité - §6Mort Dorée")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Quand le §7Sorcier§r tue un joueur, il a §e50%§r de chances de recevoir §e3§r §6pommes §6en §6or§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility3(Items.builder(Material.BLAZE_POWDER)
                .setName("§7Capacité - §6Ignis Fugere")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Maître du feu, le §7Sorcier§r a développé au cours des millénaires une capacité à s'§6ignifuger§r.\n"
                                        + " \n"
                                        + "Il se voit donc doté d'un effet de §6Résistance §6au §6Feu§r de manière §7permanante§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }

    @Events.Event
    public void onKill(GPlayer killer) {
        try {
            if (Math.random() < 0.5) {
                killer.addWaitingItem(true, new ItemStack(Material.GOLDEN_APPLE),
                        new ItemStack(Material.GOLDEN_APPLE), new ItemStack(Material.GOLDEN_APPLE));
            }
        } catch (Exception ignored) {
        }
    }
}
