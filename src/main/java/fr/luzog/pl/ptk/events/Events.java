package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.InCaseThereIsAProblem;
import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.commands.Cheat.Freeze;
import fr.luzog.pl.ptk.commands.Utils.InputGUIAndTools;
import fr.luzog.pl.ptk.game.*;
import fr.luzog.pl.ptk.utils.*;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Events implements Listener {

    public static @interface Event {
    }

    public static final double STILL_Y_VEL_CONSTANT = -0.0784000015258789;

    public static final String canInteractTag = "canInteract";
    public static final String cantClickOnTag = "cantClickOn";
    public static final String closeTag = "closeInventory";
    public static final String exeLeftTag = "executeLeftCommand";
    public static final String exeShiftLeftTag = "executeShiftLeftCommand";
    public static final String exeRightTag = "executeRightCommand";
    public static final String exeShiftRightTag = "executeShiftRightCommand";
    public static final String exeMiddleTag = "executeMiddleCommand";
    public static final String lastDamageLootingLevelTag = "lastDamageLootingLevel";
    public static final String lastDamageSilkTouchTag = "lastDamageSilkTouch";

    public static final String lastDamagerMeta = "lastDamager";

    public static List<Material> specialMat = Arrays.asList(Material.TNT, Material.TORCH, Material.REDSTONE_TORCH_ON,
            Material.REDSTONE_TORCH_OFF, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.WHEAT, /*Material.HAY_BLOCK,*/
            Material.SEEDS, Material.MELON_SEEDS, Material.PUMPKIN_SEEDS, Material.CARROT, Material.CARROT_ITEM,
            Material.POTATO, Material.POTATO_ITEM, Material.FIRE, Material.FLINT_AND_STEEL, Material.BUCKET,
            Material.WATER, Material.WATER_BUCKET, Material.LAVA, Material.LAVA_BUCKET, Material.WORKBENCH,
            Material.SIGN, Material.SIGN_POST, Material.WALL_SIGN, Material.SOIL
            /*Material.FURNACE, Material.ANVIL, Material.ENCHANTMENT_TABLE*/);
    public static List<Material> unbreakableMat = Collections.singletonList(Material.MOB_SPAWNER);
    public static List<Material> unplaceableMat = new ArrayList<>();

    public static List<Material> diamond = Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.DIAMOND_SWORD, Material.DIAMOND_AXE,
            Material.DIAMOND_PICKAXE, Material.DIAMOND_SPADE, Material.DIAMOND_HOE);
    public static List<Material> gold = Arrays.asList(Material.GOLD_HELMET, Material.GOLD_CHESTPLATE,
            Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.GOLD_SWORD, Material.GOLD_AXE,
            Material.GOLD_PICKAXE, Material.GOLD_SPADE, Material.GOLD_HOE);
    public static List<Material> iron = Arrays.asList(Material.IRON_HELMET, Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.IRON_SWORD, Material.IRON_AXE,
            Material.IRON_PICKAXE, Material.IRON_SPADE, Material.IRON_HOE);
    public static List<Material> leather = Arrays.asList(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.STONE_SWORD, Material.STONE_AXE,
            Material.STONE_PICKAXE, Material.STONE_SPADE, Material.STONE_HOE);

    public static Map<EntityType, Utils.Pair<String, String>> entitiesFrenchNames =
            new HashMap<EntityType, Utils.Pair<String, String>>() {{
                put(EntityType.BAT, new Utils.Pair<>("une", "Chauve-souris"));
                put(EntityType.BLAZE, new Utils.Pair<>("un", "Blaze"));
                put(EntityType.CAVE_SPIDER, new Utils.Pair<>("une", "Araignée de cave"));
                put(EntityType.CHICKEN, new Utils.Pair<>("un", "Poulet"));
                put(EntityType.COW, new Utils.Pair<>("une", "Vache"));
                put(EntityType.CREEPER, new Utils.Pair<>("un", "Creeper"));
                put(EntityType.ENDER_DRAGON, new Utils.Pair<>("le", "Dragon de l'End"));
                put(EntityType.ENDERMAN, new Utils.Pair<>("un", "Enderman"));
                put(EntityType.ENDERMITE, new Utils.Pair<>("une", "Endermite"));
                put(EntityType.GHAST, new Utils.Pair<>("un", "Ghast"));
                put(EntityType.GIANT, new Utils.Pair<>("un", "Géant"));
                put(EntityType.GUARDIAN, new Utils.Pair<>("un", "Gardien"));
                put(EntityType.HORSE, new Utils.Pair<>("un", "Cheval"));
                put(EntityType.IRON_GOLEM, new Utils.Pair<>("un", "Golem de fer"));
                put(EntityType.MAGMA_CUBE, new Utils.Pair<>("un", "Cube de magma"));
                put(EntityType.MUSHROOM_COW, new Utils.Pair<>("une", "Vache champignon"));
                put(EntityType.OCELOT, new Utils.Pair<>("un", "Ocelot"));
                put(EntityType.PIG, new Utils.Pair<>("un", "Cochon"));
                put(EntityType.PIG_ZOMBIE, new Utils.Pair<>("un", "Cochon Zombie"));
                put(EntityType.SHEEP, new Utils.Pair<>("un", "Mouton"));
                put(EntityType.SILVERFISH, new Utils.Pair<>("un", "Poisson argenté"));
                put(EntityType.SKELETON, new Utils.Pair<>("un", "Squelette"));
                put(EntityType.SLIME, new Utils.Pair<>("un", "Slime"));
                put(EntityType.SNOWMAN, new Utils.Pair<>("un", "Bonhomme de neige"));
                put(EntityType.SPIDER, new Utils.Pair<>("une", "Araignée"));
                put(EntityType.SQUID, new Utils.Pair<>("un", "Calmar"));
                put(EntityType.VILLAGER, new Utils.Pair<>("un", "Villageois"));
                put(EntityType.WITCH, new Utils.Pair<>("une", "Sorcière"));
                put(EntityType.WITHER, new Utils.Pair<>("un", "Wither"));
                put(EntityType.WOLF, new Utils.Pair<>("un", "Loup"));
                put(EntityType.ZOMBIE, new Utils.Pair<>("un", "Zombie"));

                put(EntityType.ARMOR_STAND, new Utils.Pair<>("un", "Mannequin"));
                put(EntityType.ARROW, new Utils.Pair<>("une", "Flèche"));
                put(EntityType.BOAT, new Utils.Pair<>("un", "Bateau"));
                put(EntityType.COMPLEX_PART, new Utils.Pair<>("une", "Partie complexe"));
                put(EntityType.DROPPED_ITEM, new Utils.Pair<>("un", "Item"));
                put(EntityType.EGG, new Utils.Pair<>("un", "Oeuf"));
                put(EntityType.ENDER_CRYSTAL, new Utils.Pair<>("un", "Cristal d'ender"));
                put(EntityType.ENDER_SIGNAL, new Utils.Pair<>("un", "Signal d'ender"));
                put(EntityType.ENDER_PEARL, new Utils.Pair<>("une", "Perle d'ender"));
                put(EntityType.EXPERIENCE_ORB, new Utils.Pair<>("une", "Orbe d'expérience"));
                put(EntityType.FALLING_BLOCK, new Utils.Pair<>("un", "Bloc tombant"));
                put(EntityType.FIREBALL, new Utils.Pair<>("une", "Boule de feu"));
                put(EntityType.FIREWORK, new Utils.Pair<>("un", "Feu d'artifice"));
                put(EntityType.FISHING_HOOK, new Utils.Pair<>("un", "Hameçon"));
                put(EntityType.ITEM_FRAME, new Utils.Pair<>("un", "Cadre d'item"));
                put(EntityType.LEASH_HITCH, new Utils.Pair<>("", "Attache"));
                put(EntityType.LIGHTNING, new Utils.Pair<>("un", "Éclair"));
                put(EntityType.MINECART, new Utils.Pair<>("un", "Train"));
                put(EntityType.MINECART_CHEST, new Utils.Pair<>("un", "Train avec coffre"));
                put(EntityType.MINECART_COMMAND, new Utils.Pair<>("un", "Train avec commande"));
                put(EntityType.MINECART_FURNACE, new Utils.Pair<>("un", "Train avec four"));
                put(EntityType.MINECART_HOPPER, new Utils.Pair<>("un", "Train avec hopper"));
                put(EntityType.MINECART_MOB_SPAWNER, new Utils.Pair<>("un", "Train avec spawner"));
                put(EntityType.MINECART_TNT, new Utils.Pair<>("un", "Train avec TNT"));
                put(EntityType.PAINTING, new Utils.Pair<>("un", "Tableau"));
                put(EntityType.PRIMED_TNT, new Utils.Pair<>("une", "TNT"));
                put(EntityType.SMALL_FIREBALL, new Utils.Pair<>("une", "Petite boule de feu"));
                put(EntityType.SNOWBALL, new Utils.Pair<>("une", "Boule de neige"));
                put(EntityType.SPLASH_POTION, new Utils.Pair<>("une", "Potion"));
                put(EntityType.THROWN_EXP_BOTTLE, new Utils.Pair<>("une", "Bouteille d'expérience"));
                put(EntityType.WEATHER, new Utils.Pair<>("la", "Météo"));
                put(EntityType.WITHER_SKULL, new Utils.Pair<>("un", "Crâne de wither"));

                put(EntityType.PLAYER, new Utils.Pair<>("un", "Joueur"));
            }};

    public static List<BlockLootsItem> breakBlockLoots = new ArrayList<BlockLootsItem>() {{
//        add(new BlockLootsItem(Arrays.asList(Material.LOG, Material.LOG_2), false, new Loots().add(new ItemStack(Material.LOG))));
//        add(new BlockLootsItem(Collections.singletonList(Material.WOOD), false, new Loots().add(new ItemStack(Material.WOOD))));
//        add(new BlockLootsItem(Collections.singletonList(Material.STONE), false, new Loots()
//                .add(new ItemStack(Material.STONE), -1, true)
//                .add(new ItemStack(Material.COBBLESTONE), -1, false)
//        ));
//        add(new BlockLootsItem(Collections.singletonList(Material.COBBLESTONE), false, new Loots()
//                .add(new ItemStack(Material.STONE), -1, true)
//                .add(new ItemStack(Material.COBBLESTONE), -1, false)
//        ));
//        add(new BlockLootsItem(Collections.singletonList(Material.SAND), false, new Loots().add(new ItemStack(Material.SAND))));
//        add(new BlockLootsItem(Collections.singletonList(Material.SANDSTONE), false, new Loots().add(new ItemStack(Material.SANDSTONE))));
//        add(new BlockLootsItem(Collections.singletonList(Material.RED_SANDSTONE), false, new Loots().add(new ItemStack(Material.RED_SANDSTONE))));
        add(new BlockLootsItem(Collections.singletonList(Material.GRAVEL), true, new Loots()
                .add(0.05, new ItemStack(Material.FLINT, 2), 0, null)
                .add(0.95, new ItemStack(Material.FLINT, 1), 0, null)

                .add(0.05, new ItemStack(Material.FLINT, 3), 1, null)
                .add(0.90, new ItemStack(Material.FLINT, 2), 1, null)
                .add(0.05, new ItemStack(Material.FLINT, 1), 1, null)

                .add(0.05, new ItemStack(Material.FLINT, 4), 2, null)
                .add(0.90, new ItemStack(Material.FLINT, 3), 2, null)
                .add(0.05, new ItemStack(Material.FLINT, 2), 2, null)

                .add(0.05, new ItemStack(Material.FLINT, 5), 3, null)
                .add(0.90, new ItemStack(Material.FLINT, 4), 3, null)
                .add(0.05, new ItemStack(Material.FLINT, 3), 3, null)
        ));
        add(new BlockLootsItem(Arrays.asList(Material.LEAVES, Material.LEAVES_2), false, new Loots()
                .add(1, new ItemStack(Material.LEAVES), -1, true)
                .add(0.05, new ItemStack(Material.LEAVES), -1, true)

                .add(0.1, new ItemStack(Material.SAPLING), -1, false)
                .add(0.3, new ItemStack(Material.APPLE), -1, false)

                .add(0.007, new ItemStack(Material.GOLDEN_APPLE), 0, false)
                .add(0.012, new ItemStack(Material.GOLDEN_APPLE), 1, false)
                .add(0.018, new ItemStack(Material.GOLDEN_APPLE), 2, false)
                .add(0.025, new ItemStack(Material.GOLDEN_APPLE), 3, false)

                .add(0.0005, new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), -1, false)
                .add(0.2, new ItemStack(Material.GOLDEN_APPLE, 1, (short) 1), 32767, false)

        ));
        add(new BlockLootsItem(Collections.singletonList(Material.IRON_ORE), true, new Loots()
                .add(0.05, new ItemStack(Material.IRON_INGOT, 2), 0, false)
                .add(0.95, new ItemStack(Material.IRON_INGOT, 1), 0, false)

                .add(0.05, new ItemStack(Material.IRON_INGOT, 3), 1, false)
                .add(0.90, new ItemStack(Material.IRON_INGOT, 2), 1, false)
                .add(0.05, new ItemStack(Material.IRON_INGOT, 1), 1, false)

                .add(0.05, new ItemStack(Material.IRON_INGOT, 4), 2, false)
                .add(0.90, new ItemStack(Material.IRON_INGOT, 3), 2, false)
                .add(0.05, new ItemStack(Material.IRON_INGOT, 2), 2, false)

                .add(0.05, new ItemStack(Material.IRON_INGOT, 5), 3, false)
                .add(0.90, new ItemStack(Material.IRON_INGOT, 4), 3, false)
                .add(0.05, new ItemStack(Material.IRON_INGOT, 3), 3, false)

                .add(0.05, new ItemStack(Material.IRON_ORE, 2), 0, true)
                .add(0.95, new ItemStack(Material.IRON_ORE, 1), 0, true)

                .add(0.05, new ItemStack(Material.IRON_ORE, 3), 1, true)
                .add(0.90, new ItemStack(Material.IRON_ORE, 2), 1, true)
                .add(0.05, new ItemStack(Material.IRON_ORE, 1), 1, true)

                .add(0.05, new ItemStack(Material.IRON_ORE, 4), 2, true)
                .add(0.90, new ItemStack(Material.IRON_ORE, 3), 2, true)
                .add(0.05, new ItemStack(Material.IRON_ORE, 2), 2, true)

                .add(0.05, new ItemStack(Material.IRON_ORE, 5), 3, true)
                .add(0.90, new ItemStack(Material.IRON_ORE, 4), 3, true)
                .add(0.05, new ItemStack(Material.IRON_ORE, 3), 3, true)
        ));
        add(new BlockLootsItem(Collections.singletonList(Material.GOLD_ORE), true, new Loots()
                .add(0.05, new ItemStack(Material.GOLD_INGOT, 2), 0, false)
                .add(0.95, new ItemStack(Material.GOLD_INGOT, 1), 0, false)

                .add(0.05, new ItemStack(Material.GOLD_INGOT, 3), 1, false)
                .add(0.90, new ItemStack(Material.GOLD_INGOT, 2), 1, false)
                .add(0.05, new ItemStack(Material.GOLD_INGOT, 1), 1, false)

                .add(0.05, new ItemStack(Material.GOLD_INGOT, 4), 2, false)
                .add(0.90, new ItemStack(Material.GOLD_INGOT, 3), 2, false)
                .add(0.05, new ItemStack(Material.GOLD_INGOT, 2), 2, false)

                .add(0.05, new ItemStack(Material.GOLD_INGOT, 5), 3, false)
                .add(0.90, new ItemStack(Material.GOLD_INGOT, 4), 3, false)
                .add(0.05, new ItemStack(Material.GOLD_INGOT, 3), 3, false)

                .add(0.05, new ItemStack(Material.GOLD_ORE, 2), 0, true)
                .add(0.95, new ItemStack(Material.GOLD_ORE, 1), 0, true)

                .add(0.05, new ItemStack(Material.GOLD_ORE, 3), 1, true)
                .add(0.90, new ItemStack(Material.GOLD_ORE, 2), 1, true)
                .add(0.05, new ItemStack(Material.GOLD_ORE, 1), 1, true)

                .add(0.05, new ItemStack(Material.GOLD_ORE, 4), 2, true)
                .add(0.90, new ItemStack(Material.GOLD_ORE, 3), 2, true)
                .add(0.05, new ItemStack(Material.GOLD_ORE, 2), 2, true)

                .add(0.05, new ItemStack(Material.GOLD_ORE, 5), 3, true)
                .add(0.90, new ItemStack(Material.GOLD_ORE, 4), 3, true)
                .add(0.05, new ItemStack(Material.GOLD_ORE, 3), 3, true)
        ));
    }};

    public static List<MobLootsItem> killMobLoots = new ArrayList<MobLootsItem>() {{
// Deprecated :
// add(new MobLootsItem(EntityType.CREEPER, EntityData.CREEPER_NORMAL, false, new Loots()
//         .setChanceLvlAmountCoefficient(1)
//         .add(0.05, new ItemStack(Material.SULPHUR), 0, null)
//         .add(0.10, new ItemStack(Material.SULPHUR), 0, null)
//         .add(0.15, new ItemStack(Material.SULPHUR), 0, null)
//         .add(0.05, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 0, false)
//         .add(0.10, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 0, true)
//         .add(0.10, new ItemStack(Material.SULPHUR), 1, null)
//         .add(0.15, new ItemStack(Material.SULPHUR), 1, null)
//         .add(0.20, new ItemStack(Material.SULPHUR), 1, null)
//         .add(0.10, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 1, false)
//         .add(0.15, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 1, true)
//         .add(0.15, new ItemStack(Material.SULPHUR), 2, null)
//         .add(0.20, new ItemStack(Material.SULPHUR), 2, null)
//         .add(0.25, new ItemStack(Material.SULPHUR), 2, null)
//         .add(0.15, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 2, false)
//         .add(0.20, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 2, true)
//         .add(0.80, new ItemStack(Material.SULPHUR), -2, null)
//         .add(0.30, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), -2, false)
//         .add(0.60, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), -2, true)
// ));
// add(new MobLootsItem(EntityType.CREEPER, EntityData.CREEPER_SUPERCHARGED, false, new Loots()
//         // Just the same that normal creeper but x1.5
//         .setChanceLvlAmountCoefficient(1)
//         .add(0.075, new ItemStack(Material.SULPHUR), 0, null)
//         .add(0.150, new ItemStack(Material.SULPHUR), 0, null)
//         .add(0.225, new ItemStack(Material.SULPHUR), 0, null)
//         .add(0.075, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 0, false)
//         .add(0.150, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 0, true)
//         .add(0.150, new ItemStack(Material.SULPHUR), 1, null)
//         .add(0.225, new ItemStack(Material.SULPHUR), 1, null)
//         .add(0.300, new ItemStack(Material.SULPHUR), 1, null)
//         .add(0.150, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 1, false)
//         .add(0.175, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 1, true)
//         .add(0.225, new ItemStack(Material.SULPHUR), 2, null)
//         .add(0.300, new ItemStack(Material.SULPHUR), 2, null)
//         .add(0.375, new ItemStack(Material.SULPHUR), 2, null)
//         .add(0.175, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 2, false)
//         .add(0.300, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 2, true)
//         .add(1.200, new ItemStack(Material.SULPHUR), -2, null)
//         .add(0.450, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), -2, false)
//         .add(0.900, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), -2, true)
// ));
        add(new MobLootsItem(EntityType.SKELETON, EntityData.SKELETON_NORMAL, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.05, new ItemStack(Material.BOW), 0, null)
                .add(0.33, new ItemStack(Material.ARROW), 0, null)
                .add(0.33, new ItemStack(Material.ARROW), 0, null)
                .add(0.33, new ItemStack(Material.ARROW), 0, null)
                .add(0.005, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 0, false)
                .add(0.01, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 0, true)
                .add(0.15, new ItemStack(Material.BOW), 1, null)
                .add(0.66, new ItemStack(Material.ARROW), 1, null)
                .add(0.66, new ItemStack(Material.ARROW), 1, null)
                .add(0.66, new ItemStack(Material.ARROW), 1, null)
                .add(0.01, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 1, false)
                .add(0.02, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 1, true)
                .add(0.25, new ItemStack(Material.BOW), 2, null)
                .add(0.99, new ItemStack(Material.ARROW), 2, null)
                .add(0.99, new ItemStack(Material.ARROW), 2, null)
                .add(0.99, new ItemStack(Material.ARROW), 2, null)
                .add(0.02, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 2, false)
                .add(0.04, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 2, true)
                .add(0.50, new ItemStack(Material.BOW), -2, null)
                .add(1, new ItemStack(Material.ARROW), -2, null)
                .add(1, new ItemStack(Material.ARROW), -2, null)
                .add(1, new ItemStack(Material.ARROW), -2, null)
        ));
        add(new MobLootsItem(EntityType.SKELETON, EntityData.SKELETON_WITHER, true, new Loots()
                .setChanceLvlProbaCoefficient(1)
                .add(0.25, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), -2, null)
        ));
        add(new MobLootsItem(EntityType.SPIDER, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.50, new ItemStack(Material.STRING), -2, null)
                .add(0.50, new ItemStack(Material.STRING), -2, null)
                .add(0.50, new ItemStack(Material.STRING), -2, null)
                .add(0.50, new ItemStack(Material.STRING), -2, null)
                .add(0.25, new ItemStack(Material.SPIDER_EYE), -2, null)
                .add(0.25, new ItemStack(Material.SPIDER_EYE), -2, null)
                .add(0.10, new ItemStack(Material.FERMENTED_SPIDER_EYE), -2, null)
                .add(0.10, new ItemStack(Material.FERMENTED_SPIDER_EYE), -2, null)
        ));
        add(new MobLootsItem(EntityType.CAVE_SPIDER, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.50, new ItemStack(Material.SPIDER_EYE), -2, null)
                .add(0.50, new ItemStack(Material.SPIDER_EYE), -2, null)
                .add(0.50, new ItemStack(Material.STRING), -2, null)
                .add(0.50, new ItemStack(Material.STRING), -2, null)
                .add(0.25, new ItemStack(Material.FERMENTED_SPIDER_EYE), -2, null)
                .add(0.25, new ItemStack(Material.FERMENTED_SPIDER_EYE), -2, null)
        ));
        add(new MobLootsItem(EntityType.ZOMBIE, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.33, new ItemStack(Material.COOKED_BEEF), -2, null)
                .add(0.33, new ItemStack(Material.COOKED_BEEF), -2, null)
                .add(0.25, new ItemStack(Material.GOLDEN_CARROT), -2, null)
        ));
        add(new MobLootsItem(EntityType.PIG_ZOMBIE, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.33, new ItemStack(Material.COOKED_BEEF), -2, null)
                .add(0.33, new ItemStack(Material.GOLDEN_CARROT), -2, null)
                .add(0.33, new ItemStack(Material.GOLDEN_CARROT), -2, null)
                .add(0.25, new ItemStack(Material.GOLD_INGOT), -2, null)
                .add(0.25, new ItemStack(Material.GOLD_INGOT), -2, null)
        ));
        add(new MobLootsItem(EntityType.BLAZE, false, new Loots()
                .setChanceLvlProbaCoefficient(2)
                .add(0.20, new ItemStack(Material.BLAZE_ROD), -2, null)
                .add(0.20, new ItemStack(Material.BLAZE_ROD), -2, null)
        ));
        add(new MobLootsItem(EntityType.ENDERMAN, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.33, new ItemStack(Material.ENDER_PEARL), -2, null)
                .add(0.33, new ItemStack(Material.ENDER_PEARL), -2, null)
                .add(0.33, new ItemStack(Material.ENDER_PEARL), -2, null)
        ));
        add(new MobLootsItem(EntityType.ENDERMITE, false, new Loots()
                .setChanceLvlProbaCoefficient(1)
                .add(0.33, new ItemStack(Material.ENDER_PEARL), -2, null)
                .add(0.33, new ItemStack(Material.ENDER_PEARL), -2, null)
                .add(0.33, new ItemStack(Material.ENDER_PEARL), -2, null)
                .add(0.33, new ItemStack(Material.ENDER_PEARL), -2, null)
        ));
        add(new MobLootsItem(EntityType.WITCH, false, new Loots()
                .setChanceLvlProbaCoefficient(2)
                .add(0.33, new ItemStack(Material.GLASS_BOTTLE), -2, null)
                .add(0.33, new ItemStack(Material.GLASS_BOTTLE), -2, null)
                .add(0.33, new ItemStack(Material.SUGAR_CANE), -2, null)
                .add(0.33, new ItemStack(Material.SUGAR_CANE), -2, null)
                .add(0.33, new ItemStack(Material.SPIDER_EYE), -2, null)
                .add(0.33, new ItemStack(Material.SPIDER_EYE), -2, null)
                .add(0.33, new ItemStack(Material.GLOWSTONE), -2, null)
                .add(0.33, new ItemStack(Material.GLOWSTONE), -2, null)
                .add(0.33, new ItemStack(Material.REDSTONE), -2, null)
                .add(0.33, new ItemStack(Material.REDSTONE), -2, null)
                .add(0.33, new ItemStack(Material.SULPHUR), -2, null)
                .add(0.33, new ItemStack(Material.SULPHUR), -2, null)
        ));
        add(new MobLootsItem(EntityType.COW, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.5, new ItemStack(Material.LEATHER), -1, null)
                .add(0.5, new ItemStack(Material.LEATHER), -1, null)
                .add(0.5, new ItemStack(Material.LEATHER), 1, null)
                .add(0.5, new ItemStack(Material.LEATHER), 2, null)
                .add(0.5, new ItemStack(Material.LEATHER), 3, null)
                .add(1, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 2, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 3, null)
        ));
        add(new MobLootsItem(EntityType.MUSHROOM_COW, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.5, new ItemStack(Material.LEATHER), -1, null)
                .add(0.5, new ItemStack(Material.LEATHER), -1, null)
                .add(0.5, new ItemStack(Material.LEATHER), 1, null)
                .add(0.5, new ItemStack(Material.LEATHER), 2, null)
                .add(0.5, new ItemStack(Material.LEATHER), 3, null)
                .add(1, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 2, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 3, null)
                .add(0.5, new ItemStack(Material.GOLDEN_CARROT), -1, null)
                .add(0.5, new ItemStack(Material.GOLDEN_CARROT), -1, null)
                .add(0.5, new ItemStack(Material.GOLDEN_CARROT), 1, null)
                .add(0.5, new ItemStack(Material.GOLDEN_CARROT), 2, null)
                .add(0.5, new ItemStack(Material.GOLDEN_CARROT), 3, null)
        ));
        add(new MobLootsItem(EntityType.PIG, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.5, new ItemStack(Material.LEATHER), -1, null)
                .add(0.5, new ItemStack(Material.LEATHER), -1, null)
                .add(0.5, new ItemStack(Material.LEATHER), 1, null)
                .add(0.5, new ItemStack(Material.LEATHER), 2, null)
                .add(0.5, new ItemStack(Material.LEATHER), 3, null)
                .add(1, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 2, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 3, null)
        ));
        add(new MobLootsItem(EntityType.CHICKEN, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.5, new ItemStack(Material.ARROW), -1, null)
                .add(0.5, new ItemStack(Material.ARROW), -1, null)
                .add(0.5, new ItemStack(Material.ARROW), 1, null)
                .add(0.5, new ItemStack(Material.ARROW), 2, null)
                .add(0.5, new ItemStack(Material.ARROW), 3, null)
                .add(1, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 2, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 3, null)
        ));
        add(new MobLootsItem(EntityType.SHEEP, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(1, new ItemStack(Material.WOOL), -1, null)
                .add(0.5, new ItemStack(Material.WOOL), -1, null)
                .add(0.5, new ItemStack(Material.WOOL), 1, null)
                .add(0.5, new ItemStack(Material.WOOL), 2, null)
                .add(0.5, new ItemStack(Material.WOOL), 3, null)
                .add(1, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), -1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 1, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 2, null)
                .add(0.5, new ItemStack(Material.COOKED_BEEF), 3, null)
        ));
        add(new MobLootsItem(EntityType.HORSE, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(1, new ItemStack(Material.LEATHER), -1, null)
                .add(0.75, new ItemStack(Material.LEATHER), -1, null)
                .add(0.5, new ItemStack(Material.LEATHER), 1, null)
                .add(0.5, new ItemStack(Material.LEATHER), 2, null)
                .add(0.5, new ItemStack(Material.LEATHER), 3, null)
        ));
        add(new MobLootsItem(EntityType.SQUID, false, new Loots() {{
            setChanceLvlAmountCoefficient(1);
            for (short i = 0; i < 16; i++)
                add(0.5, new ItemStack(Material.LEATHER, 1, i), -1, null);
        }}));
        add(new MobLootsItem(EntityType.VILLAGER, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.5, new ItemStack(Material.EMERALD), -1, null)
                .add(0.5, new ItemStack(Material.EMERALD), -1, null)
                .add(0.5, new ItemStack(Material.EMERALD), -1, null)
                .add(0.5, new ItemStack(Material.EMERALD), -1, null)
                .add(0.5, new ItemStack(Material.EMERALD), 1, null)
                .add(0.5, new ItemStack(Material.EMERALD), 1, null)
                .add(0.5, new ItemStack(Material.EMERALD), 2, null)
                .add(0.5, new ItemStack(Material.EMERALD), 2, null)
                .add(0.5, new ItemStack(Material.EMERALD), 3, null)
                .add(0.5, new ItemStack(Material.EMERALD), 3, null)
        ));
        add(new MobLootsItem(EntityType.WITHER, false, new Loots()
                .add(1, new ItemStack(Material.NETHER_STAR), -1, null)
                .add(0.5, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 0, null)
                .add(0.25, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 0, null)
                .add(0.125, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 0, null)
                .add(1, new ItemStack(Material.NETHER_STAR), 1, null)
                .add(0.75, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 1, null)
                .add(0.50, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 1, null)
                .add(0.25, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 1, null)
                .add(1, new ItemStack(Material.NETHER_STAR), 2, null)
                .add(1, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 2, null)
                .add(0.75, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 2, null)
                .add(0.50, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 2, null)
                .add(1, new ItemStack(Material.NETHER_STAR), 3, null)
                .add(1, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 3, null)
                .add(1, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 3, null)
                .add(0.75, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), 3, null)
                .add(1, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), -2, null)
                .add(1, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), -2, null)
                .add(1, new ItemStack(Material.SKULL_ITEM, 1, (short) 1), -2, null)
        ));
        add(new MobLootsItem(EntityType.IRON_GOLEM, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(1, new ItemStack(Material.IRON_INGOT), -1, null)
                .add(1, new ItemStack(Material.IRON_INGOT), -1, null)
                .add(1, new ItemStack(Material.IRON_INGOT), -1, null)
                .add(0.5, new ItemStack(Material.IRON_INGOT), -1, null)
                .add(0.5, new ItemStack(Material.IRON_INGOT), -1, null)
                .add(0.5, new ItemStack(Material.IRON_INGOT), -1, null)
                .add(0.5, new ItemStack(Material.IRON_INGOT), -1, null)
                .add(0.1, new ItemStack(Material.JACK_O_LANTERN), 0, false)
                .add(0.33, new ItemStack(Material.JACK_O_LANTERN), 0, true)
                .add(0.33, new ItemStack(Material.JACK_O_LANTERN), 1, false)
                .add(0.66, new ItemStack(Material.JACK_O_LANTERN), 1, true)
                .add(0.66, new ItemStack(Material.JACK_O_LANTERN), 2, false)
                .add(1, new ItemStack(Material.JACK_O_LANTERN), 2, true)
                .add(1, new ItemStack(Material.IRON_INGOT, 3), -2, null)
                .add(1, new ItemStack(Material.JACK_O_LANTERN), -2, false)
                .add(1, new ItemStack(Material.JACK_O_LANTERN), -2, true)
        ));
        add(new MobLootsItem(EntityType.SLIME, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.75, new ItemStack(Material.SLIME_BALL), -2, null)
                .add(0.5, new ItemStack(Material.SLIME_BALL), -2, null)
        ));
        add(new MobLootsItem(EntityType.MAGMA_CUBE, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.75, new ItemStack(Material.MAGMA_CREAM), -2, null)
                .add(0.5, new ItemStack(Material.MAGMA_CREAM), -2, null)
        ));
        add(new MobLootsItem(EntityType.GHAST, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(1, new ItemStack(Material.GHAST_TEAR), -2, null)
                .add(0.75, new ItemStack(Material.GHAST_TEAR), -2, null)
                .add(0.5, new ItemStack(Material.GHAST_TEAR), -2, null)
        ));
        add(new MobLootsItem(EntityType.ENDER_DRAGON, false, new Loots()
                .setChanceLvlProbaCoefficient(0.5)
                .add(1, new ItemStack(Material.NETHER_STAR), -2, null)
                .add(1, new ItemStack(Material.NETHER_STAR), -2, null)
                .add(1, new ItemStack(Material.NETHER_STAR), -2, null)
                .add(0.75, new ItemStack(Material.NETHER_STAR), -2, null)
                .add(0.5, new ItemStack(Material.NETHER_STAR), -2, null)
                .add(0.5, new ItemStack(Material.NETHER_STAR), -2, null)
                .add(0.25, new ItemStack(Material.NETHER_STAR), -2, null)
        ));
    }};

    public static class BlockLootsItem {
        private List<Material> materials;
        private boolean isExclusive;
        private Loots loots;

        public BlockLootsItem(List<Material> materials, boolean isExclusive, Loots loots) {
            this.materials = materials;
            this.isExclusive = isExclusive;
            this.loots = loots;
        }

        public List<Material> getMaterials() {
            return materials;
        }

        public void setMaterials(List<Material> materials) {
            this.materials = materials;
        }

        public boolean isExclusive() {
            return isExclusive;
        }

        public void setExclusive(boolean inclusive) {
            isExclusive = inclusive;
        }

        public Loots getLoots() {
            return loots;
        }

        public void setLoots(Loots loots) {
            this.loots = loots;
        }
    }

    public static class MobLootsItem {
        private EntityType type;
        private EntityData data;
        private boolean isExclusive;
        private Loots loots;

        public MobLootsItem(EntityType type, boolean isExclusive, Loots loots) {
            this.type = type;
            data = EntityData.WHATEVER;
            this.isExclusive = isExclusive;
            this.loots = loots;
        }

        public MobLootsItem(EntityType type, EntityData data, boolean isExclusive, Loots loots) {
            this.type = type;
            this.data = data;
            this.isExclusive = isExclusive;
            this.loots = loots;
        }

        public EntityType getType() {
            return type;
        }

        public void setType(EntityType type) {
            this.type = type;
        }

        public EntityData getData() {
            return data;
        }

        public void setData(EntityData data) {
            this.data = data;
        }

        public boolean isExclusive() {
            return isExclusive;
        }

        public void setExclusive(boolean inclusive) {
            isExclusive = inclusive;
        }

        public Loots getLoots() {
            return loots;
        }

        public void setLoots(Loots loots) {
            this.loots = loots;
        }
    }

    public static enum EntityData {CREEPER_NORMAL, CREEPER_SUPERCHARGED, SKELETON_NORMAL, SKELETON_WITHER, WHATEVER;}

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        PlayerJoinQuitHandler.onPlayerJoin(e);
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent e) {
        PlayerJoinQuitHandler.onPlayerLeave(e);
    }

    @EventHandler
    public static void onPlayerChat(PlayerChatEvent e) {
        PlayerChatHandler.onPlayerChat(e);
    }

    @EventHandler
    public static void onPlayerPickupItem(PlayerPickupItemEvent e) {
        Freeze.onPlayerPickupItem(e);

        if (!e.isCancelled()) {
            GPlayer gPlayer;
            if (GManager.getCurrentGame() == null
                    || (gPlayer = GManager.getCurrentGame().getPlayer(e.getPlayer().getName(), false)) == null) {
                e.setCancelled(true);
                return;
            }

            if (gPlayer.getManager().getState() == GManager.State.PAUSED
                    && !gPlayer.getTeam().getId().equals(gPlayer.getManager().getGods().getId())) {
                e.setCancelled(true);
                return;
            }

            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                gPlayer.getRoleInfo().getRole().getRole().checkForHand(e.getPlayer());
            }

            gPlayer.getStats().increasePickedItems();
        }
    }

    @EventHandler
    public static void onPlayerDropItem(PlayerDropItemEvent e) {
        Freeze.onPlayerDropItem(e);
        if (!e.isCancelled()) InputGUIAndTools.onDrop(e);

        if (!e.isCancelled()) {
            GPlayer gPlayer;
            if (GManager.getCurrentGame() == null
                    || (gPlayer = GManager.getCurrentGame().getPlayer(e.getPlayer().getName(), false)) == null) {
                e.setCancelled(true);
                return;
            }

            if (gPlayer.getManager().getState() == GManager.State.PAUSED
                    && !gPlayer.getTeam().getId().equals(gPlayer.getManager().getGods().getId())) {
                e.setCancelled(true);
                return;
            }

            gPlayer.getStats().increaseDroppedItems();
        }
    }

    @EventHandler
    public static void onPlayerBucketFill(PlayerBucketFillEvent e) {
        Freeze.onBucketFill(e);
        if (!e.isCancelled())
            BucketHandler.onFill(e);
    }

    @EventHandler
    public static void onPlayerBucketEmpty(PlayerBucketEmptyEvent e) {
        Freeze.onBucketEmpty(e);
        if (!e.isCancelled())
            BucketHandler.onEmpty(e);
    }

    @EventHandler
    public static void onPlayerItemSwap(PlayerItemHeldEvent e) {
        if (!e.isCancelled() && e.getPlayer().getGameMode() != GameMode.CREATIVE && GManager.getCurrentGame() != null) {
            GPlayer gp = GManager.getCurrentGame().getPlayer(e.getPlayer().getName(), false);
            if (gp != null && !Objects.equals(gp.getTeamId(), GTeam.GODS_ID)) {

                gp.getRoleInfo().getRole().getRole().checkForHand(e.getPlayer());

            }
        }
    }

    @EventHandler
    public static void onPlayerBedEnter(PlayerBedEnterEvent e) {
        Freeze.onPlayerBedEnter(e);

        if (!e.isCancelled()) {
            GPlayer gPlayer;
            if (GManager.getCurrentGame() == null
                    || (gPlayer = GManager.getCurrentGame().getPlayer(e.getPlayer().getName(), false)) == null) {
                e.setCancelled(true);
                return;
            }

            if (gPlayer.getManager().getState() == GManager.State.PAUSED
                    && !Objects.equals(gPlayer.getTeamId(), GTeam.GODS_ID))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onPlayerMove(PlayerMoveEvent e) {
        Freeze.onPlayerMove(e);
        if (!e.isCancelled())
            PlayerMoveHandler.onPlayerMove(e);
    }

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e) {
        e.setCancelled(false);
        Freeze.onPlayerInteract(e);

        if (!e.isCancelled() && e.getPlayer().getGameMode() != GameMode.CREATIVE && GManager.getCurrentGame() != null) {
            GPlayer gp = GManager.getCurrentGame().getPlayer(e.getPlayer().getName(), false);
            if (gp != null && !Objects.equals(gp.getTeamId(), GTeam.GODS_ID)) {

                if (e.hasItem()) {
                    if (!gp.getRoleInfo().getRole().getRole().checkForItem(e.getItem())) {
                        e.setCancelled(true);
                    }
                    gp.getRoleInfo().getRole().getRole().checkForHand(e.getPlayer());
                }

            }
        }

        if (!e.isCancelled())
            InputGUIAndTools.onInteract(e);
        if (!e.isCancelled())
            Crafting.onBlockInteract(e);
        if (!e.isCancelled())
            PlayerInteractHandler.onInteract(e);
        if (!e.isCancelled())
            GPickableLocks.Listener.onInteract(e); // LOW
    }

    @EventHandler
    public static void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Freeze.onPlayerInteractEntity(e);
        if (!e.isCancelled())
            PlayerInteractEntityHandler.onPlayerInteractEntity(e);
    }

    @EventHandler
    public static void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        Freeze.onPlayerInteractAtEntity(e);
    }

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e) {
        InCaseThereIsAProblem.Listener.onPlaceBlock(e); // HIGHEST
        if (!e.isCancelled())
            BlockPlaceHandler.onPlaceBlock(e);
    }

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent e) {
        InCaseThereIsAProblem.Listener.onBreakBlock(e); // HIGHEST
        if (!e.isCancelled())
            BlockBreakHandler.onBreakBlock(e);
        if (!e.isCancelled())
            GPickableLocks.Listener.onBreakBlock(e); // LOW
    }

    @EventHandler
    public static void onBlockPhysics(BlockPhysicsEvent e) {
        InputGUIAndTools.onForgeFalls(e);
    }

    @EventHandler
    public static void onExplosionPrime(ExplosionPrimeEvent e) {
    }

    @EventHandler
    public static void onBlockExplode(BlockExplodeEvent e) {
        e.blockList().removeIf(b -> GManager.getCurrentGame() == null
                || GManager.getCurrentGame().getPickableLocks().isPickableLock(b.getLocation())
                || !GManager.getCurrentGame().hasPermission(specialMat.contains(b.getType()) ?
                GPermissions.Type.BREAKSPE : GPermissions.Type.BREAK, Utils.normalize(b.getLocation()), true)
                || (b.getType() == Material.CHEST && GManager.getCurrentGame().getOptions().getAssaults().isActivated()
                && GManager.getCurrentGame().getParticipantsTeams().stream().anyMatch(t -> !t.isEliminated() && t.isInside(Utils.normalize(b.getLocation())))));

        if (!e.isCancelled())
            GPickableLocks.Listener.onExplode(e); // LOW
    }

    @EventHandler
    public static void onEntityExplode(EntityExplodeEvent e) {
        e.blockList().removeIf(b -> GManager.getCurrentGame() == null
                || GManager.getCurrentGame().getPickableLocks().isPickableLock(b.getLocation())
                || !GManager.getCurrentGame().hasPermission(specialMat.contains(b.getType()) ?
                GPermissions.Type.BREAKSPE : GPermissions.Type.BREAK, Utils.normalize(b.getLocation()), true)
                || (b.getType() == Material.CHEST && GManager.getCurrentGame().getOptions().getAssaults().isActivated()
                && GManager.getCurrentGame().getParticipantsTeams().stream().anyMatch(t -> !t.isEliminated() && t.isInside(Utils.normalize(b.getLocation())))));
    }

    @EventHandler
    public static void onEntityDamages(EntityDamageEvent e) {
        EntityDamageHandler.onDamage(e);
    }

    @EventHandler
    public static void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        EntityDamageByEntityHandler.onEntityDamageByEntity(e);
    }

    @EventHandler
    public static void onEntityDeath(EntityDeathEvent e) {
        if (Main.customLootingMobsSystem && !(e.getEntity() instanceof Player))
            e.getDrops().clear();
    }

    @EventHandler
    public static void onCreatureSpawn(CreatureSpawnEvent e) {
    }

    @EventHandler
    public static void onCreatePortal(EntityCreatePortalEvent e) {

    }

    @EventHandler
    public static void onEntityPortal(EntityPortalEvent e) {

    }

    @EventHandler
    public static void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            if (Main.end != null && e.getPlayer().getLocation().getWorld().getUID().equals(Main.end.getUID())
                    && GManager.getCurrentGame() != null && GManager.getCurrentGame().getSpawn() != null
                    && GManager.getCurrentGame().getSpawn().getSpawn() != null) {
                e.setCancelled(true);
                e.getPlayer().teleport(GManager.getCurrentGame().getSpawn().getSpawn());
            }
        }
    }

    @EventHandler
    public static void onFoodLevelChange(FoodLevelChangeEvent e) {
        GPlayer p;
        if (GManager.getCurrentGame() == null
                || (p = GManager.getCurrentGame().getPlayer(e.getEntity().getName(), false)) == null
                || p.getManager().getState() != GManager.State.RUNNING) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player && e.getFoodLevel() - ((Player) e.getEntity()).getFoodLevel() > 0)
            p.getStats().increaseRegainedFood((e.getFoodLevel() - ((Player) e.getEntity()).getFoodLevel()));
    }

    @EventHandler
    public static void onBowShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            GPlayer p;
            if (GManager.getCurrentGame() == null
                    || (p = GManager.getCurrentGame().getPlayer(e.getEntity().getName(), false)) == null) {
                e.setCancelled(true);
                return;
            }

            p.getStats().increaseArrowsShot();
        }
    }

    @EventHandler
    public static void onItemEnchant(EnchantItemEvent e) {
        GPlayer p;
        if (GManager.getCurrentGame() == null
                || (p = GManager.getCurrentGame().getPlayer(e.getEnchanter().getName(), false)) == null) {
            e.setCancelled(true);
            return;
        }

        if (e.getEnchanter().getGameMode() != GameMode.CREATIVE && !Objects.equals(p.getTeamId(), GTeam.GODS_ID)) {
            for (Map.Entry<Enchantment, Integer> toAdd : e.getEnchantsToAdd().entrySet()) {
                for (Map.Entry<Enchantment, Integer> limit : p.getRoleInfo().getRole().getRole().getEnchantLimit().entrySet()) {
                    if (toAdd.getKey().equals(limit.getKey()) && toAdd.getValue() > limit.getValue()) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }

        p.getStats().increaseEnchantedItems();

        if (GManager.getCurrentGame().getLimits() == null)
            return;

        Limits lim = GManager.getCurrentGame().getLimits();

        for (Enchantment enchant : e.getEnchantsToAdd().keySet())
            if (lim.getEnchantSpe().containsKey(enchant)
                    && lim.getEnchantSpe().get(enchant).containsKey(e.getItem().getType())
                    && e.getEnchantsToAdd().get(enchant) > lim.getEnchantSpe().get(enchant).get(e.getItem().getType()))
                e.setCancelled(true);
            else if (lim.getEnchantGlobal().containsKey(enchant)
                    && e.getEnchantsToAdd().get(enchant) > lim.getEnchantGlobal().get(enchant))
                e.setCancelled(true);
    }

    @EventHandler
    public static void onBrewPotion(BrewEvent e) {
        if (GManager.getCurrentGame() == null || GManager.getCurrentGame().getLimits() == null
                || e.getContents().getIngredient().getType() != Material.GLOWSTONE_DUST)
            return;

        Limits lim = GManager.getCurrentGame().getLimits();
        HashMap<PotionEffectType, Integer> p = new HashMap<>();
        for (ItemStack is : Arrays.asList(e.getContents().getItem(0),
                e.getContents().getItem(1), e.getContents().getItem(2)))
            if (is != null && is.getType() == Material.POTION)
                Potion.fromItemStack(is).getEffects().forEach(pe -> {
                    if (p.containsKey(pe.getType()))
                        p.replace(pe.getType(), Math.max(pe.getAmplifier(), p.get(pe.getType())));
                    else
                        p.put(pe.getType(), pe.getAmplifier());
                });
        for (PotionEffectType pet : p.keySet())
            if (lim.getPotion().containsKey(pet) && p.get(pet) + 1 > lim.getPotion().get(pet)) {
                e.setCancelled(true);
                return;
            }
    }

    @EventHandler
    public static void onCraft(CraftItemEvent e) {
        if (!e.isCancelled() && e.getWhoClicked().getGameMode() != GameMode.CREATIVE && GManager.getCurrentGame() != null) {
            GPlayer gp = GManager.getCurrentGame().getPlayer(e.getWhoClicked().getName(), false);
            if (gp != null && !Objects.equals(gp.getTeamId(), GTeam.GODS_ID)
                    && !gp.getRoleInfo().getRole().getRole().checkForItem(e.getRecipe().getResult())) {
                e.setResult(org.bukkit.event.Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public static void onInventoryOpen(InventoryOpenEvent e) {
        GPlayer p;
        if (GManager.getCurrentGame() == null
                || (p = GManager.getCurrentGame().getPlayer(e.getPlayer().getName(), false)) == null) {
            return;
        }
        p.getStats().increaseInventoriesOpened();
    }

    @EventHandler
    public static void onInventoryClose(InventoryCloseEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE && GManager.getCurrentGame() != null) {
            GPlayer gp = GManager.getCurrentGame().getPlayer(e.getPlayer().getName(), false);
            if (gp != null && !Objects.equals(gp.getTeamId(), GTeam.GODS_ID)) {
                gp.getRoleInfo().getRole().getRole().checkForArmor((Player) e.getPlayer());
                gp.getRoleInfo().getRole().getRole().checkForHand((Player) e.getPlayer());
            }
        }
        Crafting.onClose(e);
        InputGUIAndTools.onExit(e);
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e) {
        if (!e.isCancelled() && e.getWhoClicked().getGameMode() != GameMode.CREATIVE && GManager.getCurrentGame() != null) {
            GPlayer gp = GManager.getCurrentGame().getPlayer(e.getWhoClicked().getName(), false);
            if (gp != null && !Objects.equals(gp.getTeamId(), GTeam.GODS_ID)) {

                if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.PLAYER
                        && Arrays.asList(36, 37, 38, 39, e.getWhoClicked().getInventory().getHeldItemSlot()).contains(e.getSlot())) {
                    ItemStack is = e.getCursor();
                    if (!gp.getRoleInfo().getRole().getRole().checkForItem(is)) {
                        e.setCancelled(true);
                    }
                }

                if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.ANVIL && e.getSlot() == 2) {
                    ItemStack is = e.getCurrentItem();
                    if (!gp.getRoleInfo().getRole().getRole().checkForItem(is)) {
                        e.setCancelled(true);
                    }
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        gp.getRoleInfo().getRole().getRole().checkForArmor((Player) e.getWhoClicked());
                        gp.getRoleInfo().getRole().getRole().checkForHand((Player) e.getWhoClicked());
                    }
                }.runTaskLater(Main.instance, 1);

            }
        }

        if (!e.isCancelled())
            Crafting.onClick(e);
        if (!e.isCancelled())
            InputGUIAndTools.onClick(e);
        if (!e.isCancelled())
            InventoryClickHandler.onClick(e);
    }

    @EventHandler
    public static void onWeatherChange(WeatherChangeEvent e) {
    }

}
