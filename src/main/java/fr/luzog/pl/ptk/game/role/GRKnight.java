package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
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
        super.setDaysRunnable(2, players -> {
            players.forEach(player -> {
                player.addWaitingItem(true,
                        Items.builder(Material.CHAINMAIL_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build(),
                        Items.builder(Material.CHAINMAIL_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build(),
                        Items.builder(Material.CHAINMAIL_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build(),
                        Items.builder(Material.CHAINMAIL_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).build(),
                        Items.builder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1).build(),
                        new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 8), new ItemStack(Material.COOKED_BEEF, 8));
            });
        });
        super.setAbility1(Items.builder(Material.FEATHER)
                .setName("§7Capacité - §6Promptitude Constante")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Le §7Chevalier§r est toujours hâté d'aider son roi.\n" +
                                        "Il se doit, en cas de problème, de se précipiter pour le sauver peu importe la situation.\n"
                                        + " \n"
                                        + "Il a donc pour cela l'effet §9Rapidité §9I§r de manière §7permanante§r pour le soutenir dans sa tâche.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility2(Items.builder(Material.STONE_PICKAXE)
                .setName("§7Capacité - §6Kit Belliqueux")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Ce kit est l'équivalent d'un kit de §7combat§r classique.\n"
                                        + "Il permet une resistance moyenne pour le début de jeu.\n"
                                        + " \n"
                                        + "Le joueur obtient alors :\n"
                                        + "  §8>§r §e1§r casque en cotte de maille §dProtection §dI§r.\n"
                                        + "  §8>§r §e1§r plastron en cotte de maille §dProtection §dI§r.\n"
                                        + "  §8>§r §e1§r jambières en cotte de maille §dProtection §dI§r.\n"
                                        + "  §8>§r §e1§r bottes en cotte de maille §dProtection §dI§r.\n"
                                        + "  §8>§r §e1§r épée en fer §dTranchant §dI§r.\n"
                                        + "  §8>§r §e1§r arc.\n"
                                        + "  §8>§r §e8§r flèches.\n"
                                        + "  §8>§r §e8§r steaks.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }
}
