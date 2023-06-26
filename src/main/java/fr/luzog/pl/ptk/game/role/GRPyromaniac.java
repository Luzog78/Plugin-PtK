package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.events.Events;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class GRPyromaniac extends GRole {
    public static class Info extends GRole.Info {

        public Info() {
            super(Roles.PYROMANIAC);
        }

        public static GRPyromaniac.Info fromMap(Map<String, Object> map) {
            return new Info();
        }
    }

    public GRPyromaniac() {
        super("Pyromane", "Le pyromane est un fou de la flamme. Il aime voir ses ennemis se consumer. "
                + "Sa maîtrise du feu lui permet de repousser des hordes d'ennemis sans aucune difficulté.");
        super.setBase(Heads.HEAD_SHOCKED_BLAZE.getSkull());
        super.setEnchantLimit(Enchantment.FIRE_ASPECT, 1);
        super.setEnchantLimit(Enchantment.ARROW_FIRE, 1);
        super.addPermaEffects(new Utils.PermaEffect(PotionEffectType.FIRE_RESISTANCE, 0, false));
        super.setDaysRunnable(2, (players) -> {
            players.forEach((player) -> {
                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
                meta.addStoredEnchant(Enchantment.FIRE_ASPECT, 1, true);
                book.setItemMeta(meta);
                ItemStack flintAndSteel = new ItemStack(Material.FLINT_AND_STEEL);
                flintAndSteel.addEnchantment(Enchantment.DURABILITY, 3);
                flintAndSteel = new CustomNBT(flintAndSteel).set(GRole.ALLOWED_ITEM_TAG, true).build();
                player.addWaitingItem(true, book, flintAndSteel,
                        new ItemStack(Material.LAVA_BUCKET), new ItemStack(Material.LAVA_BUCKET));
            });
        });
        super.setAbility1(Items.builder(Material.FLINT_AND_STEEL)
                .setName("§7Capacité - §6Kit Inflammable")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Parfait pour un §7Pyromane§r, le kit inflammable propose une belle aide initiatique à la pyromanie.\n"
                                        + " \n"
                                        + "Le joueur obtient alors :\n"
                                        + "  §8>§r §e1§r livre §dAura §dde §dFeu §dI§r.\n"
                                        + "  §8>§r §e1§r briquet §dSolidité §dIII§r.\n"
                                        + "  §8>§r §e2§r seaux de lave.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility2(Items.builder(Material.FIREBALL)
                .setName("§7Capacité - §6Parum Ignis (niv. 1)")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Les flèches tirées par le §7Pyromane§r ont §e10%§r de chance de §6s'enflammer§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility3(Items.builder(Material.BLAZE_ROD)
                .setName("§7Capacité - §6Aura Flamboyante")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Les coups portés par le §7Pyromane§r ont §e10%§r de chance d'§6enflammer§r.\n"
                                        + " \n"
                                        + "  §8" + SpecialChars.MISC_1 + "§r L'entité touchée subira les effets d'une "
                                        + "§6Aura §6de §6Feu §6I½§r (elle brûlera pendant §c6 §csecondes§r).",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility4(Items.builder(Material.BLAZE_POWDER)
                .setName("§7Capacité - §6Ignis Fugere")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Maître de son élément, le §7Pyromane§r a développé une capacité à s'§6ignifuger§r.\n"
                                        + " \n"
                                        + "Il se voit donc doté d'un effet de §6Résistance §6au §6Feu§r de manière §7permanante§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }

    @Events.Event
    public void onShoot(EntityShootBowEvent e) {
        if (Math.random() < 0.10) {
            e.getProjectile().setFireTicks(200);
        }
    }

    @Events.Event
    public void onDamageAnEntity(EntityDamageByEntityEvent e) {
        if (!e.isCancelled() && Math.random() < 0.10) {
            e.getEntity().setFireTicks(120);
        }
    }
}
