package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.events.Events;
import fr.luzog.pl.ptk.events.InventoryClickHandler;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.guis.Guis;
import fr.luzog.pl.ptk.utils.CustomNBT;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

public class GRWitch extends GRole {
    public static class Info extends GRole.Info {
        private int brewedPotions;

        public Info() {
            super(Roles.WITCH);
            this.brewedPotions = 0;
        }

        public Info(int brewedPotions) {
            super(Roles.WITCH);
            this.brewedPotions = brewedPotions;
        }

        public static GRWitch.Info fromMap(Map<String, Object> map) {
            try {
                return new Info(Integer.parseInt(map.get("brewed-potions") + ""));
            } catch (Exception e) {
                return new Info();
            }
        }

        public LinkedHashMap<String, Object> toMap() {
            LinkedHashMap<String, Object> map = super.toMap();
            map.put("brewed-potions", brewedPotions);
            return map;
        }

        public int getBrewedPotions() {
            return this.brewedPotions;
        }

        public void setBrewedPotions(int brewedPotions) {
            this.brewedPotions = brewedPotions;
        }

        public void increaseBrewedPotions() {
            this.brewedPotions++;
        }

        public void decreaseBrewedPotions() {
            this.brewedPotions--;
        }
    }

    public static final String WITCHED_TAG = "witched";

    public static final List<PotionEffectType> MALUSES = Arrays.asList(
            PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.HARM, PotionEffectType.CONFUSION,
            PotionEffectType.BLINDNESS, PotionEffectType.HUNGER, PotionEffectType.WEAKNESS, PotionEffectType.POISON
            // PotionEffectType.WITHER ->> Too OP
    );

    /**
     * Put on the item the {@link #WITCHED_TAG}
     *
     * @param is The item
     * @return The item with the tag on
     */
    public static ItemStack witcherize(ItemStack is) {
        return new CustomNBT(is).setBoolean(WITCHED_TAG, true).build();
    }

    public GRWitch() {
        super("Sorcière", "Le sorcière, magicienne des abîmes des forêts et marécages, est malgré ses capacités physiques réduites, "
                + "bien plus puissante qu'elle n'y paraît. A base de potions de malus, elle domine facilement ses ennemis.");
        super.setBase(Heads.HEAD_WITCH.getSkull());
        super.setHealth(16);
        super.setPotionLimit(PotionEffectType.INCREASE_DAMAGE, 0);
        super.setPotionLimit(PotionEffectType.HARM, 1);
        super.setDaysRunnable(2, (players) -> {
            players.forEach((player) -> {
                // 2x 16388: Poison I throwable
                // 2x 16424: Weakness I throwable
                // 1x 16428: Instant Damage II throwable
                // 1x 16385: Regeneration I throwable
                // 1x  8201: Strength I drinkable
                player.addWaitingItem(true, witcherize(new ItemStack(Material.POTION, 1, (short) 16388)),
                        witcherize(new ItemStack(Material.POTION, 1, (short) 16388)),
                        witcherize(new ItemStack(Material.POTION, 1, (short) 16424)),
                        witcherize(new ItemStack(Material.POTION, 1, (short) 16424)),
                        witcherize(new ItemStack(Material.POTION, 1, (short) 16428)),
                        witcherize(new ItemStack(Material.POTION, 1, (short) 16385)),
                        witcherize(new ItemStack(Material.POTION, 1, (short) 8201)));
            });
        });
        super.setAbility1(Items.builder(Material.FERMENTED_SPIDER_EYE)
                .setName("§7Capacité - §6Kit Mortifiant")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Dès le premier jour, la §7Sorcière§r reçoit de quoi annihiler ses ennemis.\n"
                                        + " \n"
                                        + "Ce kit, fait pour le milieu de jeu, représente un réel danger, couplé aux autres habilités du rôle.\n"
                                        + " \n"
                                        + "Le joueur obtient alors :\n"
                                        + "  §8>§r §e2§r potions de §2Poison I§r jetables.\n"
                                        + "  §8>§r §e2§r potions de §0Faiblesse I§r jetables.\n"
                                        + "  §8>§r §e1§r potion de §4Dégâts II§r jetable.\n"
                                        + "  §8>§r §e1§r potion de §dRégénération I§r jetable.\n"
                                        + "  §8>§r §e1§r potion de §cForce I§r buvable.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility2(Items.builder(Material.DISPENSER)
                .setName("§7Capacité - §6Maintien de Distance")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Quand la §7Sorcière§r concocte une potion de malus, celle-ci en devient §7jetable§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
        super.setAbility3(Items.builder(Material.SPECKLED_MELON)
                .setName("§7Capacité - §6Jamais 2 sans 3")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §f" + Utils.breakLines(
                                "Maîtresse de l'art subtile des potions, toutes les §92 potions§r de malus concoctées, "
                                        + "une §6troisième§r est créée avec un effet §4malus aléatoire§r.",
                                ABILITY_LINE_LENGTH).replace("\n", "\n  §r").replace("§r", "§f"),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .build());
    }

    @Events.Event
    public void onClick(InventoryClickEvent e, GPlayer gp) {
        if (e.getClickedInventory().getType() != InventoryType.BREWING || !(gp.getRoleInfo() instanceof Info)) {
            return;
        }
        Info info = gp.getRoleInfoAs(Info.class);

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() != Material.POTION) {
            return;
        }

        Potion potion = Potion.fromItemStack(item);

        boolean malus = false;
        for (PotionEffect effect : potion.getEffects()) {
            if (MALUSES.contains(effect.getType())) {
                malus = true;
                break;
            }
        }
        if (!malus) {
            return;
        }

//        if (item.getDurability() < 16384) {
//            item.setDurability((short) (item.getDurability() + 8192));
//            e.setCurrentItem(item);
//        }
        if (!potion.isSplash()) {
            potion.setSplash(true);
            item = potion.toItemStack(item.getAmount());
            e.setCurrentItem(item);
        }

        CustomNBT nbt = new CustomNBT(item);
        if (!nbt.hasKey(WITCHED_TAG) || !nbt.getBoolean(WITCHED_TAG)) {
            nbt.setBoolean(WITCHED_TAG, true);
            item = nbt.build();
            e.setCurrentItem(item);
            info.increaseBrewedPotions();
            gp.saveRoleInfo();
            if (info.getBrewedPotions() % 2 == 0) {
                ItemStack newItem = new ItemStack(Material.POTION, 1, (short) 16384);
                PotionMeta newMeta = (PotionMeta) newItem.getItemMeta();
                newMeta.setDisplayName("§4Potion de Malus");
                PotionEffectType type = MALUSES.get(new Random().nextInt(MALUSES.size()));
                int duration = new Random().nextInt(901) + 300;
                newMeta.addCustomEffect(new PotionEffect(type, duration, 0), true);
                newItem.setItemMeta(newMeta);
                gp.addWaitingItem(true, witcherize(newItem));
            }
        }
    }
}
