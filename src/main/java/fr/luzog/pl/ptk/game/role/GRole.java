package fr.luzog.pl.ptk.game.role;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.GPlayer;
import fr.luzog.pl.ptk.utils.Color;
import fr.luzog.pl.ptk.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GRole {
    public static enum Roles {
        DEFAULT("no-role", new GRole("§cAucun Role", "Vous n'avez tous aucun rôle durant la première phase de jeu. "
                + "Ne vous inquiètez pas, ça arrive !"), GRole.Info.class),
        KING("king", new GRKing(), GRKing.Info.class),
        KNIGHT("knight", new GRKnight(), GRKnight.Info.class),
        SQUIRE("squire", new GRSquire(), GRSquire.Info.class),
        WIZZARD("wizzard", new GRWizzard(), GRWizzard.Info.class),
        WITCH("witch", new GRWitch(), GRWitch.Info.class),
        ARCHER("archer", new GRArcher(), GRArcher.Info.class),
        GUARD("guard", new GRGuard(), GRGuard.Info.class),
        FREEBOOTER("freebooter", new GRFreebooter(), GRFreebooter.Info.class),
        PYROMANIAC("pyromaniac", new GRPyromaniac(), GRPyromaniac.Info.class),
        ;

        private final String id;
        private final GRole role;
        private final Class infoClass;

        Roles(String id, GRole role, Class infoClass) {
            this.id = id;
            this.role = role;
            this.infoClass = infoClass;
        }

        public String getId() {
            return id;
        }

        public GRole getRole() {
            return role;
        }

        public Class getInfoClass() {
            return infoClass;
        }

        public static Roles fromId(String id) {
            for (Roles role : Roles.values()) {
                if (role.getId().equalsIgnoreCase(id)) {
                    return role;
                }
            }
            return null;
        }
    }

    public static class Info {
        private Roles _roleType;

        /**
         * > <u>NotNull</u> <br>
         * <br>
         * <b>Default:</b> 0 <br>
         * <br>
         * The player will have the role's relative health. <br>
         * >> <code>health = role's health + healthModifier</code>
         */
        private double healthModifier;
        /**
         * > <u>Nullable</u> <br>
         * <br>
         * <b>Default:</b> null <br>
         * <br>
         * If null, the player will have the role's armorLimit. <br>
         * Else, the player will have this armorLimit. <br>
         * >> <code>limit = this limit != null ? this limit : role's limit</code>
         */
        private Integer armorLimit;

        public Info() {
            this._roleType = Roles.DEFAULT;
        }

        public Info(Roles roleType) {
            this._roleType = roleType;
            this.healthModifier = 0;
            this.armorLimit = null;
        }

        public Info(Roles roleType, double healthModifier, Integer armorLimit) {
            this._roleType = roleType;
            this.healthModifier = healthModifier;
            this.armorLimit = armorLimit;
        }

        public void tick(GPlayer gp) {
            _roleType.getRole().tick(this, gp);
        }

        public Roles getRoleType() {
            return _roleType;
        }

        public void setRoleType(Roles roleType) {
            this._roleType = roleType;
        }

        public double getHealthModifier() {
            return healthModifier;
        }

        public void setHealthModifier(double healthModifier) {
            this.healthModifier = healthModifier;
        }

        public Integer getArmorLimit() {
            return armorLimit;
        }

        public void setArmorLimit(Integer armorLimit) {
            this.armorLimit = armorLimit;
        }

        public double getFinalHealth() {
            return _roleType.getRole().getHealth() + healthModifier;
        }

        public int getFinalArmorLimit() {
            return armorLimit != null ? armorLimit : _roleType.getRole().getArmorLimit();
        }

        public static Info anyRoleInfoFromMap(Map<String, Object> map) {
            try {
                Roles role = Roles.fromId((String) map.get("class"));
                if (role == null)
                    role = Roles.DEFAULT;
                return (Info) role.getInfoClass().getMethod("fromMap", Map.class).invoke(null, map);
            } catch (Exception e) {
                System.out.println(Color.RED + "Error while loading a RoleInfo: " + e.getMessage());
                e.printStackTrace();
                System.out.println("So the object will be ignored.\nStop modifying the configs... lol. ^^" + Color.RESET);
                return null;
            }
        }

        public static Info fromMap(Map<String, Object> map) {
            double healthModifier = map.get("health-modifier") != null ? Double.parseDouble(map.get("health-modifier") + "") : 0;
            int armorLimit = map.get("armor-limit") != null ? Integer.parseInt(map.get("armor-limit") + "") : 0;
            return new Info(Roles.DEFAULT, healthModifier, armorLimit);
        }

        public LinkedHashMap<String, Object> toMap() {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("class", _roleType.getId());
            return map;
        }
    }

    public static interface RoleRunnable {
        /**
         * Runs the task <b><u>without synchronous check</u></b> (runs only the operations).
         *
         * @param players The concerned players.
         */
        public void run(List<GPlayer> players);

        /**
         * Runs the task <b><u>synchronously</u></b> (runs the operations <b>IN</b> a BukkitRunnable).
         *
         * @param plugin  The plugin to run the task (for the BukkitRunnable).
         * @param players The concerned players.
         */
        public default void runTask(Plugin plugin, List<GPlayer> players) {
            RoleRunnable runnable = this;
            new BukkitRunnable() {
                @Override
                public void run() {
                    runnable.run(players);
                }
            }.runTask(plugin);
        }
    }

    public static final int DESC_LINE_LENGTH = 32;
    public static final int ABILITY_LINE_LENGTH = 36;

    private String name;
    private ItemStack base;
    private String description;
    private float health;
    /**
     * Armor limit is the pieces of armor that the player <b><i><u>CANNOT</u></i></b> wear.<br>
     * > &nbsp; <b><code>0b0000_0000_0000_0001</code></b> &nbsp; = &nbsp; Leather Helmet<br>
     * > &nbsp; <b><code>0b0000_0000_0000_0010</code></b> &nbsp; = &nbsp; Leather Chestplate<br>
     * > &nbsp; <b><code>0b0000_0000_0000_0100</code></b> &nbsp; = &nbsp; Leather Leggings<br>
     * > &nbsp; <b><code>0b0000_0000_0000_1000</code></b> &nbsp; = &nbsp; Leather Boots<br>
     * > &nbsp; <b><code>0b0000_0000_0001_0000</code></b> &nbsp; = &nbsp; Iron Helmet<br>
     * > &nbsp; <b><code>0b0000_0000_0010_0000</code></b> &nbsp; = &nbsp; Iron Chestplate<br>
     * > &nbsp; <b><code>0b0000_0000_0100_0000</code></b> &nbsp; = &nbsp; Iron Leggings<br>
     * > &nbsp; <b><code>0b0000_0000_1000_0000</code></b> &nbsp; = &nbsp; Iron Boots<br>
     * > &nbsp; <b><code>0b0000_0001_0000_0000</code></b> &nbsp; = &nbsp; Gold Helmet<br>
     * > &nbsp; <b><code>0b0000_0010_0000_0000</code></b> &nbsp; = &nbsp; Gold Chestplate<br>
     * > &nbsp; <b><code>0b0000_0100_0000_0000</code></b> &nbsp; = &nbsp; Gold Leggings<br>
     * > &nbsp; <b><code>0b0000_1000_0000_0000</code></b> &nbsp; = &nbsp; Gold Boots<br>
     * > &nbsp; <b><code>0b0001_0000_0000_0000</code></b> &nbsp; = &nbsp; Diamond Helmet<br>
     * > &nbsp; <b><code>0b0010_0000_0000_0000</code></b> &nbsp; = &nbsp; Diamond Chestplate<br>
     * > &nbsp; <b><code>0b0100_0000_0000_0000</code></b> &nbsp; = &nbsp; Diamond Leggings<br>
     * > &nbsp; <b><code>0b1000_0000_0000_0000</code></b> &nbsp; = &nbsp; Diamond Boots<br>
     */
    private int armorLimit;
    private List<Utils.PermaEffect> permaEffects;
    private List<Material> materialLimit;
    private Map<Enchantment, Integer> enchantLimit;
    private Map<PotionEffectType, Integer> potionLimit;
    private Map<Integer, RoleRunnable> daysRunnables;
    private ItemStack ability1, ability2, ability3, ability4;

    public GRole(String name, String description) {
        this.name = name;
        this.base = new ItemStack(Material.BARRIER);
        this.description = description;
        this.health = 20;
        this.armorLimit = 0b0100_0000_0000_0000;
        this.permaEffects = new ArrayList<>();
        this.materialLimit = new ArrayList<>();
        addMaterialLimit(Material.FISHING_ROD, Material.WEB, Material.TNT);
        this.enchantLimit = new HashMap<>();
        setEnchantLimit(Enchantment.FIRE_ASPECT, 0);
        setEnchantLimit(Enchantment.KNOCKBACK, 0);
        setEnchantLimit(Enchantment.ARROW_FIRE, 0);
        setEnchantLimit(Enchantment.ARROW_KNOCKBACK, 0);
        setEnchantLimit(Enchantment.ARROW_INFINITE, 0);
        setEnchantLimit(Enchantment.DURABILITY, 1);
        this.potionLimit = new HashMap<>();
        setPotionLimit(PotionEffectType.INCREASE_DAMAGE, -1);
        setPotionLimit(PotionEffectType.HARM, 0);
        this.daysRunnables = new HashMap<>();
        this.ability1 = null;
        this.ability2 = null;
        this.ability3 = null;
        this.ability4 = null;
    }

    public GRole(String name, ItemStack base, String description, float health, int armorLimit,
                 List<Utils.PermaEffect> permaEffects, List<Material> materialLimit,
                 Map<Enchantment, Integer> enchantLimit, Map<PotionEffectType, Integer> potionLimit,
                 Map<Integer, RoleRunnable> daysRunnables, ItemStack ability1, ItemStack ability2,
                 ItemStack ability3, ItemStack ability4) {
        this.name = name;
        this.base = base;
        this.description = description;
        this.health = health;
        this.armorLimit = armorLimit;
        this.permaEffects = permaEffects;
        this.materialLimit = materialLimit;
        this.enchantLimit = enchantLimit;
        this.potionLimit = potionLimit;
        this.daysRunnables = daysRunnables;
        this.ability1 = ability1;
        this.ability2 = ability2;
        this.ability3 = ability3;
        this.ability4 = ability4;
    }

    /**
     * The updates made to the role
     */
    public void tick() {
        // The updates made to the role
    }

    /**
     * The updates made to the player, linked to the role.
     *
     * @param roleInfo The role info of the player
     * @param gp       The player to update
     */
    public void tick(Info roleInfo, GPlayer gp) {
        Player p = gp.getPlayer();
        if (p != null) {
            double health = roleInfo != null ? roleInfo.getFinalHealth() : this.health;
            if (p.getMaxHealth() != health)
                p.setMaxHealth(health);
            new BukkitRunnable() {
                @Override
                public void run() {
                    new ArrayList<>(permaEffects).forEach(e ->
                            p.addPotionEffect(e.toPotionEffect(260), true));
                }
            }.runTask(Main.instance);
        }
    }

    public void checkForArmor(Info roleInfo, Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<ItemStack> toDrop = new ArrayList<>();
                ItemStack[] armor = p.getInventory().getArmorContents();
                for (int i = 0; i < armor.length; i++) {
                    if (!checkForItem(roleInfo, armor[i])) {
                        toDrop.add(armor[i]);
                        armor[i] = null;
                    }
                }
                p.getInventory().setArmorContents(armor);
                toDrop.forEach(i -> p.getWorld().dropItemNaturally(p.getLocation(), i));
            }
        }.runTask(Main.instance);
    }

    public void checkForHand(Info roleInfo, Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack inHand = p.getInventory().getItemInHand();
                if (!checkForItem(roleInfo, inHand)) {
                    p.getWorld().dropItemNaturally(p.getLocation(), inHand);
                    p.getInventory().setItemInHand(null);
                }
            }
        }.runTask(Main.instance);
    }

    public boolean checkForItem(Info roleInfo, ItemStack is) {
        if (is == null || is.getType() == Material.AIR) {
            return true;
        }

        if (materialLimit.contains(is.getType())) {
            return false;
        }

        for (Map.Entry<Enchantment, Integer> el : enchantLimit.entrySet()) {
            if (is.containsEnchantment(el.getKey()) && is.getEnchantmentLevel(el.getKey()) > el.getValue()) {
                return false;
            }
        }

        if (is.getType() == Material.POTION) {
            Potion p = Potion.fromItemStack(is);
            for (PotionEffect pe : p.getEffects()) {
                if (potionLimit.containsKey(pe.getType()) && pe.getAmplifier() > potionLimit.get(pe.getType())) {
                    return false;
                }
            }
        }

        int val;
        if (is.getType() == Material.LEATHER_HELMET) {
            val = 0b0000_0000_0000_0001;
        } else if (is.getType() == Material.LEATHER_CHESTPLATE) {
            val = 0b0000_0000_0000_0010;
        } else if (is.getType() == Material.LEATHER_LEGGINGS) {
            val = 0b0000_0000_0000_0100;
        } else if (is.getType() == Material.LEATHER_BOOTS) {
            val = 0b0000_0000_0000_1000;
        } else if (is.getType() == Material.IRON_HELMET) {
            val = 0b0000_0000_0001_0000;
        } else if (is.getType() == Material.IRON_CHESTPLATE) {
            val = 0b0000_0000_0010_0000;
        } else if (is.getType() == Material.IRON_LEGGINGS) {
            val = 0b0000_0000_0100_0000;
        } else if (is.getType() == Material.IRON_BOOTS) {
            val = 0b0000_0000_1000_0000;
        } else if (is.getType() == Material.GOLD_HELMET) {
            val = 0b0000_0001_0000_0000;
        } else if (is.getType() == Material.GOLD_CHESTPLATE) {
            val = 0b0000_0010_0000_0000;
        } else if (is.getType() == Material.GOLD_LEGGINGS) {
            val = 0b0000_0100_0000_0000;
        } else if (is.getType() == Material.GOLD_BOOTS) {
            val = 0b0000_1000_0000_0000;
        } else if (is.getType() == Material.DIAMOND_HELMET) {
            val = 0b0001_0000_0000_0000;
        } else if (is.getType() == Material.DIAMOND_CHESTPLATE) {
            val = 0b0010_0000_0000_0000;
        } else if (is.getType() == Material.DIAMOND_LEGGINGS) {
            val = 0b0100_0000_0000_0000;
        } else if (is.getType() == Material.DIAMOND_BOOTS) {
            val = 0b1000_0000_0000_0000;
        } else {
            val = 0b0000_0000_0000_0000;
        }

        int armorLimit = roleInfo != null ? roleInfo.getFinalArmorLimit() : this.armorLimit;
        if ((armorLimit & val) != 0) {
            return false;
        }

        for (Map.Entry<Enchantment, Integer> el : enchantLimit.entrySet()) {
            if (is.containsEnchantment(el.getKey()) && is.getEnchantmentLevel(el.getKey()) > el.getValue()) {
                return false;
            }
        }

        return true;
    }

