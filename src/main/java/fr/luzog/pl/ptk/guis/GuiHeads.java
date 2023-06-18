package fr.luzog.pl.ptk.guis;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.commands.Fun.Head;
import fr.luzog.pl.ptk.game.GListener;
import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GPickableLocks;
import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.SpecialChars;
import fr.luzog.pl.ptk.utils.Utils;
import net.minecraft.server.v1_8_R3.TileEntity;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class GuiHeads {
    private static final String colors = "123456789abcde";

    public static String randomizeColor(String origin) {
        Random r = new Random();
        StringBuilder fin = new StringBuilder();
        for(int i = 0; i < origin.length(); i++) {
            fin.append("§").append(colors.charAt(r.nextInt(colors.length()))).append(origin.charAt(i));
        }
        return fin.toString();
    }

    public static ItemStack getMainItem(String lastLoreLine, String command) {
        return Items.builder(Heads.RAINBOW_CHEST.getSkull())
                .setName(randomizeColor("Custom Heads"))
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  " + randomizeColor("Nombre de têtes custom :") + " §f" + Heads.values().length,
                        " ",
                        "§8" + Guis.loreSeparator
                                + (lastLoreLine == null ? "" : "\n§7" + lastLoreLine)
                )
                .setCantClickOn(true)
                .setGlobalCommandOnClick(command)
                .build();
    }

    public static ItemStack getHeadItem(String id, String name, String data, boolean isBased64,
                                        String lastLoreLine, String command) {
        StringBuilder formattedData = new StringBuilder();
        if (isBased64) {
            // Splitting the data every 16 characters
            for (String s : data.split("(?<=\\G.{16})")) {
                formattedData.append("\n    §7").append(s);
            }
        } else {
            formattedData.append("\n    §7").append(data);
        }
        return Items.builder(isBased64 ? Heads.getCustomSkull(data) : Heads.getSkullOf(data))
                .setName("§5" + name)
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  §8Identifiant : §f" + id,
                        " ",
                        "  §8Joueur réel : " + (isBased64 ? "§cNon" : "§aOui"),
                        " ",
                        "  §8Données : " + formattedData,
                        " ",
                        "§8" + Guis.loreSeparator + (lastLoreLine == null ? "" : "\n§7" + lastLoreLine)
                )
                .setCantClickOn(true)
                .setGlobalCommandOnClick(command)
                .build();
    }

    public static Inventory getMainInventory(String back, String navigationBaseCommand, int page) {
        ArrayList<ItemStack> content = new ArrayList<>();
        for (Heads head : Heads.values()) {
            content.add(getHeadItem(head.name(), head.getName(), head.getData(), head.isCustom(),
                    "§7Clic pour récupérer la tête", "head " + (head.isCustom() ? "-b " : "") + head.getData()));
        }

        Inventory inv = Guis.getPagedInventory(randomizeColor("Custom Heads"), 54, back,
                getMainItem("Clic pour rafraîchir", navigationBaseCommand + " " + page),
                null, navigationBaseCommand, page, content);

        return inv;
    }
}
