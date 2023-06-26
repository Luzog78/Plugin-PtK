package fr.luzog.pl.ptk.guis;

import fr.luzog.pl.ptk.utils.Heads;
import fr.luzog.pl.ptk.utils.Items;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GuiHeads {

    public static ItemStack getMainItem(String lastLoreLine, String command) {
        return Items.builder(Heads.RAINBOW_CHEST.getSkull())
                .setName(Utils.rainbowize("Custom Heads"))
                .setLore(
                        "§8" + Guis.loreSeparator,
                        " ",
                        "  " + Utils.rainbowize("Nombre de têtes custom :") + " §f" + Heads.values().length,
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

        Inventory inv = Guis.getPagedInventory(Utils.rainbowize("Custom Heads"), 54, back,
                getMainItem("Clic pour rafraîchir", navigationBaseCommand + " " + page),
                null, navigationBaseCommand, page, content);

        return inv;
    }
}