//    public static GRole anyRoleFromMap(Map<String, Object> map) {
//        try {
//            String type = (String) map.get("class");
//            Class clazz = Class.forName("fr.luzog.pl.ptk.game.role." + type);
//            return (GRole) clazz.getMethod("fromMap", Map.class).invoke(null, map);
//        } catch (Exception e) {
//            System.out.println(Color.RED + "Error while loading a GRole: " + e.getMessage());
//            e.printStackTrace();
//            System.out.println("So the object will be ignored.\nStop modifying the configs... lol. ^^" + Color.RESET);
//            return null;
//        }
//    }
//
//    public static GRole fromMap(Map<String, Object> map) {
//        try {
//            String name = (String) map.get("name");
//            String description = (String) map.get("description");
//            float healthModifier = Float.parseFloat("" + map.get("health-modifier"));
//            int armorLimit = (int) map.get("armor-limit");
//            List<Map<String, Object>> effects = (List<Map<String, Object>>) map.get("effects");
//            Map<String, Object> enchantLimit = map.get("enchant-limit") instanceof Map ? (Map<String, Object>) map.get("enchant-limit")
//                    : ((MemorySection) map.get("enchant-limit")).getValues(false);
//            Map<String, Object> daysRunnables = map.get("days-runnables") instanceof Map ? (Map<String, Object>) map.get("days-runnables")
//                    : ((MemorySection) map.get("days-runnables")).getValues(false);
//            List<Utils.EffectItem> effectItems = new ArrayList<>();
//            effects.forEach(e -> effectItems.add(Utils.EffectItem.fromMap(e)));
//            Map<Enchantment, Integer> enchantLimitMap = new HashMap<>();
//            enchantLimit.forEach((e, l) -> enchantLimitMap.put(Enchantment.getByName(e), Integer.parseInt(l + "")));
//            Map<Integer, BukkitRunnable> daysRunnablesMap = new HashMap<>();
//            daysRunnables.forEach((d, r) -> daysRunnablesMap.put(Integer.parseInt(d), Utils.objectFromBase64(r + "", BukkitRunnable.class)));
//            return new GRole(name, description, healthModifier, armorLimit, effectItems, enchantLimitMap, daysRunnablesMap);
//        } catch (Exception e) {
//            System.out.println(Color.RED + "Error while loading a GRole: " + e.getMessage());
//            e.printStackTrace();
//            System.out.println("So the object will be ignored.\nStop modifying the configs... lol. ^^" + Color.RESET);
//            return null;
//        }
//    }
//
//    public LinkedHashMap<String, Object> toMap() {
//        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
//        map.put("class", this.getClass().getSimpleName());
//        map.put("name", name);
//        map.put("description", description);
//        map.put("health-modifier", healthModifier);
//        map.put("armor-limit", armorLimit);
//        List<Map<String, Object>> effects = new ArrayList<>();
//        this.effects.forEach(e -> effects.add(e.toMap()));
//        map.put("effects", effects);
//        Map<String, Object> enchantLimit = new HashMap<>();
//        this.enchantLimit.forEach((e, l) -> enchantLimit.put(e.getName(), l));
//        map.put("enchant-limit", enchantLimit);
//        Map<String, Object> daysRunnables = new HashMap<>();
//        this.daysRunnables.forEach((d, r) -> daysRunnables.put(d + "", Utils.objectToBase64(r)));
//        map.put("days-runnables", daysRunnables);
//        return map;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getBase() {
        return base;
    }

    public void setBase(ItemStack base) {
        this.base = base;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getArmorLimit() {
        return armorLimit;
    }

    public void setArmorLimit(int armorLimit) {
        this.armorLimit = armorLimit;
    }

    public List<Utils.PermaEffect> getPermaEffects() {
        return permaEffects;
    }

    public void setPermaEffects(List<Utils.PermaEffect> permaEffects) {
        this.permaEffects = permaEffects;
    }

    public void addPermaEffects(Utils.PermaEffect... permaEffects) {
        Collections.addAll(this.permaEffects, permaEffects);
    }

    public void removePermaEffect(Utils.PermaEffect permaEffects) {
        this.permaEffects.remove(permaEffects);
    }

    public void removePermaEffects(PotionEffectType type) {
        this.permaEffects.removeIf(e -> e.getType().equals(type));
    }

    public List<Material> getMaterialLimit() {
        return materialLimit;
    }

    public void setMaterialLimit(List<Material> materialLimit) {
        this.materialLimit = materialLimit;
    }

    public void addMaterialLimit(Material... materialLimit) {
        Collections.addAll(this.materialLimit, materialLimit);
    }

    public void removeMaterialLimit(Material material) {
        this.materialLimit.remove(material);
    }

    public Map<Enchantment, Integer> getEnchantLimit() {
        return enchantLimit;
    }

    public void setEnchantLimit(Map<Enchantment, Integer> enchantLimit) {
        this.enchantLimit = enchantLimit;
    }

    public void setEnchantLimit(Enchantment enchant, int limit) {
        if (this.enchantLimit.containsKey(enchant))
            this.enchantLimit.replace(enchant, limit);
        else
            this.enchantLimit.put(enchant, limit);
    }

    public void removeEnchantLimit(Enchantment enchant) {
        this.enchantLimit.remove(enchant);
    }

    public Map<PotionEffectType, Integer> getPotionLimit() {
        return potionLimit;
    }

    public void setPotionLimit(Map<PotionEffectType, Integer> potionLimit) {
        this.potionLimit = potionLimit;
    }

    public void setPotionLimit(PotionEffectType potion, int limit) {
        if (this.potionLimit.containsKey(potion))
            this.potionLimit.replace(potion, limit);
        else
            this.potionLimit.put(potion, limit);
    }

    public void removePotionLimit(PotionEffectType potion) {
        this.potionLimit.remove(potion);
    }

    public Map<Integer, RoleRunnable> getDaysRunnables() {
        return daysRunnables;
    }

    public void setDaysRunnables(Map<Integer, RoleRunnable> daysRunnables) {
        this.daysRunnables = daysRunnables;
    }

    public void setDaysRunnable(int day, RoleRunnable runnable) {
        if (this.daysRunnables.containsKey(day))
            this.daysRunnables.replace(day, runnable);
        else
            this.daysRunnables.put(day, runnable);
    }

    public void removeDaysRunnable(int day) {
        this.daysRunnables.remove(day);
    }

    public ItemStack getAbility1() {
        return ability1;
    }

    public void setAbility1(ItemStack ability1) {
        this.ability1 = ability1;
    }

    public ItemStack getAbility2() {
        return ability2;
    }

    public void setAbility2(ItemStack ability2) {
        this.ability2 = ability2;
    }

    public ItemStack getAbility3() {
        return ability3;
    }

    public void setAbility3(ItemStack ability3) {
        this.ability3 = ability3;
    }

    public ItemStack getAbility4() {
        return ability4;
    }

    public void setAbility4(ItemStack ability4) {
        this.ability4 = ability4;
    }
}
