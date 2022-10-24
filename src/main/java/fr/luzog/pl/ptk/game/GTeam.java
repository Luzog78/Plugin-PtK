package fr.luzog.pl.ptk.game;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.utils.Broadcast;
import fr.luzog.pl.ptk.utils.Config;
import fr.luzog.pl.ptk.utils.SpecialChars;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GTeam {

    public static final String GODS_ID = "gods", GODS_FILE = "Gods.yml",
            SPECS_ID = "specs", SPECS_FILE = "Specs.yml";
    public static double TEAM_RADIUS = 8;

    public void saveToConfig(String gameId, boolean soft) {
        if (soft)
            return;

        getConfig(gameId)
                .load()

                .setName(name, true)
                .setPrefix(prefix, true)
                .setColor(color.name(), true)
                .setRadius(radius, true)
                .setEliminated(isEliminated, true)
                .setEliminators(eliminators, true)
                .setOldPlayers(oldPlayers, true)
                .setSpawn(spawn, true)
                .setPermissions(permissions, true)

                .save();
    }

    public Config.Team getConfig(String gameId) {
        return new Config.Team("game-" + gameId + "/teams/" + (id.equalsIgnoreCase(GODS_ID) ? GODS_FILE : id.equalsIgnoreCase(SPECS_ID) ? SPECS_FILE : id + ".yml"));
    }

    private String id, name, prefix, eliminators;
    private ChatColor color;
    private Location spawn;
    private double radius;
    private ArrayList<String> oldPlayers;
    private boolean isEliminated;

    private Team scoreboardTeam;

    private GPermissions permissions;

    public GTeam(String id) {
        this.id = id;
        this.name = id;
        this.prefix = id + " ";
        this.eliminators = null;
        this.color = ChatColor.WHITE;
        this.spawn = new Location(Main.world, 0, 0, 0);
        this.radius = 0;
        this.oldPlayers = new ArrayList<>();
        this.isEliminated = false;
        this.permissions = new GPermissions(GPermissions.Definition.DEFAULT);
        scoreboardTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("gt" + UUID.randomUUID().toString().substring(0, 4) + ":" + id);
        updateParams();
    }

    public GTeam(String id, String name, String prefix, String eliminators, ChatColor color, Location spawn,
                 Location plunderLoc, double radius, ArrayList<String> oldPlayers, boolean isEliminated,
                 long defaultEliminationCooldown, long eliminationCooldown, GPermissions permissions) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.eliminators = eliminators;
        this.color = color;
        this.spawn = spawn;
        this.radius = radius;
        this.oldPlayers = oldPlayers == null ? new ArrayList<>() : oldPlayers;
        this.isEliminated = isEliminated;
        this.permissions = permissions;
        scoreboardTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("gt" + UUID.randomUUID().toString().substring(0, 4) + ":" + id);
        updateParams();
    }

    public boolean everyoneIsNearOf(Location loc) {
        return getPlayers().stream().allMatch(p -> {
            Double d;
            return getManager().getState() == GManager.State.RUNNING
                    && (p.getPlayer() == null || ((d = Utils.safeDistance(p.getPlayer().getLocation(),
                    loc, true)) != null && d <= 5.5));
        });
    }

    public void eliminate(String eliminators, boolean broadcast, boolean checkForTheOther, boolean save) {
        setEliminators(eliminators, false);
        isEliminated = true;
        getPlayers().forEach(p -> {
            oldPlayers.add(p.getName());
            if (broadcast && p.getPlayer() != null)
                p.getPlayer().sendMessage(Main.PREFIX + "§4§lVous avez été éliminé de la partie.");
            p.leaveTeam(false);
            p.setTeam(GTeam.SPECS_ID, true);
            if (p.getPlayer() != null) {
                p.getPlayer().setGameMode(GameMode.SPECTATOR);
                if (getManager().getSpawn().getSpawn() != null)
                    p.getPlayer().teleport(getManager().getSpawn().getSpawn());
                else if (getManager().getLobby().getSpawn() != null)
                    p.getPlayer().teleport(getManager().getLobby().getSpawn());
            }
        });
        Broadcast.announcement(Main.PREFIX + "§r§lLa team " + getName() + "§r a été !éliminée §lde la partie."
                + "\nDonc !" + oldPlayers.size() + " !joueurs ont été éliminés.");
        if (save && getManager() != null)
            saveToConfig(getManager().getId(), false);
        if (checkForTheOther) {
            long teams = getManager().getTeams().stream().filter(t -> !t.getId().equals(GODS_ID)
                    && !t.getId().equals(SPECS_ID) && !t.isEliminated()).count();
            if (teams == 0 || teams == 1)
                getManager().end();
            else
                Broadcast.log("§eIl reste !" + teams + " !équipes en jeu !");
        }
    }

    public void reintroduce(boolean broadcast, boolean save) {
        isEliminated = false;
        int count = oldPlayers.size();
        oldPlayers.forEach(pName -> {
            GPlayer p = getManager().getPlayer(pName, true);
            if (broadcast && p.getPlayer() != null)
                p.getPlayer().sendMessage(Main.PREFIX + "§2§lVous avez été réintégré à la partie.");
            p.leaveTeam(false);
            p.setTeam(id, true);
            if (p.getPlayer() != null) {
                p.getPlayer().setGameMode(GameMode.SURVIVAL);
                if (spawn != null)
                    p.getPlayer().teleport(spawn);
                else if (getManager().getSpawn().getSpawn() != null)
                    p.getPlayer().teleport(getManager().getSpawn().getSpawn());
                else if (getManager().getLobby().getSpawn() != null)
                    p.getPlayer().teleport(getManager().getLobby().getSpawn());
            }
        });
        oldPlayers.clear();
        Broadcast.announcement(Main.PREFIX + "§r§lLa team " + name + " a été !réintégrée à la partie."
                + "\nDonc !" + count + " !joueurs sont de retour.");
        if (save && getManager() != null)
            saveToConfig(getManager().getId(), false);
    }

    public void wall(int height, Material material) {
        List<Material> transparent = Arrays.asList(Material.AIR, Material.LONG_GRASS, Material.SNOW,
                Material.LEAVES, Material.LEAVES_2, Material.SAPLING, Material.DEAD_BUSH,
                Material.YELLOW_FLOWER, Material.RED_ROSE, Material.BROWN_MUSHROOM,
                Material.RED_MUSHROOM, Material.DOUBLE_PLANT, Material.TORCH);
        int minX = spawn.clone().subtract(radius, 0, 0).getBlockX(), maxX = spawn.clone().add(radius, 0, 0).getBlockX() - 1,
                minZ = spawn.clone().subtract(0, 0, radius).getBlockZ(), maxZ = spawn.clone().add(0, 0, radius).getBlockZ() - 1;
        for (int x = minX; x <= maxX; x++)
            for (int z = minZ; z <= maxZ; z++)
                if (x == minX || x == maxX || z == minZ || z == maxZ)
                    for (int y = 254; y > 0; y--)
                        if (!transparent.contains(spawn.getWorld().getBlockAt(x, y, z).getType())) {
                            for (int i = height >= 0 ? 0 : -1; (height >= 0) == (i < height); i += height >= 0 ? 1 : -1)
                                if (y + 1 + i < 256 && (height < 0 || transparent.contains(spawn.getWorld()
                                        .getBlockAt(x, y + 1 + i, z).getType())))
                                    spawn.getWorld().getBlockAt(x, y + 1 + i, z)
                                            .setType(material, true);
                            break;
                        }
    }

    public void altar() {
//        It jams the players.. :/
//        spawn.getBlock().setType(Material.FENCE, true);
//        spawn.clone().add(0, 1, 0).getBlock().setType(Material.FENCE, true);
        spawn.clone().add(0, 2, 0).getBlock().setType(Material.FENCE, true);
        spawn.clone().add(0, 3, 0).getBlock().setType(Material.FENCE, true);

        spawn.clone().add(1, 2, 0).getBlock().setType(Material.WOOL, true);
        spawn.clone().add(1, 2, 0).getBlock().setData(Utils.chatToDataColor(color));
        spawn.clone().add(1, 3, 0).getBlock().setType(Material.WOOL, true);
        spawn.clone().add(1, 3, 0).getBlock().setData(Utils.chatToDataColor(color));
        spawn.clone().add(-1, 2, 0).getBlock().setType(Material.WOOL, true);
        spawn.clone().add(-1, 2, 0).getBlock().setData(Utils.chatToDataColor(color));
        spawn.clone().add(-1, 3, 0).getBlock().setType(Material.WOOL, true);
        spawn.clone().add(-1, 3, 0).getBlock().setData(Utils.chatToDataColor(color));
    }

    public boolean isInside(Location loc) {
        return isInside(loc, 0);
    }

    public boolean isInside(Location loc, int incrementTeamRadius) {
        Location l1 = getSpawn().clone(), l2 = getSpawn().clone();
        l1.setY(-1);
        l2.setY(256);
        return Utils.isInside(loc, l1.add(radius + incrementTeamRadius, 0, radius + incrementTeamRadius),
                l2.subtract(radius + incrementTeamRadius, 0, radius + incrementTeamRadius));
    }

    public GZone getZone(boolean friendly) {
        return getZone(friendly, 0);
    }

    public GZone getZone(boolean friendly, int incrementTeamRadius) {
        return new GZone(getId(), friendly ? GZone.Type.FRIENDLY : GZone.Type.HOSTILE, getSpawn().clone(),
                getSpawn().clone().subtract(radius + incrementTeamRadius, radius + incrementTeamRadius,
                        radius + incrementTeamRadius), getSpawn().clone().add(radius + incrementTeamRadius,
                radius + incrementTeamRadius, radius + incrementTeamRadius), getPermissions());
    }

    public GManager getManager() {
        for (GManager manager : GManager.registered)
            for (GTeam team : manager.getTeams())
                if (team.equals(this))
                    return manager;
        return null;
    }

    public void updatePlayers() {
        scoreboardTeam.getPlayers().forEach(p -> scoreboardTeam.removePlayer(p));
        scoreboardTeam.getEntries().forEach(e -> scoreboardTeam.addEntry(e));
        getPlayers().forEach(p -> {
            if (p.getName() != null)
                scoreboardTeam.addPlayer(Bukkit.getOfflinePlayer(p.getName()));
        });
    }

    public void updateParams() {
        scoreboardTeam.setDisplayName(getName());
        scoreboardTeam.setPrefix(getPrefix());
        scoreboardTeam.setNameTagVisibility(NameTagVisibility.ALWAYS);
        scoreboardTeam.setCanSeeFriendlyInvisibles(false);
        scoreboardTeam.setAllowFriendlyFire(true);
    }

    public String getId() {
        return id;
    }

    public void setId(@Nonnull String id, boolean renameFile) {
        if (getManager() != null) {
            if (getManager().getTeam(id) != null && !getManager().getTeam(id).equals(this))
                throw new GException.DuplicateTeamIdException(id, getManager().getId());
            if (this.id.equalsIgnoreCase(GODS_ID) || this.id.equalsIgnoreCase(SPECS_ID))
                throw new GException.CannotChangeTeamIdException(this.id, getManager().getId());
        }
        if (renameFile && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).getFile().renameTo(new File(getConfig(getManager().getId()).getFile().getParentFile().getPath() + "/" + id + ".yml"));
        }
        this.id = id;
    }

    public String getName() {
        return color + name.replace("§r", color.toString()) + color + "§r";
    }

    public void setName(String name, boolean save) {
        this.name = name;
        updateParams();
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setName(name, true).save();
        }
    }

    public String getPrefix() {
        String p = color + prefix.replace("§r", color.toString());
        if (prefix.contains("§"))
            for (String s : prefix.toLowerCase().split("§"))
                if (!s.startsWith("r") && !s.startsWith((color.getChar() + "").toLowerCase())) {
                    p += color;
                    break;
                }
        while (p.contains(color + "" + color))
            p = p.replace(color + "" + color, color.toString());
        return p;
    }

    public void setPrefix(String prefix, boolean save) {
        this.prefix = prefix;
        updateParams();
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setPrefix(prefix, true).save();
        }
    }

    public String getEliminators() {
        return eliminators;
    }

    public void setEliminators(String eliminators, boolean save) {
        this.eliminators = eliminators;
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setEliminators(eliminators, true).save();
        }
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color, boolean save) {
        this.color = color;
        updateParams();
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setColor(color.name(), true).save();
        }
    }

    public Location getSpawn() {
        return spawn == null ? getManager() == null ? null : getManager().getSpawn().getSpawn() : spawn;
    }

    public void setSpawn(Location spawn, boolean save) {
        this.spawn = spawn;
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setSpawn(spawn, true).save();
        }
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius, boolean save) {
        this.radius = radius;
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setRadius(radius, true).save();
        }
    }

    public ArrayList<String> getOldPlayers() {
        return oldPlayers;
    }

    public void setOldPlayers(ArrayList<String> oldPlayers, boolean save) {
        this.oldPlayers = oldPlayers;
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setOldPlayers(oldPlayers, true).save();
        }
    }

    public ArrayList<GPlayer> getPlayers() {
        ArrayList<GPlayer> ps = new ArrayList<>();
        if (getManager() != null)
            ps.addAll(getManager().getPlayers());
        ps.removeIf(p -> p.getTeam() == null || !Objects.equals(p.getTeamId(), id));
        return ps;
    }

    /*
     * public FKPlayer getPlayer(UUID uuid) {
     *     for (FKPlayer player : getPlayers())
     *         if (player.getUuid().equals(uuid))
     *             return player;
     *     return null;
     * }
     */

    public GPlayer getPlayer(@Nonnull String name) {
        for (GPlayer player : getPlayers())
            if (name.equalsIgnoreCase(player.getName()))
                return player;
        return null;
    }

    /**
     * @throws GException.PlayerAlreadyInTeamException
     * @deprecated This method is deprecated. Use {@link GPlayer#setTeam(String, boolean)} instead.
     */
    @Deprecated
    public void addPlayer(GPlayer player) {
        if (player.getTeam() == null)
            player.setTeam(id, true);
        else
            throw new GException.PlayerAlreadyInTeamException(id, player.getName());
    }

    /**
     * @throws GException.PlayerNotInTeamException
     * @deprecated This method is deprecated. Use {@link GPlayer#leaveTeam(boolean)} instead.
     */
    @Deprecated
    public void removePlayer(GPlayer player) {
        if (player.getTeam() == this)
            player.leaveTeam(true);
        else
            throw new GException.PlayerNotInTeamException(id, player.getName());
    }

    public boolean isEliminated() {
        return isEliminated;
    }

    public void setEliminated(boolean isEliminated, boolean save) {
        this.isEliminated = isEliminated;
        if (save && getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setEliminated(isEliminated, true).save();
        }
    }

    public Team getScoreboardTeam() {
        return scoreboardTeam;
    }

    /**
     * @deprecated You may not use this method.
     */
    @Deprecated
    public void setScoreboardTeam(Team scoreboardTeam) {
        this.scoreboardTeam = scoreboardTeam;
    }

    public GPermissions getPermissions() {
        return permissions;
    }

    public void setPermissions(GPermissions permissions, boolean save) {
        this.permissions = permissions;
        if (save)
            savePermissions();
    }

    public void savePermissions() {
        if (getManager() != null) {
            if (!getConfig(getManager().getId()).exists())
                saveToConfig(getManager().getId(), true);
            getConfig(getManager().getId()).load().setPermissions(permissions, true).save();
        }
    }
}
