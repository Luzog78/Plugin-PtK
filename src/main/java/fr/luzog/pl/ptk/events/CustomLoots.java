package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.utils.Loots;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomLoots {

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

//                .add(0.05, new ItemStack(Material.WOOD), 0, false)
//                .add(0.10, new ItemStack(Material.WOOD), 1, false)
//                .add(0.15, new ItemStack(Material.WOOD), 2, false)
//                .add(0.2, new ItemStack(Material.WOOD), 3, false)


                .add(0.07, new ItemStack(Material.APPLE), 0, false)
                .add(0.1, new ItemStack(Material.APPLE), 1, false)
                .add(0.15, new ItemStack(Material.APPLE), 2, false)
                .add(0.2, new ItemStack(Material.APPLE), 3, false)

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
        add(new MobLootsItem(EntityType.CREEPER, EntityData.CREEPER_NORMAL, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.05, new ItemStack(Material.SULPHUR), 0, null)
                .add(0.10, new ItemStack(Material.SULPHUR), 0, null)
                .add(0.15, new ItemStack(Material.SULPHUR), 0, null)
                .add(0.05, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 0, false)
                .add(0.10, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 0, true)
                .add(0.10, new ItemStack(Material.SULPHUR), 1, null)
                .add(0.15, new ItemStack(Material.SULPHUR), 1, null)
                .add(0.20, new ItemStack(Material.SULPHUR), 1, null)
                .add(0.10, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 1, false)
                .add(0.15, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 1, true)
                .add(0.15, new ItemStack(Material.SULPHUR), 2, null)
                .add(0.20, new ItemStack(Material.SULPHUR), 2, null)
                .add(0.25, new ItemStack(Material.SULPHUR), 2, null)
                .add(0.15, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 2, false)
                .add(0.20, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 2, true)
                .add(0.80, new ItemStack(Material.SULPHUR), -2, null)
                .add(0.30, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), -2, false)
                .add(0.60, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), -2, true)
        ));
        add(new MobLootsItem(EntityType.CREEPER, EntityData.CREEPER_SUPERCHARGED, false, new Loots()
                // Just the same that normal creeper but x1.5
                .setChanceLvlAmountCoefficient(1)
                .add(0.075, new ItemStack(Material.SULPHUR), 0, null)
                .add(0.150, new ItemStack(Material.SULPHUR), 0, null)
                .add(0.225, new ItemStack(Material.SULPHUR), 0, null)
                .add(0.075, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 0, false)
                .add(0.150, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 0, true)
                .add(0.150, new ItemStack(Material.SULPHUR), 1, null)
                .add(0.225, new ItemStack(Material.SULPHUR), 1, null)
                .add(0.300, new ItemStack(Material.SULPHUR), 1, null)
                .add(0.150, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 1, false)
                .add(0.175, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 1, true)
                .add(0.225, new ItemStack(Material.SULPHUR), 2, null)
                .add(0.300, new ItemStack(Material.SULPHUR), 2, null)
                .add(0.375, new ItemStack(Material.SULPHUR), 2, null)
                .add(0.175, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 2, false)
                .add(0.300, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), 2, true)
                .add(1.200, new ItemStack(Material.SULPHUR), -2, null)
                .add(0.450, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), -2, false)
                .add(0.900, new ItemStack(Material.SKULL_ITEM, 1, (short) 4), -2, true)
        ));
        add(new MobLootsItem(EntityType.SKELETON, EntityData.SKELETON_NORMAL, false, new Loots()
                .setChanceLvlAmountCoefficient(1)
                .add(0.05, new ItemStack(Material.BOW), 0, null)
                .add(0.33, new ItemStack(Material.ARROW), 0, null)
                .add(0.33, new ItemStack(Material.ARROW), 0, null)
                .add(0.33, new ItemStack(Material.ARROW), 0, null)
                .add(0.05, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 0, false)
                .add(0.10, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 0, true)
                .add(0.15, new ItemStack(Material.BOW), 1, null)
                .add(0.66, new ItemStack(Material.ARROW), 1, null)
                .add(0.66, new ItemStack(Material.ARROW), 1, null)
                .add(0.66, new ItemStack(Material.ARROW), 1, null)
                .add(0.10, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 1, false)
                .add(0.15, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 1, true)
                .add(0.25, new ItemStack(Material.BOW), 2, null)
                .add(0.99, new ItemStack(Material.ARROW), 2, null)
                .add(0.99, new ItemStack(Material.ARROW), 2, null)
                .add(0.99, new ItemStack(Material.ARROW), 2, null)
                .add(0.15, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 2, false)
                .add(0.20, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), 2, true)
                .add(0.50, new ItemStack(Material.BOW), -2, null)
                .add(1, new ItemStack(Material.ARROW), -2, null)
                .add(1, new ItemStack(Material.ARROW), -2, null)
                .add(1, new ItemStack(Material.ARROW), -2, null)
                .add(0.30, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), -2, false)
                .add(0.60, new ItemStack(Material.SKULL_ITEM, 1, (short) 0), -2, true)
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
}
