package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.InCaseThereIsAProblem;
import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.commands.Admin.Vanish;
import fr.luzog.pl.ptk.commands.Cheat.Freeze;
import fr.luzog.pl.ptk.commands.Utils.InputText;
import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GPermissions;
import fr.luzog.pl.ptk.game.GPickableLocks;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.utils.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
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

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        PlayerJoinQuitHandler.onPlayerJoin(e);
        Vanish.onPlayerJoin(e);
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
            List<GPlayer> gPlayers = GManager.getGlobalPlayer(e.getPlayer().getName());
            if (gPlayers.isEmpty())
                e.setCancelled(true);
            else for (GPlayer gPlayer : gPlayers) {
                if (gPlayer != null)
                    gPlayer.getStats().increasePickedItems();

                if (gPlayer != null && gPlayer.getManager().getState() == GManager.State.PAUSED
                        && !gPlayer.getTeam().getId().equals(gPlayer.getManager().getGods().getId()))
                    e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void onPlayerDropItem(PlayerDropItemEvent e) {
        Freeze.onPlayerDropItem(e);

        if (!e.isCancelled()) {
            List<GPlayer> gPlayers = GManager.getGlobalPlayer(e.getPlayer().getName());
            if (gPlayers.isEmpty())
                e.setCancelled(true);
            else for (GPlayer gPlayer : gPlayers) {
                if (gPlayer != null)
                    gPlayer.getStats().increaseDroppedItems();

                if (gPlayer != null && gPlayer.getManager().getState() == GManager.State.PAUSED
                        && !gPlayer.getTeam().getId().equals(gPlayer.getManager().getGods().getId()))
                    e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void onPlayerBucketFill(PlayerBucketFillEvent e) {
        Freeze.onBucketFill(e);
        if (!e.isCancelled()) BucketHandler.onFill(e);
    }

    @EventHandler
    public static void onPlayerBucketEmpty(PlayerBucketEmptyEvent e) {
        Freeze.onBucketEmpty(e);
        if (!e.isCancelled()) BucketHandler.onEmpty(e);
    }

    @EventHandler
    public static void onPlayerItemSwap(PlayerItemHeldEvent e) {
    }

    @EventHandler
    public static void onPlayerBedEnter(PlayerBedEnterEvent e) {
        Freeze.onPlayerBedEnter(e);

        if (!e.isCancelled()) {
            List<GPlayer> gPlayers = GManager.getGlobalPlayer(e.getPlayer().getName());
            if (gPlayers.isEmpty())
                e.setCancelled(true);
            else for (GPlayer gPlayer : gPlayers)
                if (gPlayer != null && gPlayer.getManager().getState() == GManager.State.PAUSED
                        && !gPlayer.getTeam().getId().equals(gPlayer.getManager().getGods().getId()))
                    e.setCancelled(true);
        }
    }

    @EventHandler
    public static void onPlayerTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL || e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL)
            e.setCancelled(true);
    }

    @EventHandler
    public static void onPlayerMove(PlayerMoveEvent e) {
        Freeze.onPlayerMove(e);
        if (!e.isCancelled()) PlayerMoveHandler.onPlayerMove(e);
    }

    @EventHandler
    public static void onPlayerInteract(PlayerInteractEvent e) {
        Freeze.onPlayerInteract(e);
        if (!e.isCancelled()) Crafting.onBlockInteract(e);
        if (!e.isCancelled()) PlayerInteractHandler.onInteract(e);
        if (!e.isCancelled()) GPickableLocks.Listener.onInteract(e); // LOW
    }

    @EventHandler
    public static void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Freeze.onPlayerInteractEntity(e);
        if (!e.isCancelled()) PlayerInteractEntityHandler.onPlayerInteractEntity(e);
    }

    @EventHandler
    public static void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        Freeze.onPlayerInteractAtEntity(e);
    }

    @EventHandler
    public static void onBlockPlace(BlockPlaceEvent e) {
        InCaseThereIsAProblem.Listener.onPlaceBlock(e); // HIGHEST
        if (!e.isCancelled()) BlockPlaceHandler.onPlaceBlock(e);
    }

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent e) {
        InCaseThereIsAProblem.Listener.onBreakBlock(e); // HIGHEST
        if (!e.isCancelled()) BlockBreakHandler.onBreakBlock(e);
        if (!e.isCancelled()) GPickableLocks.Listener.onBreakBlock(e); // LOW
    }

    @EventHandler
    public static void onBlockPhysics(BlockPhysicsEvent e) {
        InputText.onForgeFall(e);
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

        if (!e.isCancelled()) GPickableLocks.Listener.onExplode(e); // LOW
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
    public static void onEntityCreatePortal(EntityCreatePortalEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public static void onEntityPortal(EntityPortalEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public static void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getEntity().getType() == EntityType.CREEPER)
            if (new Random().nextInt(4) == 0)
                ((Creeper) e.getEntity()).setPowered(true);
    }

    @EventHandler
    public static void onFoodLevelChange(FoodLevelChangeEvent e) {
        List<GPlayer> gPlayers = GManager.getGlobalPlayer(e.getEntity().getName());
        if (gPlayers.isEmpty()) {
            e.setCancelled(true);
            return;
        }

        for (GPlayer p : gPlayers)
            if (e.getEntity() instanceof Player && p != null && e.getFoodLevel() - ((Player) e.getEntity()).getFoodLevel() > 0)
                p.getStats().increaseRegainedFood((e.getFoodLevel() - ((Player) e.getEntity()).getFoodLevel()));
    }

    @EventHandler
    public static void onBowShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            List<GPlayer> gPlayers = GManager.getGlobalPlayer(e.getEntity().getName());
            if (gPlayers.isEmpty()) {
                e.setCancelled(true);
                return;
            }

            for (GPlayer p : gPlayers)
                if (p != null)
                    p.getStats().increaseArrowsShot();
        }
    }

    @EventHandler
    public static void onItemEnchant(EnchantItemEvent e) {
        List<GPlayer> gPlayers = GManager.getGlobalPlayer(e.getEnchanter().getName());
        if (gPlayers.isEmpty()) {
            e.setCancelled(true);
            return;
        }

        for (GPlayer p : gPlayers)
            if (p != null)
                p.getStats().increaseEnchantedItems();

        if (GManager.getCurrentGame() == null || GManager.getCurrentGame().getLimits() == null
                || GManager.getCurrentGame().getPlayer(e.getEnchanter().getName(), false) == null)
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
        if (Arrays.asList(Material.WOOD_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLD_HOE, Material.DIAMOND_HOE)
                .contains(e.getRecipe().getResult().getType())) {
            e.setResult(org.bukkit.event.Event.Result.ALLOW);
            e.setCurrentItem(Items.builder(e.getRecipe().getResult().getType())
                    .setName("§bHoue Sacrée")
                    .setLore(
                            "§8" + Utils.loreSeparator,
                            " ",
                            "  §8Un grand merci au §dBêta Testeur",
                            "  §f Maxtriller§8 d'avoir aidé à la",
                            "  §8 recherche de bugs.",
                            " ",
                            "  §8C'est grâce à sa personne que",
                            "  §8 vous pouvez utiliser cette houe.",
                            "  §8Alors prenez-en bien soin !",
                            " ",
                            "§8" + Utils.loreSeparator
                    )
                    .addEnchant(Enchantment.DIG_SPEED, 10)
                    .build());
        }
    }

    @EventHandler
    public static void onInventoryOpen(InventoryOpenEvent e) {
        List<GPlayer> gPlayers = GManager.getGlobalPlayer(e.getPlayer().getName());

        for (GPlayer p : gPlayers)
            if (p != null)
                p.getStats().increaseInventoriesOpened();
    }

    @EventHandler
    public static void onInventoryClose(InventoryCloseEvent e) {
        Crafting.onClose(e);
        InputText.onExit(e);
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent e) {
        Crafting.onClick(e);
        if (!e.isCancelled()) InputText.onClick(e);
        if (!e.isCancelled()) InventoryClickHandler.onClick(e);
    }

    @EventHandler
    public static void onWeatherChange(WeatherChangeEvent e) {
    }

}
