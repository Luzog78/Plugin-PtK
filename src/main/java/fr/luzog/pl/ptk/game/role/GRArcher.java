package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.events.Events;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
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

import java.util.*;

public class GRArcher extends GRole {
    public static class Info extends GRole.Info {

        public Info() {
            super(Roles.ARCHER);
        }

        public static GRArcher.Info fromMap(Map<String, Object> map) {
            return new Info();
        }
    }

    public GRArcher() {
        super("Archer", "L'archer se bat à distance, sa force stratégique repose donc sur ses capacités de défense.");
        super.setBase(Heads.HEAD_CURSED_SKELETON.getSkull());
        super.setEnchantLimit(Enchantment.ARROW_KNOCKBACK, 1);
        super.setEnchantLimit(Enchantment.ARROW_INFINITE, 1);
        super.setDaysRunnable(2, (players) -> {
            players.forEach((player) -> {
                ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
                meta.addStoredEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
                book.setItemMeta(meta);
                player.addWaitingItem(true, new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 32), book);
            });
        });
        super.setAbility1(Items.builder(Material.BOW)
                .setName("§7Capacité - §6Kit de l'Hirondelle")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Ce kit, grâce à sa longue portée, permet à l'§7archer§r de toujours garder un oeil, "
                                        + "tel une épée de Damoclès, sur ses ennemis.\n"
                                        + " \n"
                                        + "Le joueur obtient alors :\n"
                                        + "  §8>§r §e1§r arc.\n"
                                        + "  §8>§r §e32§r flèches.\n"
                                        + "  §8>§r §e1§r livre §dFrappe I§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility2(Items.builder(Material.FIREBALL)
                .setName("§7Capacité - §6Inflamare")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Les flèches tirées par l'§7archer§r ont §e30%§r de chance de §6s'enflammer§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility3(Items.builder(Material.FEATHER)
                .setName("§7Capacité - §6Léger comme la plume")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Léger comme la plume, l'§7archer§r peut tomber sans nulle crainte.\n"
                                        + "Il ne subit §3aucun §3dégât §3de §3chute§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility4(Items.builder(Material.WATCH)
                .setName("§7Capacité - §6Aisance nocturne")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Durant §9la §9nuit§r, l'§7archer§r se déplace bien plus facilement.\n"
                                        + "Il obtient alors §aVitesse §aI§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }

    @Override
    public void tick(GRole.Info roleInfo, GPlayer gp) {
        super.tick(roleInfo, gp);
        Player p = gp.getPlayer();
        if (p != null && gp.getManager().getTime() >= 12000) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 260, 0, false, false), true);
                }
            }.runTask(Main.instance);
        }
    }

    @Events.Event
    public void onShoot(EntityShootBowEvent e) {
//        if (e.getEntity() instanceof Player) {
//            Player player = (Player) e.getEntity();
//            player.launchProjectile(org.bukkit.entity.Fireball.class);
//        }

        if (Math.random() < 0.30) {
            e.getProjectile().setFireTicks(200);
        }
    }

    @Events.Event
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }
}
