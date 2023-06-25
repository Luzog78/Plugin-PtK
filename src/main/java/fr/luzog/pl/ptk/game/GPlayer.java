package fr.luzog.pl.ptk.game;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.commands.Admin.Vanish;
import fr.luzog.pl.ptk.game.role.GRKing;
import fr.luzog.pl.ptk.game.role.GRKnight;
import fr.luzog.pl.ptk.game.role.GRole;
import fr.luzog.pl.ptk.utils.Color;
import fr.luzog.pl.ptk.utils.Config;
import fr.luzog.pl.ptk.utils.PlayerStats;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GPlayer {

    public void saveToConfig(String gameId, boolean soft) {
        if (soft)
            return;

        getConfig(gameId)
                .load()

                .setLastUuid(lastUuid, true)
                .setTeam(teamId, true)
                .setRoleInfo(roleInfo, true)
                .setCompass(compass, true)
                .setInventories(new ArrayList<>(), false)
                .setStats(stats, true)
                .setPermissions(personalPermissions, true)
                .setWaitingItems(waitingItems, true)
                .setWaitingEffects(waitingEffects, true)

                .save();
    }

    public Config.Player getConfig(String gameId) {
        return new Config.Player("game-" + Objects.requireNonNull(gameId) + "/players/" + name + ".yml");
    }

    public static class Compass {
        private String name;
        private double radius;
        private Location location;

        public Compass(String name, double radius, Location location) {
            this.name = name;
            this.radius = radius;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getRadius() {
            return radius;
        }

        public void setRadius(double radius) {
            this.radius = radius;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    private String name;
    private UUID lastUuid;

    private String teamId;
    private GRole.Info roleInfo;
    private Compass compass;

    private PlayerStats stats;

    private GPermissions personalPermissions;

    private GListener.PersonalListener personalListener;

    private List<ItemStack> waitingItems;
    private List<PotionEffect> waitingEffects;

    public GPlayer(@Nullable String name, @Nullable GRole.Info roleInfo,
                   @Nullable PlayerStats stats, @Nullable GPermissions personalPermissions) {
        this.name = name;
        this.lastUuid = null;

        this.teamId = null;
        this.roleInfo = roleInfo == null ? new GRKnight.Info() : roleInfo;
        this.compass = null;

        this.stats = stats == null ? new PlayerStats() : stats;
        this.personalPermissions = personalPermissions == null ? new GPermissions(GPermissions.Definition.DEFAULT) : personalPermissions;

        this.personalListener = new GListener.PersonalListener(this);

        this.waitingItems = new ArrayList<>();
        this.waitingEffects = new ArrayList<>();
    }

    public GPlayer(String name, UUID lastUuid, String teamId, GRole.Info roleInfo, Compass compass,
                   PlayerStats stats, GPermissions personalPermissions) {
        this.name = name;
        this.lastUuid = lastUuid;

        this.teamId = teamId;
        this.roleInfo = roleInfo;
        this.compass = compass;

        this.stats = stats == null ? new PlayerStats() : stats;
        this.personalPermissions = personalPermissions == null ? new GPermissions(GPermissions.Definition.DEFAULT) : personalPermissions;

        this.personalListener = new GListener.PersonalListener(this);

        this.waitingItems = new ArrayList<>();
        this.waitingEffects = new ArrayList<>();
    }

    public Player getPlayer() {
        return Bukkit.getPlayerExact(name);
    }

    public boolean hasPermission(GPermissions.Type permissionType, Location loc) {
        return hasPermission(permissionType, loc, 0);
    }

    public boolean hasPermission(GPermissions.Type permissionType, Location loc, int incrementTeamRadius) {
        if (personalPermissions.getPermission(permissionType) != GPermissions.Definition.DEFAULT)
            return personalPermissions.getPermission(permissionType) == GPermissions.Definition.ON;
        if (getTeam() != null && getTeam().getPermissions().getPermission(permissionType) != GPermissions.Definition.DEFAULT)
            return getTeam().getPermissions().getPermission(permissionType) == GPermissions.Definition.ON;
        if (getManager().getPriority().getPermission(permissionType) != GPermissions.Definition.DEFAULT)
            return getManager().getPriority().getPermission(permissionType) == GPermissions.Definition.ON;
        if (getZone(loc, incrementTeamRadius) != null)
            switch (getZone(loc, incrementTeamRadius).getType()) {
                case LOBBY:
                    if (getManager().getLobby().getPermissions().getPermission(permissionType) == GPermissions.Definition.DEFAULT)
                        break;
                    return getManager().getLobby().getPermissions().getPermission(permissionType) == GPermissions.Definition.ON;

                case SPAWN:
                    if (getManager().getSpawn().getPermissions().getPermission(permissionType) == GPermissions.Definition.DEFAULT)
                        break;
                    return getManager().getSpawn().getPermissions().getPermission(permissionType) == GPermissions.Definition.ON;

                case ZONE:
                    for (GZone zone : getManager().getNormalZones())
                        if (zone.isInside(loc))
                            if (zone.getPermissions().getPermission(permissionType) != GPermissions.Definition.DEFAULT)
                                return zone.getPermissions().getPermission(permissionType) == GPermissions.Definition.ON;
                    break;

                case FRIENDLY:
                    if (getManager().getFriendly().getPermission(permissionType) == GPermissions.Definition.DEFAULT)
                        break;
                    return getManager().getFriendly().getPermission(permissionType) == GPermissions.Definition.ON;

                case HOSTILE:
                    if (getManager().getHostile().getPermission(permissionType) == GPermissions.Definition.DEFAULT)
                        break;
                    return getManager().getHostile().getPermission(permissionType) == GPermissions.Definition.ON;

                case NEUTRAL:
                default:
                    break;
            }
        if (getManager().getNeutral().getPermission(permissionType) != GPermissions.Definition.DEFAULT)
            return getManager().getNeutral().getPermission(permissionType) == GPermissions.Definition.ON;
        return getManager().getGlobal().getPermission(permissionType) == GPermissions.Definition.ON;
    }

    public GZone getZone() {
        if (!Bukkit.getOfflinePlayer(name).isOnline())
            return null;
        return getZone(Bukkit.getPlayerExact(name).getLocation());
    }

    public GZone getZone(Location loc) {
        return getZone(loc, 0);
    }

    public GZone getZone(Location loc, int incrementTeamRadius) {
        if (getManager().getLobby().isInside(loc))
            return getManager().getLobby();
        if (getManager().getSpawn().isInside(loc))
            return getManager().getSpawn();
        if (getTeam() != null && getTeam().isInside(loc, incrementTeamRadius))
            return getTeam().getZone(true, incrementTeamRadius);
        for (GTeam team : getManager().getTeams())
            if (!team.isEliminated())
                if (team.isInside(loc, incrementTeamRadius))
                    return team.getZone(false, incrementTeamRadius);
        for (GZone zone : getManager().getNormalZones())
            if (zone.isInside(loc))
                return zone;
        return null;
    }

    public GManager getManager() {
        for (GManager manager : GManager.registered)
            if (manager.getPlayers().contains(this))
                return manager;
        return null;
    }

    public String getTeamId() {
        return teamId;
    }

    public GTeam getTeam() {
        return getManager() == null ? null : getManager().getTeam(teamId);
    }

    public void setTeam(String teamId, boolean save) {
        if (this.teamId == null && teamId == null)
            return;
        if (getTeam() != null) {
            leaveTeam(false);
            return;
        }
        this.teamId = teamId;
        getTeam().updatePlayers();
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setTeam(teamId, true).save();
        }
    }

    public void leaveTeam(boolean save) {
        GTeam tempTeam = getTeam();
        teamId = null;
        if (tempTeam != null)
            tempTeam.updatePlayers();
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setTeam(null, true).save();
        }
    }

    public GRole.Info getRoleInfo() {
        return roleInfo;
    }

    public <T> T getRoleInfoAs(Class<T> type) {
        return (T) roleInfo;
    }

    public void setRoleInfo(GRole.Info roleInfo, boolean save) {
        try {
            this.roleInfo = roleInfo == null ? (GRole.Info) GRole.Roles.DEFAULT.getInfoClass().newInstance() : roleInfo;
            if (save) {
                saveRoleInfo();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveRoleInfo() {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setRoleInfo(roleInfo, true).save();
        }
    }

    public Compass getCompass() {
        return compass;
    }

    public void setCompass(Compass compass, boolean save) {
        this.compass = compass;
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setCompass(compass, true).save();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name, boolean renameFile) {
        if (renameFile && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).getFile().renameTo(new File(getConfig(getManager().getId()).getFile().getParentFile().getPath() + "/" + name + ".yml"));
        }
        this.name = name;
    }

    public UUID getLastUuid() {
        return lastUuid;
    }

    public void setLastUuid(UUID lastUuid, boolean save) {
        this.lastUuid = lastUuid;
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setLastUuid(lastUuid, true).save();
        }
    }

    public String getDisplayName() {
        return (Vanish.isVanished(name) && Vanish.isPrefix ? Vanish.pre_suf_ix : "")
                + (getTeam() != null ? getTeam().getPrefix() : "")
                + name
                + (Vanish.isVanished(name) && !Vanish.isPrefix ? Vanish.pre_suf_ix : "")
                + "§r";
    }

    public PlayerStats getStats() {
        return stats;
    }

    public void setStats(PlayerStats stats, boolean save) {
        this.stats = stats;
        if (save)
            saveStats();
    }

    public void saveStats() {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            Config.Player config = getConfig(getManager().getId()).load();
            PlayerStats stats = config.getStats();
            for (Field f : PlayerStats.class.getDeclaredFields()) {
                f.setAccessible(true);
                try {
                    if (!Objects.equals(f.get(stats), f.get(this.stats))) {
                        config.setStats(this.stats, true).save();
                        break;
                    }
                } catch (IllegalAccessException ignored) {
                }
                f.setAccessible(false);
            }
        }
    }

    public GPermissions getPersonalPermissions() {
        return personalPermissions;
    }

    public void setPersonalPermissions(GPermissions personalPermissions, boolean save) {
        this.personalPermissions = personalPermissions;
        if (save)
            savePersonalPermissions();
    }

    public void savePersonalPermissions() {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setPermissions(personalPermissions, true).save();
        }
    }

    public GListener.PersonalListener getPersonalListener() {
        return personalListener;
    }

    public void setPersonalListener(GListener.PersonalListener personalListener) {
        this.personalListener = personalListener;
        this.personalListener.setGPlayer(this);
    }

    public List<ItemStack> getWaitingItems() {
        return waitingItems;
    }

    public void setWaitingItems(List<ItemStack> waitingItems, boolean save) {
        this.waitingItems = waitingItems;
        tryToGiveWaitingItems(save);
    }

    public void addWaitingItem(boolean save, ItemStack... items) {
        waitingItems.addAll(Arrays.asList(items));
        tryToGiveWaitingItems(save);
    }

    public void removeWaitingItem(boolean save, ItemStack... items) {
        waitingItems.removeAll(Arrays.asList(items));
        tryToGiveWaitingItems(save);
    }

    public void clearWaitingItems(boolean save) {
        waitingItems.clear();
        tryToGiveWaitingItems(save);
    }

    public void tryToGiveWaitingItems(boolean save) {
        if (getPlayer() != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<ItemStack> copy = new ArrayList<>(waitingItems);
                    waitingItems.clear();
                    getPlayer().getInventory().addItem(copy.toArray(new ItemStack[0]))
                            .forEach((i, itemStack) -> waitingItems.add(itemStack));
                }
            }.runTask(Main.instance);
        }
        if (save)
            saveWaitingItems();
    }

    public void saveWaitingItems() {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setWaitingItems(waitingItems, true).save();
        }
    }

    public List<PotionEffect> getWaitingEffects() {
        return waitingEffects;
    }

    public void setWaitingEffects(List<PotionEffect> waitingEffects, boolean save) {
        this.waitingEffects = waitingEffects;
        tryToGiveWaitingEffects(save);
    }

    public void addWaitingEffect(boolean save, PotionEffect... effects) {
        waitingEffects.addAll(Arrays.asList(effects));
        tryToGiveWaitingEffects(save);
    }

    public void removeWaitingEffect(boolean save, PotionEffect... effects) {
        waitingEffects.removeAll(Arrays.asList(effects));
        tryToGiveWaitingEffects(save);
    }

    public void clearWaitingEffects(boolean save) {
        waitingEffects.clear();
        tryToGiveWaitingEffects(save);
    }

    public void tryToGiveWaitingEffects(boolean save) {
        if (getPlayer() != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    waitingEffects.forEach(potionEffect -> getPlayer().addPotionEffect(potionEffect, true));
                    waitingEffects.clear();
                }
            }.runTask(Main.instance);
        }
        if (save)
            saveWaitingEffects();
    }

    public void saveWaitingEffects() {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setWaitingEffects(waitingEffects, true).save();
        }
    }

    public void saveInventory(@Nullable String id, @Nullable String name, @Nullable String creator, PlayerInventory inventory) {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            List<Utils.SavedInventory> inventories = getConfig(getManager().getId()).load().getInventories();
            inventories.add(new Utils.SavedInventory(id + "", name, creator, false,
                    Arrays.asList(inventory.getContents()), inventory.getArmorContents()));
            getConfig(getManager().getId()).load().setInventories(inventories, true).save();
        }
    }

    public Utils.SavedInventory getInventory(String id, int idx, boolean delete) {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            List<Utils.SavedInventory> inventories = getConfig(getManager().getId()).load().getInventories();
            try {
                Utils.SavedInventory inventory = inventories.stream().filter(inv -> inv.getId().equals(id + ""))
                        .collect(Collectors.toList()).get(idx);
                if (delete && inventory != null) {
                    inventories.remove(inventory);
                    getConfig(getManager().getId()).load().setInventories(inventories, true).save();
                }
                return inventory;
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        }
        return null;
    }

    public Utils.SavedInventory getLastInventory(String id, boolean delete) {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            List<Utils.SavedInventory> inventories = getConfig(getManager().getId()).load().getInventories();
            Utils.SavedInventory inventory = inventories.stream().filter(inv -> inv.getId().equals(id + ""))
                    .max(Comparator.comparing(Utils.SavedInventory::getCreation)).orElse(null);
            if (delete && inventory != null) {
                inventories.remove(inventory);
                getConfig(getManager().getId()).load().setInventories(inventories, true).save();
            }
            return inventory;
        }
        return null;
    }

    public boolean deleteInventory(String id, int idx) {
        try {
            List<Utils.SavedInventory> inventories = getConfig(getManager().getId()).load().getInventories();
            Utils.SavedInventory inventory = inventories.stream().filter(inv -> inv.getId().equals(id + ""))
                    .collect(Collectors.toList()).get(idx);
            inventories.remove(inventory);
            getConfig(getManager().getId()).load().setInventories(inventories, true).save();
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean deleteLastInventory(String id) {
        List<Utils.SavedInventory> inventories = getConfig(getManager().getId()).load().getInventories();
        Utils.SavedInventory inventory = inventories.stream().filter(inv -> inv.getId().equals(id + ""))
                .max(Comparator.comparing(Utils.SavedInventory::getCreation)).orElse(null);
        if (inventory == null)
            return false;
        inventories.remove(inventory);
        getConfig(getManager().getId()).load().setInventories(inventories, true).save();
        return true;
    }

    public int deleteAllInventories(String id) {
        List<Utils.SavedInventory> inventories = getConfig(getManager().getId()).load().getInventories();
        Stream<Utils.SavedInventory> s = inventories.stream().filter(inv -> inv.getId().equals(id + ""));
        int size = (int) s.count();
        s.forEach(inventories::remove);
        if (size > 0)
            getConfig(getManager().getId()).load().setInventories(inventories, true).save();
        return size;
    }

    public List<Utils.SavedInventory> getInventories() {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            return getConfig(getManager().getId()).load().getInventories();
        }
        return new ArrayList<>();
    }

    public void clearInventories() {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setInventories(new ArrayList<>(), true).save();
        }
    }
}
