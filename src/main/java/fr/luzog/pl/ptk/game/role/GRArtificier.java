package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.events.Events;
import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class GRArtificier extends GRole {
    public static class Info extends GRole.Info {

        public Info() {
            super(Roles.ARTIFICIER);
        }

        public static GRArtificier.Info fromMap(Map<String, Object> map) {
            return new Info();
        }
    }

    public GRArtificier() {
    super("Artificier", "Expert dans l'art de la pyrotechnie, l'artificier "
                + "est également expert dans l'art de la destruction. Il est le seul à pouvoir utiliser les explosifs TNT "
                + "et représente donc un vrai atout.");
        super.setBase(Heads.HEAD_CREEPER_WITH_TNT.getSkull());
        super.removeMaterialLimit(Material.TNT);
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
        super.setAbility1(Items.builder(Material.TNT)
                .setName("§7Capacité - §6Kit Explosif")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Le saint Graal de la pyrotechnie, voilà ce que présente le kit explosif.\n"
                                        + " \n"
                                        + "Le joueur obtient alors :\n"
                                        + "  §8>§r §e1§r briquet §dSolidité §dIII§r.\n"
                                        + "  §8>§r §e2§r §4TNTs§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility2(Items.builder(Material.FIREBALL)
                .setName("§7Capacité - §6Bombarda (niv. 3)")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Les flèches tirées par l'§7Artificier§r ont §e20%§r de chance de devenir des §6boules §6de §6feu§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }

    @Events.Event
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player && Math.random() < 0.20) {
            Player player = (Player) e.getEntity();
            player.launchProjectile(Fireball.class);
            e.setProjectile(null);
        }
    }
}
