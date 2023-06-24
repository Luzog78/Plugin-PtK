package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GRSquire extends GRole {
    public static class Info extends GRole.Info {
        private boolean isKnight;

        public Info() {
            super(Roles.SQUIRE);
            this.isKnight = false;
        }

        public Info(boolean isKnight) {
            super(Roles.SQUIRE);
            this.isKnight = isKnight;
        }

        public static Info fromMap(Map<String, Object> map) {
            return new Info(Boolean.parseBoolean(map.get("is-knight") + ""));
        }

        public LinkedHashMap<String, Object> toMap() {
            LinkedHashMap<String, Object> map = super.toMap();
            map.put("is-knight", isKnight);
            return map;
        }

        public boolean isKnight() {
            return this.isKnight;
        }

        public void setKnight(boolean isKnight, @Nullable Player playerToGiveStuffTo) {
            this.isKnight = isKnight;
            if (playerToGiveStuffTo != null) {
                playerToGiveStuffTo.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD))
                        .forEach((i, itemStack) -> playerToGiveStuffTo.getWorld().dropItem(playerToGiveStuffTo.getLocation(), itemStack));
            }
        }
    }

    public GRSquire() {
        super("Écuyer", "L'écuyer, garde du roi et fier aspirant à la fonction de chevalier, " +
                "fait tout son possible pour faire accroître sa force afin d'en devenir un.");
        super.setBase(Heads.MISC_CROWN_ICON_PURPLE.getSkull());
        super.setHealth(20);
        super.setArmorLimit(0b1001_0000_0000_0000);
        super.setEnchantLimit(Enchantment.DAMAGE_ALL, 2);
        super.setAbility1(Items.builder(Heads.MISC_CROWN_ICON_BLACK.getSkull())
                .setName("§7Capacité - §6Montée en grade")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Je joueur peux devenir §7Chevalier§r en tuant un joueur de l'équipe adverse.\n"
                                        + " \n"
                                        + "Il obtient alors :\n"
                                        + "  §8>§r L'effet §cForce I§r de manière §9permanente§r.\n"
                                        + "  §8>§r Une §bépée en diamant§r avec les enchantements §dSharpness III§r et §dUnbreaking III§r.\n"
                                        + "  §8>§r Toutes les caractéristiques du §7Chevalier§r (y compris §4coeurs§r, §barmure§r, §deffets§r, etc).",
                                32).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }

    @Override
    public void tick(GRole.Info roleInfo, Player p) {
        if (roleInfo instanceof GRSquire.Info) {
            GRSquire.Info info = (GRSquire.Info) roleInfo;

            if (info.isKnight()) {

                info.setArmorLimit(Roles.KNIGHT.getRole().getArmorLimit());
                info.setHealthModifier(Roles.KNIGHT.getRole().getHealth() - roleInfo.getRoleType().getRole().getHealth());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 260, 0, false, false), true);
                        new ArrayList<>(Roles.KNIGHT.getRole().getPermaEffects()).forEach(e ->
                                p.addPotionEffect(e.toPotionEffect(260), true));
                    }
                }.runTask(Main.instance);

            }
        }

        super.tick(roleInfo, p);
    }
}
