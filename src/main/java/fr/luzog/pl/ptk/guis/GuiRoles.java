package fr.luzog.pl.ptk.guis;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.role.GRole;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.SpecialChars;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiRoles {

    public static ItemStack getMainItem(String lastLoreLine, String command) {
        return Items.builder(Material.EXP_BOTTLE)
                .setName("§6Rôles")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §6Nombre de rôles : §e" + (GRole.Roles.values().length - 1),
                        " ",
                        "§8" + Guis.loreSeparator
                                + (lastLoreLine == null ? "" : "\n§7" + lastLoreLine)
                )
                .setCantClickOn(true)
                .setGlobalCommandOnClick(command)
                .build();
    }

    public static ItemStack getRoleItem(GRole.Roles roleType, String lastLoreLine, String command) {
        if (roleType == null)
            return getMainItem(null, "null");
        GRole role = roleType.getRole();
        return Items.builder(role.getBase() == null ? new ItemStack(Material.EMERALD) : role.getBase())
                .setName("§e" + role.getName())
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §8ID : §7" + roleType.getId(),
                        " ",
                        "  §8Description :",
                        "    §7" + Utils.breakLines(role.getDescription(), 32)
                                .replace("\n", "\n    §7").replace("§r", "§7"),
                        " ",
                        "§8" + Guis.loreSeparator + (lastLoreLine == null ? "" : "\n§7" + lastLoreLine)
                )
                .setCantClickOn(true)
                .setGlobalCommandOnClick(command)
                .build();
    }

    public static ItemStack getRoleInfoItem(GRole.Info info, String lastLoreLine, String command) {
        if (info == null)
            return getMainItem(null, "null");

        StringBuilder display = new StringBuilder();
        for (Field field : info.getClass().getDeclaredFields()) {
            boolean isAccessible = field.isAccessible();
            if (field.getName().startsWith("_"))
                continue;
            field.setAccessible(true);
            try {
                display.append("\n    §8>  §7").append(field.getName()).append(" :  §f").append(field.get(info));
            } catch (IllegalAccessException e) {
                field.setAccessible(isAccessible);
                throw new RuntimeException(e);
            }
            field.setAccessible(isAccessible);
        }

        return Items.builder(Material.PAPER)
                .setName("§e" + info.getRoleType().getRole().getName() + "§f - §7Info")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        (display.length() == 0 ? "  §8>>>  Rien par ici...  <<<" : "  §8Variables :" + display),
                        " ",
                        "§8" + Guis.loreSeparator + (lastLoreLine == null ? "" : "\n§7" + lastLoreLine)
                )
                .setCantClickOn(true)
                .setGlobalCommandOnClick(command)
                .build();
    }

    public static Inventory getRoleInventory(GRole.Roles roleType, GRole.Info info, String refresh, String back) {
        GRole role = roleType.getRole();

        Inventory inv = Guis.getBaseInventory("§6Rôles §f- §e" + role.getName(), 54, back,
                getMainItem("§7Clic pour rafraîchir", refresh), null);

        inv.setItem(Utils.posOf(4, 1), getRoleItem(roleType, null, "null"));

        if (info != null)
            inv.setItem(Utils.posOf(7, 1), getRoleInfoItem(info, null, "null"));

        inv.setItem(Utils.posOf(3, 2), Items.builder(Material.GOLDEN_APPLE)
                .setName("§4Vie : §c" + ((info == null ? role.getHealth() : info.getFinalHealth()) / 2) + "❤")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §8Base : §c" + (role.getHealth() / 2) + "❤"
                                + (info == null ? "" : "\n  §8Modifieur : "
                                + (info.getHealthModifier() > 0 ? "§a+" : info.getHealthModifier() < 0 ? "§4" : "§7±")
                                + (info.getHealthModifier() / 2) + "❤"),
                        " ",
                        "  " + Utils.heartsDisplay(info == null ? role.getHealth() : info.getFinalHealth()).replace("\n", "\n  "),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .setCantClickOn(true)
                .build());

        StringBuilder armorsLimit = new StringBuilder();
        LinkedHashMap<Integer, String> armors = new LinkedHashMap<>();
        armors.put(0b0000_0000_0000_0001, "Casque en §4Cuir");
        armors.put(0b0000_0000_0000_0010, "Plastron en §4Cuir");
        armors.put(0b0000_0000_0000_0100, "Jambières en §4Cuir");
        armors.put(0b0000_0000_0000_1000, "Bottes en §4Cuir");
        armors.put(0b0000_0000_0001_0000, "Casque en §7Fer");
        armors.put(0b0000_0000_0010_0000, "Plastron en §7Fer");
        armors.put(0b0000_0000_0100_0000, "Jambières en §7Fer");
        armors.put(0b0000_0000_1000_0000, "Bottes en §7Fer");
        armors.put(0b0000_0001_0000_0000, "Casque en §6Or");
        armors.put(0b0000_0010_0000_0000, "Plastron en §6Or");
        armors.put(0b0000_0100_0000_0000, "Jambières en §6Or");
        armors.put(0b0000_1000_0000_0000, "Bottes en §6Or");
        armors.put(0b0001_0000_0000_0000, "Casque en §bDiamant");
        armors.put(0b0010_0000_0000_0000, "Plastron en §bDiamant");
        armors.put(0b0100_0000_0000_0000, "Jambières en §bDiamant");
        armors.put(0b1000_0000_0000_0000, "Bottes en §bDiamant");
        armors.forEach((armor, name) -> {
            boolean r = (role.getArmorLimit() & armor) != 0;
            boolean i = info == null ? r : (info.getFinalArmorLimit() & armor) != 0;
            armorsLimit.append("  §").append(i ? "4" + SpecialChars.NO : "2" + SpecialChars.YES)
                    .append("§7  -  §").append(i ? "c" : "a").append(r ? "§n" : "")
                    .append(r || i ? ChatColor.stripColor(name) : name).append("\n");
        });
        inv.setItem(Utils.posOf(5, 2), Items.builder(Material.DIAMOND_CHESTPLATE)
                .setName("§bArmures autorisées :")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        "§8Code de base : " + role.getArmorLimit() + (info == null ? "" : "\n§8Code actuel : " + info.getFinalArmorLimit()),
                        " ",
                        armorsLimit.toString(),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .setCantClickOn(true)
                .build());

        StringBuilder potionsLimit = new StringBuilder();
        role.getPotionLimit().entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue))
                .forEach((e) -> {
                    String name = Utils.rawPotionToCommonTermMap.containsKey(e.getKey()) ?
                            Utils.rawPotionToCommonTermMap.get(e.getKey()) : e.getKey().getName();
                    if (e.getValue() <= 0) {
                        potionsLimit.append("  §4").append(SpecialChars.NO).append("§7  -  §c").append(name).append("\n");
                    } else {
                        potionsLimit.append("  §6").append(e.getValue()).append("§7  -  §d").append(name).append("\n");
                    }
                });
        if (potionsLimit.length() == 0) {
            potionsLimit.append("  §7---  Aucune potion interdite  ---");
        }
        inv.setItem(Utils.posOf(2, 3), Items.builder(Material.POTION)
                .setDurability((short) 8194)
                .addFlag(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES)
                .setName("§dPotions interdites :")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        potionsLimit.toString(),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .setCantClickOn(true)
                .build());

        StringBuilder enchantsLimit = new StringBuilder();
        role.getEnchantLimit().entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue))
                .forEach((e) -> {
                    String name = Utils.rawEnchantToCommonTermMap.containsKey(e.getKey()) ?
                            Utils.rawEnchantToCommonTermMap.get(e.getKey()) : e.getKey().getName();
                    if (e.getValue() <= 0) {
                        enchantsLimit.append("  §4").append(SpecialChars.NO).append("§7  -  §c").append(name).append("\n");
                    } else {
                        enchantsLimit.append("  §6").append(e.getValue()).append("§7  -  §5").append(name).append("\n");
                    }
                });
        if (enchantsLimit.length() == 0) {
            enchantsLimit.append("  §7---  Aucun enchantement interdit  ---");
        }
        inv.setItem(Utils.posOf(4, 3), Items.builder(Material.ENCHANTED_BOOK)
                .setName("§5Enchantements interdits :")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        enchantsLimit.toString(),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .setCantClickOn(true)
                .build());

        StringBuilder materialLimit = new StringBuilder();
        role.getMaterialLimit().forEach((material) ->
                materialLimit.append("  §4").append(SpecialChars.NO).append("§7  -  §c").append(
                        Arrays.stream(material.name().split("_"))
                                .map(word -> word.isEmpty() ? word
                                        : Character.toTitleCase(word.charAt(0)) + word.substring(1).toLowerCase())
                                .collect(Collectors.joining(" "))
                ).append("\n"));
        if (materialLimit.length() == 0) {
            materialLimit.append("  §7---  Aucun item interdit  ---");
        }
        inv.setItem(Utils.posOf(6, 3), Items.builder(Material.BARRIER)
                .setName("§cItems interdits :")
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        materialLimit.toString(),
                        " ",
                        "§8" + Guis.loreSeparator
                )
                .setCantClickOn(true)
                .build());

        inv.setItem(Utils.posOf(1, 4), role.getAbility1() == null ?
                Items.builder(Material.INK_SACK)
                        .setDurability((short) 8)
                        .setName("§7Capacité n°1")
                        .setLore(
                                "§8" + Guis.loreSeparator,
                                " ",
                                "  §8>>>  Aucune  <<<",
                                " ",
                                "§8" + Guis.loreSeparator
                        )
                        .setCantClickOn(true)
                        .build() :
                Items.builder(role.getAbility1())
                        .setCantClickOn(true)
                        .build());

        inv.setItem(Utils.posOf(3, 4), role.getAbility2() == null ?
                Items.builder(Material.INK_SACK)
                        .setDurability((short) 8)
                        .setName("§7Capacité n°2")
                        .setLore(
                                "§8" + Guis.loreSeparator,
                                " ",
                                "  §8>>>  Aucune  <<<",
                                " ",
                                "§8" + Guis.loreSeparator
                        )
                        .setCantClickOn(true)
                        .build() :
                Items.builder(role.getAbility2())
                        .setCantClickOn(true)
                        .build());

        inv.setItem(Utils.posOf(5, 4), role.getAbility3() == null ?
                Items.builder(Material.INK_SACK)
                        .setDurability((short) 8)
                        .setName("§7Capacité n°3")
                        .setLore(
                                "§8" + Guis.loreSeparator,
                                " ",
                                "  §8>>>  Aucune  <<<",
                                " ",
                                "§8" + Guis.loreSeparator
                        )
                        .setCantClickOn(true)
                        .build() :
                Items.builder(role.getAbility3())
                        .setCantClickOn(true)
                        .build());

        inv.setItem(Utils.posOf(7, 4), role.getAbility4() == null ?
                Items.builder(Material.INK_SACK)
                        .setDurability((short) 8)
                        .setName("§7Capacité n°4")
                        .setLore(
                                "§8" + Guis.loreSeparator,
                                " ",
                                "  §8>>>  Aucune  <<<",
                                " ",
                                "§8" + Guis.loreSeparator
                        )
                        .setCantClickOn(true)
                        .build() :
                Items.builder(role.getAbility4())
                        .setCantClickOn(true)
                        .build());

        return inv;
    }

    public static Inventory getMainInventory(String back, String navigationBaseCommand, int page) {
        return Guis.getPagedInventory("§6Rôles", 54, back,
                getMainItem("Clic pour rafraîchir", navigationBaseCommand + " " + page),
                null, navigationBaseCommand, page, Arrays.stream(GRole.Roles.values())
                        .map(r -> getRoleItem(r, "Clic pour voir plus\n \n§7Commande :\n§7/" + Main.CMD + " roles "
                                + r.getId(), Main.CMD + " roles " + r.getId())).collect(Collectors.toList()));
    }
}
