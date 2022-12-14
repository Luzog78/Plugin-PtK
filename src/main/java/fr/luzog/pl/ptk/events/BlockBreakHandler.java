package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GPermissions;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.game.GTeam;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class BlockBreakHandler {

    public static void dropNormally(Location loc, ItemStack is) {
        dropNormally(loc, Collections.singletonList(is));
    }

    public static void dropNormally(Location loc, Collection<ItemStack> is) {
        Location l = Utils.normalize(loc);
        is.forEach(i -> l.getWorld().dropItemNaturally(l, i));
    }

    @Events.Event
    public static void onBreakBlock(BlockBreakEvent e) {
        GPlayer fp;
        if (GManager.getCurrentGame() == null
                || (fp = GManager.getCurrentGame().getPlayer(e.getPlayer().getName(), false)) == null) {
            e.setCancelled(true);
            return;
        }

        if (!fp.hasPermission(Events.specialMat.contains(e.getBlock().getType()) ?
                GPermissions.Type.BREAKSPE : GPermissions.Type.BREAK,
                Utils.normalize(e.getBlock().getLocation()), 10)) {
            e.setCancelled(true);
            return;
        }

        if (Events.unbreakableMat.contains(e.getBlock().getType())
                && fp.getTeam() != null && fp.getTeam().getId().equals(GTeam.GODS_ID)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Main.PREFIX + "§cBlock Incassable.");
            return;
        }

        fp.getStats().increaseBlocksBroken();
        if (Arrays.asList(Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE,
                Material.LAPIS_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.QUARTZ_ORE,
                Material.GLOWING_REDSTONE_ORE).contains(e.getBlock().getType()))
            fp.getStats().increaseOresBroken();

        if (e.getPlayer().getGameMode() == GameMode.CREATIVE || e.getPlayer().getGameMode() == GameMode.SPECTATOR)
            return;

        int chanceLvl = e.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        boolean silkTouch = e.getPlayer().getItemInHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) > 0;

        if (e.getPlayer().getItemInHand().getType() == Material.SHEARS
                && (e.getBlock().getType() == Material.LEAVES || e.getBlock().getType() == Material.LEAVES_2))
            silkTouch = true;

        boolean finalSilkTouch = silkTouch;
        if (Main.customLootingBlocksSystem)
            CustomLoots.breakBlockLoots.forEach(item -> {
                if (item.getMaterials().contains(e.getBlock().getType())) {
                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR, true);
                    ArrayList<ItemStack> drops = new ArrayList<>();
                    double exp = 0;
                    if (item.isExclusive())
                        drops.add(item.getLoots().lootsExclusive(chanceLvl, finalSilkTouch));
                    else
                        drops.addAll(item.getLoots().lootsInclusive(chanceLvl, finalSilkTouch));
//                        for(ItemStack i : drops)
//                            if(i.getType() == Material.IRON_INGOT || i.getType() == Material.IRON_ORE)
//                                exp += 0.7;
//                            else if (i.getType() == Material.GOLD_INGOT || i.getType() == Material.GOLD_ORE)
//                                exp += 1.0;
                    dropNormally(e.getBlock().getLocation(), drops);
                }
            });
    }

}
