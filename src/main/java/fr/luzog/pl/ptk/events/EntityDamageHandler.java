package fr.luzog.pl.ptk.events;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.game.GTeam;
import fr.luzog.pl.ptk.utils.Broadcast;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EntityDamageHandler {

    public static List<String> spawnProtected = new ArrayList<>();
    public static String spawnProtectionExpirationMessage = "§aVous êtes désormais §6vulnérable§a...";

    @Events.Event
    public static void onDamage(EntityDamageEvent e) {
        if (e.getEntity().getType() == EntityType.ARMOR_STAND)
            return;

        if (!(e.getEntity() instanceof LivingEntity))
            return;

        LivingEntity entity = (LivingEntity) e.getEntity();
        Location tempLoc = entity.getLocation().add(0, 0.5, 0);

        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            GPlayer gp = GManager.getCurrentGame() == null ? null : GManager.getCurrentGame().getPlayer(p.getName(), false);

            if (gp == null || spawnProtected.contains(p.getName())
                    || (gp.getManager().getState() == GManager.State.PAUSED
                    && !Objects.equals(gp.getTeamId(), GTeam.GODS_ID))) {
                e.setCancelled(true);
                return;
            }

            if (e.getDamage() < 1_000_000_000_000_000_000_000.0) // Avoid /kill command
                gp.getStats().increaseDamageTaken(e.getDamage());

            if (entity.getHealth() - e.getFinalDamage() <= 0 && e.getCause() != EntityDamageEvent.DamageCause.POISON) {
                gp.getStats().increaseDeaths();
                gp.saveStats();
                List<String> m = new ArrayList<>();
                switch (e.getCause()) {
                    case BLOCK_EXPLOSION:
                        m.add("a été explosé.");
                        m.add("a été explosé.");
                        m.add("a pété... §oPÉTÉ !");
                        break;
                    case CONTACT:
                    case CUSTOM:
                        break;
                    case DROWNING:
                        m.add("s'est noyé.");
                        m.add("ne savait pas nager.");
                        m.add("glouglouglouu...");
                        break;
                    case ENTITY_ATTACK:
                        String s = e.getEntity().getMetadata(Events.lastDamagerMeta).get(0).asString();
                        m.add("a été tué par " + s + "§r.");
                        m.add("s'est fait décimé par " + s + "§r.");
                        m.add("s'est fait violenté par " + s + "§r.");
                        m.add("a perdu le combat contre " + s + "§r.");
                        m.add("a perdu la bataille contre " + s + "§r.");
                        m.add("s'est mesuré à " + s + "§r.");
                        m.add("s'est opposé à " + s + "§r.");
                        m.add("a défié " + s + "§r et a perdu...");
                        break;
                    case ENTITY_EXPLOSION:
                        m.add("a été explosé.");
                        m.add("n'est pas fan des creepers. ^^'");
                        m.add("a pété... §oPÉTÉ !");
                        break;
                    case FALL:
                        m.add("est mort de chute.");
                        m.add("s'est écrasé par terre.");
                        m.add("est descendu trop vite.");
                        m.add("a fait un plongeon raté.");
                        m.add("s'est pris pour un oiseau.");
                        m.add("a imité une crêpe.");
                        break;
                    case FALLING_BLOCK:
                        m.add("a été tué par un bloc.");
                        break;
                    case FIRE:
                        m.add("a été carbonisé instantanément !!");
                        break;
                    case FIRE_TICK:
                        m.add("a brûlé.");
                        m.add("n'est pas fan des flammes.");
                        m.add("a subit les flammes.");
                        m.add("s'est fait décimer par le feu.");
                        m.add("a voulu faire un barbecue.");
                        break;
                    case LAVA:
                        m.add("a nagé dans la lave.");
                        m.add("a tenté un plongeon dans le magma.");
                        m.add("a voulu faire plouf dans la lave.");
                        break;
                    case LIGHTNING:
                        m.add("a été frappé par la foudre.");
                        m.add("s'est électrocuté.");
                        m.add("a été touché par un éclair.");
                        m.add("a subit un coup de foudre.");
                        break;
                    case MAGIC:
                        m.add("a été tué par la magie.");
                        break;
                    case MELTING:
                        m.add("a été fondu.");
                        break;
                    case POISON:
                        // Useless: Poison can't kill
                        m.add("a été empoisonné.");
                        break;
                    case PROJECTILE:
                        m.add("s'est fait tirer dessus.");
                        break;
                    case STARVATION:
                        m.add("est mort de faim.");
                        m.add("a oublié de manger.");
                        break;
                    case SUFFOCATION:
                        m.add("s'est étouffé.");
                        m.add("s'est asphyxié.");
                        m.add("a suffoqué.");
                        break;
                    case SUICIDE:
                        m.add("s'est suicidé. lol.");
                        break;
                    case THORNS:
                        m.add("s'est pris sa propre attaque.");
                        break;
                    case VOID:
                        m.add("est tombé dans le vide.");
                        m.add("a rejoint le vide.");
                        m.add("est tombé dans l'infini...");
                        break;
                    case WITHER:
                        m.add("a été infecté par le wither.");
                        break;
                    default:
                        break;
                }
                if (m.size() == 0) {
                    m.add("est mort.");
                    m.add("n'est plus.");
                    m.add("s'est fait décimé.");
                }
                Broadcast.mess(gp.getDisplayName() + "§c "
                        + m.get(new Random().nextInt(m.size())).replace("§r", "§c"));
                e.setCancelled(true);
                p.setHealth(p.getMaxHealth());
                p.setFoodLevel(20);
                p.setSaturation(20f);
                ((ExperienceOrb) p.getWorld().spawnEntity(p.getLocation(), EntityType.EXPERIENCE_ORB))
                        .setExperience((int) (Utils.lvlToExp(p.getLevel() + p.getExp()) * 0.75));
                p.setLevel(0);
                p.setExp(0);
                p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
                new ArrayList<ItemStack>() {{
                    addAll(Arrays.asList(p.getInventory().getContents()));
                    addAll(Arrays.asList(p.getInventory().getArmorContents()));
                    removeIf(is -> is == null || is.getType() == Material.AIR);
                    forEach(is -> p.getWorld().dropItemNaturally(tempLoc, is));
                }};
                p.getInventory().setArmorContents(new ItemStack[4]);
                p.getInventory().clear();
                spawnProtected.add(p.getName());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (spawnProtected.contains(p.getName())) {
                            spawnProtected.remove(p.getName());
                            p.sendMessage(spawnProtectionExpirationMessage);
                        }
                    }
                }.runTaskLater(Main.instance, Main.globalConfig.getSpawnProtectionDuration());

                GTeam gt = gp.getTeam();
                if (gt != null) {
                    gp.setTeam(null, false);
                    if (gt.hasKing() || gp.getManager().getDay() == 1) {
                        // Respawn
                        gp.setTeam(gt.getId(), false);
                    } else {
                        // Eliminate
                        gt.updatePlayers();
                        gp.setTeam(GTeam.SPECS_ID, true);
                        gt.addOldPlayer(gp.getName(), true);
                        p.setGameMode(GameMode.SPECTATOR);
                        p.setBedSpawnLocation(null, true);
                        if (gt.getPlayers().size() == 0) {
                            gt.eliminate(true, true, true);
                        }
                    }
                }

                Location respawn = null;

                gt = gp.getTeam();
                if (gt != null) {
                    if (!Objects.equals(gt.getId(), GTeam.GODS_ID) && !Objects.equals(gt.getId(), GTeam.SPECS_ID)) {
                        if (p.getBedSpawnLocation() != null) {
                            respawn = p.getBedSpawnLocation();
                        } else if (gt.getSpawn() != null) {
                            respawn = gt.getSpawn();
                        }
                    }
                }

                if (respawn == null) {
                    if (gp.getManager().getSpawn() != null) {
                        respawn = gp.getManager().getSpawn().getSpawn();
                    } else if (gp.getManager().getLobby() != null) {
                        respawn = gp.getManager().getLobby().getSpawn();
                    } else if (Main.world != null) {
                        respawn = Main.world.getSpawnLocation();
                    } else {
                        respawn = p.getLocation();
                    }
                }

                p.teleport(respawn);
            }
        } else if (entity.getHealth() - e.getFinalDamage() <= 0)
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!e.isCancelled()) {
                        if (Main.customLootingMobsSystem) {
                            Events.killMobLoots.forEach(loot -> {
                                if (entity.getType() == loot.getType() && (loot.getData() == Events.EntityData.WHATEVER || (loot.getData() != Events.EntityData.WHATEVER
                                        && (entity instanceof Creeper && ((Creeper) entity).isPowered() == (loot.getData() == Events.EntityData.CREEPER_SUPERCHARGED))
                                        || (entity instanceof Skeleton && ((Skeleton) entity).getSkeletonType() == (loot.getData() == Events.EntityData.SKELETON_WITHER ?
                                        Skeleton.SkeletonType.WITHER : Skeleton.SkeletonType.NORMAL))))) {
                                    Location loc = entity.getLocation().clone().add(0, 0.3, 0);
                                    int chanceLvl = entity.hasMetadata(Events.lastDamageLootingLevelTag) ?
                                            entity.getMetadata(Events.lastDamageLootingLevelTag).get(0).asInt() : 0;
                                    boolean silkTouch = entity.hasMetadata(Events.lastDamageSilkTouchTag) && entity.getMetadata(Events.lastDamageSilkTouchTag).get(0).asBoolean();
                                    if (loot.isExclusive()) {
                                        ItemStack is = loot.getLoots().lootsExclusive(chanceLvl, silkTouch);
                                        if (is.getType() != Material.AIR)
                                            loc.getWorld().dropItemNaturally(loc, is);
                                    } else
                                        loot.getLoots().lootsInclusive(chanceLvl, silkTouch).forEach(is -> loc.getWorld().dropItemNaturally(loc, is));
                                }
                            });
                        }
                    }
                }
            }.runTask(Main.instance);
    }

}
