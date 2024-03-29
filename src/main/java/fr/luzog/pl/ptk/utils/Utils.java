package fr.luzog.pl.ptk.utils;

import fr.luzog.pl.ptk.Main;
import fr.luzog.pl.ptk.game.GManager;
import fr.luzog.pl.ptk.game.GPlayer;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static final String loreSeparator = "------------------";

    public static final Map<Enchantment, String> rawEnchantToCommonTermMap = new HashMap<Enchantment, String>() {{
        put(Enchantment.ARROW_DAMAGE, "Power");
        put(Enchantment.ARROW_FIRE, "Flame");
        put(Enchantment.ARROW_INFINITE, "Infinity");
        put(Enchantment.ARROW_KNOCKBACK, "Punch");
//        put(Enchantment.BINDING_CURSE, "Curse of Binding");
        put(Enchantment.DAMAGE_ALL, "Sharpness");
        put(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods");
        put(Enchantment.DAMAGE_UNDEAD, "Smite");
        put(Enchantment.DEPTH_STRIDER, "Depth Strider");
        put(Enchantment.DIG_SPEED, "Efficiency");
        put(Enchantment.DURABILITY, "Unbreaking");
        put(Enchantment.FIRE_ASPECT, "Fire Aspect");
//        put(Enchantment.FROST_WALKER, "Frost Walker");
        put(Enchantment.KNOCKBACK, "Knockback");
        put(Enchantment.LOOT_BONUS_BLOCKS, "Fortune");
        put(Enchantment.LOOT_BONUS_MOBS, "Looting");
        put(Enchantment.LUCK, "Luck of the Sea");
        put(Enchantment.LURE, "Lure");
//        put(Enchantment.MENDING, "Mending");
        put(Enchantment.OXYGEN, "Respiration");
        put(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");
        put(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection");
        put(Enchantment.PROTECTION_FALL, "Feather Falling");
        put(Enchantment.PROTECTION_FIRE, "Fire Protection");
        put(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection");
        put(Enchantment.SILK_TOUCH, "Silk Touch");
//        put(Enchantment.SWEEPING_EDGE, "Sweeping Edge");
        put(Enchantment.THORNS, "Thorns");
        put(Enchantment.WATER_WORKER, "Aqua Affinity");
    }};

    public static final Map<PotionEffectType, String> rawPotionToCommonTermMap = new HashMap<PotionEffectType, String>() {{
        put(PotionEffectType.ABSORPTION, "Absorption");
        put(PotionEffectType.BLINDNESS, "Blindness");
        put(PotionEffectType.CONFUSION, "Nausea");
        put(PotionEffectType.DAMAGE_RESISTANCE, "Resistance");
        put(PotionEffectType.FAST_DIGGING, "Haste");
        put(PotionEffectType.FIRE_RESISTANCE, "Fire Resistance");
        put(PotionEffectType.HARM, "Instant Damage");
        put(PotionEffectType.HEAL, "Instant Health");
        put(PotionEffectType.HEALTH_BOOST, "Health Boost");
        put(PotionEffectType.HUNGER, "Hunger");
        put(PotionEffectType.INCREASE_DAMAGE, "Strength");
        put(PotionEffectType.INVISIBILITY, "Invisibility");
        put(PotionEffectType.JUMP, "Jump Boost");
//        put(PotionEffectType.LEVITATION, "Levitation");
//        put(PotionEffectType.LUCK, "Luck");
        put(PotionEffectType.NIGHT_VISION, "Night Vision");
        put(PotionEffectType.POISON, "Poison");
        put(PotionEffectType.REGENERATION, "Regeneration");
        put(PotionEffectType.SATURATION, "Saturation");
        put(PotionEffectType.SLOW, "Slowness");
        put(PotionEffectType.SLOW_DIGGING, "Mining Fatigue");
        put(PotionEffectType.SPEED, "Speed");
//        put(PotionEffectType.UNLUCK, "Bad Luck");
        put(PotionEffectType.WATER_BREATHING, "Water Breathing");
        put(PotionEffectType.WEAKNESS, "Weakness");
        put(PotionEffectType.WITHER, "Wither");
    }};

    /**
     * It's a class that represents a Mojang profile
     */
    public static class MojangProfile {
        private UUID uuid;
        private String name;
        private String rawTextures;
        private String rawSignature;
        private long timestamp;
        private String profileId;
        private String profileName;
        private String skinURL;
        private String capeURL;

        public MojangProfile(UUID uuid, String name, String rawTextures, String rawSignature) {
            this.uuid = uuid;
            this.name = name;
            this.rawTextures = rawTextures;
            this.rawSignature = rawSignature;

            timestamp = 0;
            profileId = null;
            profileName = null;
            skinURL = null;
            capeURL = null;

            try {
                JSONObject obj = (JSONObject) new JSONParser().parse(new String(Base64.getDecoder().decode(this.rawTextures)));
                if (obj.containsKey("timestamp"))
                    timestamp = Long.parseLong(obj.get("timestamp").toString());
                if (obj.containsKey("profileId"))
                    profileId = obj.get("profileId").toString();
                if (obj.containsKey("profileName"))
                    profileName = obj.get("profileName").toString();
                if (obj.containsKey("textures")) {
                    JSONObject textures = (JSONObject) obj.get("textures");
                    if (textures.containsKey("SKIN")) {
                        JSONObject skin = (JSONObject) textures.get("SKIN");
                        if (skin.containsKey("url"))
                            skinURL = skin.get("url").toString();
                    }
                    if (textures.containsKey("CAPE")) {
                        JSONObject cape = (JSONObject) textures.get("CAPE");
                        if (cape.containsKey("url"))
                            capeURL = cape.get("url").toString();
                    }
                }
            } catch (ParseException ignored) {
            }
        }

        @Override
        public String toString() {
            return "MojangProfile{" +
                    "uuid=" + uuid +
                    ", name='" + name + '\'' +
                    ", rawTextures='" + rawTextures + '\'' +
                    ", rawSignature='" + rawSignature + '\'' +
                    ", timestamp=" + timestamp +
                    ", profileId='" + profileId + '\'' +
                    ", profileName='" + profileName + '\'' +
                    ", skinURL='" + skinURL + '\'' +
                    ", capeURL='" + capeURL + '\'' +
                    '}';
        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRawTextures() {
            return rawTextures;
        }

        public void setRawTextures(String rawTextures) {
            this.rawTextures = rawTextures;
        }

        public String getRawSignature() {
            return rawSignature;
        }

        public void setRawSignature(String rawSignature) {
            this.rawSignature = rawSignature;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getProfileId() {
            return profileId;
        }

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        public String getProfileName() {
            return profileName;
        }

        public void setProfileName(String profileName) {
            this.profileName = profileName;
        }

        public String getSkinURL() {
            return skinURL;
        }

        public void setSkinURL(String skinURL) {
            this.skinURL = skinURL;
        }

        public String getCapeURL() {
            return capeURL;
        }

        public void setCapeURL(String capeURL) {
            this.capeURL = capeURL;
        }
    }

    /**
     * Pair is a generic class that holds two objects of any type.
     */
    public static class Pair<A, B> {
        private A a;
        private B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A getKey() {
            return a;
        }

        public B getValue() {
            return b;
        }

        public void setKey(A a) {
            this.a = a;
        }

        public void setValue(B b) {
            this.b = b;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "a=" + a +
                    ", b=" + b +
                    '}';
        }
    }

    /**
     * Triple is a class that holds three objects of any type.
     */
    public static class Triple<A, B, C> {
        private A a;
        private B b;
        private C c;

        public Triple(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public A getA() {
            return a;
        }

        public B getB() {
            return b;
        }

        public C getC() {
            return c;
        }

        public void setA(A a) {
            this.a = a;
        }

        public void setB(B b) {
            this.b = b;
        }

        public void setC(C c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return "Triple{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    '}';
        }
    }

    public static class TripleMap<A, B, C> {
        private final ArrayList<Triple<A, B, C>> list;

        public TripleMap() {
            list = new ArrayList<>();
        }

        public void put(A a, B b, C c) {
            list.add(new Triple<>(a, b, c));
        }

        public Triple<A, B, C> get(int index) {
            return list.get(index);
        }

        public List<Triple<A, B, C>> getByA(A a) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getA().equals(a))
                    result.add(triple);
            }
            return result;
        }

        public List<Triple<A, B, C>> getByB(B b) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getB().equals(b))
                    result.add(triple);
            }
            return result;
        }

        public List<Triple<A, B, C>> getByC(C c) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getC().equals(c))
                    result.add(triple);
            }
            return result;
        }

        public List<Triple<A, B, C>> getByAB(A a, B b) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getA().equals(a) && triple.getB().equals(b))
                    result.add(triple);
            }
            return result;
        }

        public List<Triple<A, B, C>> getByAC(A a, C c) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getA().equals(a) && triple.getC().equals(c))
                    result.add(triple);
            }
            return result;
        }

        public List<Triple<A, B, C>> getByBC(B b, C c) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getB().equals(b) && triple.getC().equals(c))
                    result.add(triple);
            }
            return result;
        }

        public List<Triple<A, B, C>> getByABC(A a, B b, C c) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getA().equals(a) && triple.getB().equals(b) && triple.getC().equals(c))
                    result.add(triple);
            }
            return result;
        }

        public List<Triple<A, B, C>> getAll() {
            return list;
        }

        public void remove(int index) {
            list.remove(index);
        }

        public void remove(Triple<A, B, C> triple) {
            list.remove(triple);
        }

        public void removeByA(A a) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getA().equals(a))
                    result.add(triple);
            }
            list.removeAll(result);
        }

        public void removeByB(B b) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getB().equals(b))
                    result.add(triple);
            }
            list.removeAll(result);
        }

        public void removeByC(C c) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getC().equals(c))
                    result.add(triple);
            }
            list.removeAll(result);
        }

        public void removeByAB(A a, B b) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getA().equals(a) && triple.getB().equals(b))
                    result.add(triple);
            }
            list.removeAll(result);
        }

        public void removeByAC(A a, C c) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getA().equals(a) && triple.getC().equals(c))
                    result.add(triple);
            }
            list.removeAll(result);
        }

        public void removeByBC(B b, C c) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getB().equals(b) && triple.getC().equals(c))
                    result.add(triple);
            }
            list.removeAll(result);
        }

        public void removeByABC(A a, B b, C c) {
            List<Triple<A, B, C>> result = new ArrayList<>();
            for (Triple<A, B, C> triple : list) {
                if (triple.getA().equals(a) && triple.getB().equals(b) && triple.getC().equals(c))
                    result.add(triple);
            }
            list.removeAll(result);
        }

        public void clear() {
            list.clear();
        }

        public int size() {
            return list.size();
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }

        public boolean contains(Triple<A, B, C> triple) {
            return list.contains(triple);
        }
    }

    public static class SavedInventory {
        private String id;
        private String name;
        private long creation;
        private String creator;
        private boolean opOnly;
        private ArrayList<ItemStack> content;
        private ItemStack[] armor;

        public SavedInventory(String id, String name, long creation, String creator,
                              boolean opOnly, String content, String armor) {
            this.id = id;
            this.name = name;
            this.creation = creation;
            this.creator = creator;
            this.opOnly = opOnly;
            setRawContent(content);
            setRawArmor(armor);
        }

        public SavedInventory(String id, String name, String creator, boolean opOnly,
                              List<ItemStack> content, ItemStack[] armor) {
            this.id = id;
            this.name = name;
            this.creation = new Date().getTime();
            this.creator = creator;
            this.opOnly = opOnly;
            setContent(content);
            setArmor(armor);
        }

        public static SavedInventory fromMap(Map<String, Object> map) {
            try {
                String id = (String) map.get("id");
                String name = map.containsKey("name") ? (String) map.get("name") : null;
                long creation = map.containsKey("creation") ? (long) map.get("creation") : 0;
                String creator = map.containsKey("creator") ? (String) map.get("creator") : null;
                boolean opOnly = map.containsKey("op-only") && (boolean) map.get("op-only");
                String content = map.containsKey("content") ? (String) map.get("content") : null;
                String armor = map.containsKey("armor") ? (String) map.get("armor") : null;
                return new SavedInventory(id, name, creation, creator, opOnly, content, armor);
            } catch (Exception e) {
                System.out.println(Color.RED + "Error while loading an inventory: " + e.getMessage());
                e.printStackTrace();
                System.out.println("So the inventory will be ignored.\nStop modifying the configs... lol. ^^" + Color.RESET);
                return null;
            }
        }

        @Override
        public String toString() {
            return "SavedInventory{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", creation=" + creation +
                    ", creator='" + creator + '\'' +
                    ", opOnly=" + opOnly +
                    ", content='" + content + '\'' +
                    ", armor='" + Arrays.toString(armor) + '\'' +
                    '}';
        }

        public LinkedHashMap<String, Object> toMap() {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("creation", creation);
            map.put("creator", creator);
            map.put("op-only", opOnly);
            map.put("content", getRawContent());
            map.put("armor", getRawArmor());
            return map;
        }

        public int count() {
            int count = 0;
            for (ItemStack item : Stream.concat(content.stream(), Arrays.stream(armor)).toArray(ItemStack[]::new)) {
                if (item != null && item.getType() != Material.AIR) {
                    count += item.getAmount();
                }
            }
            return count;
        }

        /**
         * It equips the player with the current inventory.
         *
         * @param p     The player to equip the kit to
         * @param clear Whether to clear the player's inventory before equipping the kit.<br>
         *              If <i style="color: #ffffff">false</i>, the content will be <b><i>ADDED</i></b> and <b><i>not PLACED</i></b>.
         */
        public void equip(Player p, boolean clear) {
            PlayerInventory inv = p.getInventory();
            if (clear) {
                inv.clear();
                inv.setArmorContents(new ItemStack[4]);
            }
            if (content != null) {
                ItemStack[] items = content.stream().map(is -> is == null ?
                        new ItemStack(Material.AIR) : is).toArray(ItemStack[]::new);
                if (!clear)
                    inv.addItem(items);
                else
                    for (int i = 0; i < items.length && i < 36; i++)
                        inv.setItem(i, items[i]);
            }
            if (armor != null) {
                for (int i = 0; i < armor.length && i < 4; i++) {
                    if (armor[i] != null) {
                        if (inv.getArmorContents()[i] != null && inv.getArmorContents()[i].getType() != Material.AIR) {
                            inv.addItem(armor[i]);
                        } else {
                            ItemStack[] a = inv.getArmorContents().clone();
                            a[i] = armor[i];
                            inv.setArmorContents(a);
                        }
                    }
                }
            }
        }

        public ArrayList<ItemStack> getContent() {
            return content;
        }

        public ItemStack[] getArmor() {
            return armor;
        }

        public void setContent(Collection<ItemStack> content) {
            this.content = content.stream().map(is -> is == null || is.getType() == Material.AIR ?
                    null : is).collect(Collectors.toCollection(ArrayList::new));
            if (this.content.stream().noneMatch(Objects::nonNull))
                this.content = new ArrayList<>();
        }

        public void setArmor(ItemStack[] armor) {
            this.armor = new ItemStack[4];
            for (int i = 0; i < armor.length && i < 4; i++) {
                this.armor[i] = armor[i] == null || armor[i].getType() == Material.AIR ? null : armor[i];
            }
            if (Arrays.stream(this.armor).noneMatch(Objects::nonNull))
                this.armor = new ItemStack[0];
        }

        public String getRawContent() {
            return itemStackArrayToBase64(content.toArray(new ItemStack[0]));
        }

        public String getRawArmor() {
            return itemStackArrayToBase64(armor);
        }

        public void setRawContent(String content) {
            setContent(new ArrayList<>(Arrays.asList(itemStackArrayFromBase64(content))));
        }

        public void setRawArmor(String armor) {
            setArmor(itemStackArrayFromBase64(armor));
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public long getCreation() {
            return creation;
        }

        public String getCreator() {
            return creator;
        }

        public boolean isOpOnly() {
            return opOnly;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCreation(long creation) {
            this.creation = creation;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public void setOpOnly(boolean opOnly) {
            this.opOnly = opOnly;
        }
    }

    public static class PermaEffect {
        private PotionEffectType type;
        private int amplifier;
        private boolean showParticles;

        public PermaEffect(PotionEffectType type, int amplifier, boolean showParticles) {
            this.type = type;
            this.amplifier = amplifier;
            this.showParticles = showParticles;
        }

        public static PermaEffect fromMap(Map<String, Object> map) {
            try {
                String id = (String) map.get("type");
                PotionEffectType type = PotionEffectType.getByName(id);
                int amplifier = (int) map.get("amplifier");
                boolean showParticles = (boolean) map.get("show-particles");
                return new PermaEffect(type, amplifier, showParticles);
            } catch (Exception e) {
                System.out.println(Color.RED + "Error while loading an PermaEffect: " + e.getMessage());
                e.printStackTrace();
                System.out.println("So the object will be ignored.\nStop modifying the configs... lol. ^^" + Color.RESET);
                return null;
            }
        }

        @Override
        public String toString() {
            return "PermaEffect{" +
                    "type='" + type.getName() + '\'' +
                    ", amplifier=" + amplifier +
                    ", showParticles=" + showParticles +
                    '}';
        }

        public LinkedHashMap<String, Object> toMap() {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("type", type.getName());
            map.put("amplifier", amplifier);
            map.put("show-particles", showParticles);
            return map;
        }

        public EffectItem toEffectItem() {
            return new EffectItem(type, -1, amplifier, showParticles);
        }

        public PotionEffect toPotionEffect() {
            return new PotionEffect(type, 1000000, amplifier, false, showParticles);
        }

        public PotionEffect toPotionEffect(int duration) {
            return new PotionEffect(type, duration, amplifier, false, showParticles);
        }

        public PotionEffectType getType() {
            return type;
        }

        public void setType(PotionEffectType type) {
            this.type = type;
        }

        public int getAmplifier() {
            return amplifier;
        }

        public void setAmplifier(int amplifier) {
            this.amplifier = amplifier;
        }

        public boolean isShowingParticles() {
            return showParticles;
        }

        public void setShowingParticles(boolean showParticles) {
            this.showParticles = showParticles;
        }
    }


    public static class EffectItem {
        private PotionEffectType type;
        private int duration;
        private int amplifier;
        private boolean showParticles;

        public EffectItem(PotionEffectType type, int duration, int amplifier, boolean showParticles) {
            this.type = type;
            this.duration = duration;
            this.amplifier = amplifier;
            this.showParticles = showParticles;
        }

        public static EffectItem fromMap(Map<String, Object> map) {
            try {
                String id = (String) map.get("type");
                PotionEffectType type = PotionEffectType.getByName(id);
                int duration = (int) map.get("duration");
                int amplifier = (int) map.get("amplifier");
                boolean showParticles = (boolean) map.get("show-particles");
                return new EffectItem(type, duration, amplifier, showParticles);
            } catch (Exception e) {
                System.out.println(Color.RED + "Error while loading an EffectItem: " + e.getMessage());
                e.printStackTrace();
                System.out.println("So the object will be ignored.\nStop modifying the configs... lol. ^^" + Color.RESET);
                return null;
            }
        }

        @Override
        public String toString() {
            return "EffectItem{" +
                    "type='" + type.getName() + '\'' +
                    ", duration=" + duration +
                    ", amplifier=" + amplifier +
                    ", showParticles=" + showParticles +
                    '}';
        }

        public LinkedHashMap<String, Object> toMap() {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("type", type.getName());
            map.put("duration", duration);
            map.put("amplifier", amplifier);
            map.put("show-particles", showParticles);
            return map;
        }

        public PotionEffect toPotionEffect() {
            return new PotionEffect(type, duration == -1 ? 1000000 : duration, amplifier, false, showParticles);
        }

        public PotionEffectType getType() {
            return type;
        }

        public void setType(PotionEffectType type) {
            this.type = type;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getAmplifier() {
            return amplifier;
        }

        public void setAmplifier(int amplifier) {
            this.amplifier = amplifier;
        }

        public boolean isShowingParticles() {
            return showParticles;
        }

        public void setShowingParticles(boolean showParticles) {
            this.showParticles = showParticles;
        }
    }

    /**
     * If the location is within the bounds of the two locations, return true
     * <br>
     * (If the locations are not in the same world or just null, return false)
     *
     * @param loc  The location you want to check if it's inside the cuboid.
     * @param loc1 The first location
     * @param loc2 The location of the second corner of the cuboid.
     * @return A boolean value.
     */
    public static boolean isInside(Location loc, Location loc1, Location loc2) {
        if (loc == null || loc1 == null || loc2 == null || !loc.getWorld().getUID().equals(loc1.getWorld().getUID()) || !loc.getWorld().getUID().equals(loc2.getWorld().getUID()))
            return false;
        double maxX = Math.max(loc1.getX(), loc2.getX()), maxY = Math.max(loc1.getY(), loc2.getY()), maxZ = Math.max(loc1.getZ(), loc2.getZ()),
                minX = Math.min(loc1.getX(), loc2.getX()), minY = Math.min(loc1.getY(), loc2.getY()), minZ = Math.min(loc1.getZ(), loc2.getZ());
        return loc.getX() <= maxX && loc.getX() >= minX && loc.getY() <= maxY && loc.getY() >= minY && loc.getZ() <= maxZ && loc.getZ() >= minZ
                && (loc.getWorld().getUID().equals(loc1.getWorld().getUID()) || loc.getWorld().getUID().equals(loc2.getWorld().getUID()));
    }

    /**
     * It returns a list of all the blocks in the cuboid defined by the two locations
     * <br>
     * (If the locations are not in the same world or just null, the method will return an empty list)
     *
     * @param loc1 The first location
     * @param loc2 The second location
     * @return A list of blocks
     */
    public static List<Block> getBlocksIn(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null || !loc1.getWorld().getUID().equals(loc2.getWorld().getUID()))
            return new ArrayList<>();
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX()), maxY = Math.max(loc1.getBlockY(), loc2.getBlockY()), maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ()),
                minX = Math.min(loc1.getBlockX(), loc2.getBlockX()), minY = Math.min(loc1.getBlockY(), loc2.getBlockY()), minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        List<Block> blocks = new ArrayList<>();
        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++)
                    blocks.add(loc1.getWorld().getBlockAt(x, y, z));
        return blocks;
    }

    /**
     * Given a column and row, return the position of the cell in the inventory.
     *
     * @param col The column of the cell.
     * @param row The row of the cell you want to get the position of.
     * @return The position of the cell in the array.
     */
    public static int posOf(int col, int row) {
        return row * 9 + col;
    }

    /**
     * Given a position, return the column and row of that position.
     *
     * @param position The position of the cell in the inventory.
     * @return A pair of integers.
     */
    public static Pair<Integer, Integer> colAndRowOf(int position) {
        return new Pair<>(position % 9, position / 9);
    }

    public static List<Integer> zoneOf(int pos1, int pos2) {
        return zoneOf(pos1, pos2, false);
    }

    /**
     * It returns a list of all the positions in the zone between two positions, including the two positions themselves
     *
     * @param pos1 The first position.
     * @param pos2 The position of the second point.
     * @param wall If true, the zone will be a filled zone, if false, it will be an empty zone.
     * @return A list of integers.
     */
    public static List<Integer> zoneOf(int pos1, int pos2, boolean wall) {
        int i1, j1, i2, j2;
        if (colAndRowOf(pos1).getKey() <= colAndRowOf(pos2).getKey()) {
            i1 = colAndRowOf(pos1).getKey();
            j1 = colAndRowOf(pos2).getKey();
        } else {
            i1 = colAndRowOf(pos2).getKey();
            j1 = colAndRowOf(pos1).getKey();
        }
        if (colAndRowOf(pos1).getValue() <= colAndRowOf(pos2).getValue()) {
            i2 = colAndRowOf(pos1).getValue();
            j2 = colAndRowOf(pos2).getValue();
        } else {
            i2 = colAndRowOf(pos2).getValue();
            j2 = colAndRowOf(pos1).getValue();
        }
        return new ArrayList<Integer>() {{
            for (int col = i1; col <= j1; col++)
                for (int row = i2; row <= j2; row++)
                    if (!wall || col == i1 || col == j1 || row == i2 || row == j2)
                        add(posOf(col, row));
        }};
    }

    /**
     * It fills an inventory with an itemstack from position 1 to position 2
     *
     * @param inv  The inventory you want to fill
     * @param pos1 The first position to start filling from.
     * @param pos2 The position to stop filling at.
     * @param is   The itemstack you want to fill the inventory with
     */
    public static void fill(Inventory inv, int pos1, int pos2, ItemStack is) {
        fill(inv, pos1, pos2, false, is);
    }

    /**
     * Fills the inventory with the given item stack from the given positions.
     *
     * @param inv  The inventory to fill
     * @param pos1 The first position of the zone.
     * @param pos2 The second position of the zone.
     * @param wall If true, the zone will be a filled zone, if false, it will be an empty zone.
     * @param is   The item to fill the inventory with
     */
    public static void fill(Inventory inv, int pos1, int pos2, boolean wall, ItemStack is) {
        zoneOf(pos1, pos2, wall).forEach(p -> inv.setItem(p, is));
    }

    /**
     * @param p    <strong style="color: #ff0000">null</strong> to Broadcast
     * @param sec  &nbsp; <code style="color: #00ff00"><0</code> &nbsp; to do instantly &nbsp;
     *             | &nbsp; <code style="color: #00ff00">0</code> &nbsp; to do instantly WITH last msg &nbsp;
     *             | &nbsp; <code style="color: #00ff00">>0</code> &nbsp; normal
     * @param text Use variable <strong style="color: #ffffff">%i%</strong> and use <strong style="color: #ffffff">§r</strong> to default color
     */
    public static void countDown(@Nullable Player p, int sec, boolean ascendant, boolean title, boolean chat, String text, @Nullable String end,
                                 String defaultColor, String warningColor, String criticalColor, String lastColor, String endColor, @Nullable Runnable r) {
        if (sec < 0) {
            if (r != null)
                r.run();
            return;
        }
        new BukkitRunnable() {
            int i = sec;

            @Override
            public void run() {
                String ii = (ascendant ? sec - i : i) + "";
                String s = ("§r" + (i == 0 ? end == null ? "" : end : text).replace("%i%", ii).replace("%I%", ii)).replace("\n", "\n§r")
                        .replace("§r", i == 5 || i == 4 ? warningColor : i == 3 || i == 2 ? criticalColor : i == 1 ? lastColor : i == 0 ? endColor : defaultColor);

                if (i != 0 || end != null)
                    if (p == null) {
                        if (chat)
                            Broadcast.log(s.contains("\n") ? s.split("\n", 2)[0] : s);
                        if (title)
                            Bukkit.getOnlinePlayers().forEach(pl ->
                                    pl.sendTitle(s.contains("\n") ? s.split("\n", 2)[0] : s, s.contains("\n") ? s.split("\n", 2)[1] : ""));
                    } else {
                        if (title)
                            p.sendTitle(s.contains("\n") ? s.split("\n", 2)[0] : s, s.contains("\n") ? s.split("\n", 2)[1] : "");
                        if (chat)
                            p.sendMessage(Main.PREFIX + (s.contains("\n") ? s.split("\n", 2)[0] : s));
                    }

                if (i == 0) {
                    this.cancel();
                    if (r != null)
                        r.run();
                    return;
                }

                i--;
            }
        }.runTaskTimer(Main.instance, 0, 20);
    }

    /**
     * It takes a location, and returns a location that is the center of the block that the original location is in
     * (Y position included)
     *
     * @param loc The location to normalize
     * @return A Location object
     */
    public static Location normalize(Location loc) {
        return normalize(loc, true);
    }

    /**
     * It takes a location, and returns a location that is the center of the block that the original location is in
     *
     * @param loc The location to normalize
     * @param y   If true, the y-coordinate will be normalized. If false, it will not.
     * @return A Location object
     */
    public static Location normalize(Location loc, boolean y) {
        Location l = loc.clone();
        l.setX(((int) l.getX()) + 0.5);
        if (y)
            l.setY(((int) l.getY()) + 0.5);
        l.setZ(((int) l.getZ()) + 0.5);
        l.setYaw(0);
        l.setPitch(0);
        return l;
    }

    /**
     * It returns the distance between two locations. (null if one of the locations is null)
     *
     * @param a             The first location
     * @param b             The second location
     * @param considerWorld If true, the method will return null if the two locations are in different worlds.
     * @return The exact distance between the two locations.
     */
    public static Double safeDistance(Location a, Location b, boolean considerWorld) {
        if (a == null && b == null)
            return 0d;
        if (a == null || b == null)
            return null;
        double d = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
        return !considerWorld || (a.getWorld() != null && b.getWorld() != null
                && Objects.equals(a.getWorld().getName(), b.getWorld().getName())) ? d : null;
    }

    /**
     * It returns the distance between two locations, or "x" if the locations are in different worlds
     *
     * @param a             The first location
     * @param b             The location to compare to.
     * @param considerWorld If true, the distance will be calculated between the two locations, even if they are in
     *                      different worlds.
     * @param precision     The number of decimal places to round to (0 to get an int).
     * @param radius        If distance <= radius, the distance will be xx.x
     * @return The formatted distance between two locations.
     */
    public static String safeDistance(Location a, Location b, boolean considerWorld, int precision, double radius) {
        Double d = safeDistance(a, b, considerWorld);
        String s = precision <= 0 ? d == null || d <= radius ? "00" : Math.round(d) + ""
                : String.format("%." + precision + "f", d == null || d <= radius ? 0.0 : d);
        return (d == null || d <= radius ? s.replace("0", "x") : s).replace(",", ".");
    }

    /**
     * It takes a list of strings and returns a packet that will set the header and footer of the tab list
     *
     * @param header The header of the tab list.
     * @param footer The footer of the tab list.
     * @return A PacketPlayOutPlayerListHeaderFooter object.
     */
    public static PacketPlayOutPlayerListHeaderFooter getTabHeaderAndFooter(List<String> header, List<String> footer) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        Field a, b;
        try {
            a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }

        String h = "";
        String f = "";
        if (header.isEmpty())
            try {
                a.set(packet, new ChatComponentText(""));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        else
            for (String hd : header)
                h += hd + "\n";
        if (footer.isEmpty())
            try {
                b.set(packet, new ChatComponentText(""));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        else
            for (String ft : footer)
                f += ft + "\n";
        try {
            a.set(packet, new ChatComponentText(h.substring(0, h.length() - 1)));
            b.set(packet, new ChatComponentText(f.substring(0, f.length() - 1)));
        } catch (StringIndexOutOfBoundsException ignore) {
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public static String getFormattedWorld(String world) {
        return world.equalsIgnoreCase("world") ? "§aOverWorld"
                : world.equalsIgnoreCase("world_nether") ? "§dNether"
                : world.equalsIgnoreCase("world_the_end") ? "§5End"
                : world;
    }

    /**
     * It returns a formatted / user-friendly string of a location.
     *
     * @param loc     The location you want to convert to a string.
     * @param decimal Whether to use decimals.
     * @param ywPi    Yaw and Pitch included
     * @param world   If true, the world name will be added to the end of the string.
     * @return A string with the location of the player.
     */
    public static String locToString(Location loc, boolean decimal, boolean ywPi, boolean world) {
        DecimalFormat df = new DecimalFormat(decimal ? "0.00" : "#");
        return loc == null ? "§cnull"
                : (df.format(loc.getX()) + "  " + df.format(loc.getY()) + "  " + df.format(loc.getZ())
                + (ywPi ? "  (" + df.format(loc.getYaw()) + "  " + df.format(loc.getPitch()) + ")" : "")
                + (world ? "  " + (loc.getWorld() == null ? "§cnull"
                : (loc.getWorld().getName().equalsIgnoreCase("world") ? "§aOverWorld"
                : loc.getWorld().getName().equalsIgnoreCase("world_nether") ? "§dNether"
                : loc.getWorld().getName().equalsIgnoreCase("world_the_end") ? "§5End"
                : loc.getWorld().getName().equalsIgnoreCase("world"))) : "") + "§r").replace(",", ".");
    }

    public static UUID parseUUID(String uuid) {
        try {
            if (uuid.length() == 32)
                return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
        } catch (IllegalArgumentException ignored) {
        }
        return null;
    }

    public static String sendGETRequest(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line).append("\n");
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRandomQuoteFromAPI() {
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(sendGETRequest("https://api.quotable.io/random")); // Or https://zenquotes.io/api/random
            return obj.get("content") + "\n  - " + obj.get("author");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pair<String, UUID> getNameAndUUIDFromMojangAPI(String username) {
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(sendGETRequest("https://api.mojang.com/users/profiles/minecraft/" + username));
            return new Pair<>(obj.get("name").toString(), parseUUID(obj.get("id").toString()));
        } catch (ParseException | RuntimeException e) {
            // Case of the username not existing ->> GET response is null                        ->> ParseException
            // Case of the username is invalid   ->> GET response code: 400 and contains "error" ->> RuntimeException
            return null;
        }
    }

    public static MojangProfile getProfileFromMojangAPI(UUID uuid) {
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(sendGETRequest("https://sessionserver.mojang.com/session/minecraft/profile/" + (uuid + "").replace("-", "") + "?unsigned=false"));
            JSONObject prop = (JSONObject) ((JSONArray) obj.get("properties")).get(0);
            return new MojangProfile(parseUUID(obj.get("id").toString()), obj.get("name").toString(), prop.get("value").toString(), prop.get("signature").toString());
        } catch (ParseException | RuntimeException e) {
            // Case of the uuid not existing ->> GET response is null                        ->> ParseException
            // Case of the uuid is invalid   ->> GET response code: 400 and contains "error" ->> RuntimeException
            return null;
        }
    }

    /**
     * Try to run the given runnable, and return true if it succeeded, or false if it failed.
     *
     * @param printStackTrace If true, print in console errors.
     * @param r               The runnable to run.
     * @return A boolean value.
     */
    public static boolean tryTo(boolean printStackTrace, Runnable r) {
        try {
            r.run();
            return true;
        } catch (Exception e) {
            if (printStackTrace) {
                System.out.println(Color.RED);
                e.printStackTrace();
                System.out.println(Color.RESET);
            }
            return false;
        }
    }


    /**
     * This function returns an ArrayList of all the players in the current game and in the server.
     *
     * @return An {@link ArrayList} of {@link String}s
     */
    public static ArrayList<String> getAllPlayers() {
        return getAllPlayers(GManager.getCurrentGame());
    }

    /**
     * It gets all the players in the game and in the server, and returns them as an ArrayList
     *
     * @param manager The {@link GManager} object that you want to get the players from.
     * @return A list of all players in the game.
     */
    public static ArrayList<String> getAllPlayers(GManager manager) {
        HashSet<String> players = new HashSet<>();
        if (manager != null && manager.getPlayers() != null)
            players.addAll(manager.getPlayers().stream().map(GPlayer::getName).collect(Collectors.toList()));
        players.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        return new ArrayList<>(players);
    }

    public static ChatColor dyeToChatColor(DyeColor color) {
        return dataToChatColor(color.getData());
    }

    public static DyeColor chatToDyeColor(ChatColor color) {
        return dataToDyeColor(chatToDataColor(color));
    }

    public static ChatColor dataToChatColor(int color) {
        switch (color) {
            case 0:
                return ChatColor.WHITE;
            case 1:
                return ChatColor.GOLD;
            case 2:
                return ChatColor.LIGHT_PURPLE;
            case 3:
                return ChatColor.BLUE;
            case 4:
                return ChatColor.YELLOW;
            case 5:
                return ChatColor.GREEN;
            case 6:
                return ChatColor.AQUA;
            case 7:
                return ChatColor.GRAY;
            case 8:
                return ChatColor.DARK_GRAY;
            case 9:
                return ChatColor.DARK_AQUA;
            case 10:
                return ChatColor.DARK_PURPLE;
            case 11:
                return ChatColor.DARK_BLUE;
            case 12:
                return ChatColor.DARK_RED;
            case 13:
                return ChatColor.DARK_GREEN;
            case 14:
                return ChatColor.RED;
            case 15:
                return ChatColor.BLACK;
            default:
                return null;
        }
    }

    public static DyeColor dataToDyeColor(int color) {
        switch (color) {
            case 0:
                return DyeColor.WHITE;
            case 1:
                return DyeColor.ORANGE;
            case 2:
                return DyeColor.MAGENTA;
            case 3:
                return DyeColor.LIGHT_BLUE;
            case 4:
                return DyeColor.YELLOW;
            case 5:
                return DyeColor.LIME;
            case 6:
                return DyeColor.PINK;
            case 7:
                return DyeColor.GRAY;
            case 8:
                return DyeColor.SILVER;
            case 9:
                return DyeColor.CYAN;
            case 10:
                return DyeColor.PURPLE;
            case 11:
                return DyeColor.BLUE;
            case 12:
                return DyeColor.BROWN;
            case 13:
                return DyeColor.GREEN;
            case 14:
                return DyeColor.RED;
            case 15:
                return DyeColor.BLACK;
            default:
                return null;
        }
    }

    public static byte chatToDataColor(ChatColor color) {
        switch (color) {
            case WHITE:
                return 0;
            case GOLD:
                return 1;
            case LIGHT_PURPLE:
                return 2;
            case BLUE:
                return 3;
            case YELLOW:
                return 4;
            case GREEN:
                return 5;
            case AQUA:
                return 6;
            case GRAY:
                return 7;
            case DARK_GRAY:
                return 8;
            case DARK_AQUA:
                return 9;
            case DARK_PURPLE:
                return 10;
            case DARK_BLUE:
                return 11;
            case DARK_RED:
                return 12;
            case DARK_GREEN:
                return 13;
            case RED:
                return 14;
            case BLACK:
                return 15;
            default:
                return -1;
        }
    }

    /**
     * Let's make a <strong>ProgressBar</strong> !
     *
     * @param leftSide   The string to be placed to the left of the progress bar.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code style="color: #ff0000; font-weight: 900;">[</code><code>####------------]</code><br>
     * @param rightSide  The string to be appended to the right side of the progress bar.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code>[####------------</code><code style="color: #ff0000; font-weight: 900;">]</code><br>
     * @param fill       The character to use to fill the progress bar.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code>[<code style="color: #ff0000; font-weight: 900;">####</code>------------]</code><br>
     * @param empty      The character to use for the empty part of the bar.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code>[####<code style="color: #ff0000; font-weight: 900;">------------</code>]</code><br>
     * @param endFill    If progress is 100%, the bar will be filled with the fill character.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">100% - </code><code>[<code style="color: #00ff00; font-weight: 900;">****************</code>]</code><br>
     * @param startEmpty In contrary, at 0%, the bar will be filled with the empty character.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp; 0% - </code><code>[<code style="color: #0000ff; font-weight: 900;">~~~~~~~~~~~~~~~~</code>]</code><br>
     * @param length     The length of the progress bar (number of characters).<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code style="color: #aaaaaa;">[####------------]</code>
     *                   &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code style="color: #aaaaaa;">[##------]</code><br>
     * @param progress   The progress of the bar, from 0 to 1.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code style="color: #aaaaaa;">[##------]</code>
     *                   <code style="color: #00aa00">&nbsp;50% - </code><code style="color: #aaaaaa;">[####----]</code>
     *                   <code style="color: #00aa00">&nbsp;75% - </code><code style="color: #aaaaaa;">[######--]</code><br>
     * @param format     The format of the final bar with 5 vars: <b>{b}</b> | the formatted bar ; <b>{p}</b> | the percentage (int) ;
     *                   <b>{p.}</b> | the percentage (double #0.0) ; <b>{p..}</b> | the percentage (double #0.00) ;
     *                   <b>{p#}</b> | the progression (0 to 1 float) ; <b>{l}</b> | the length of the progress bar<br>
     *                   Example:
     *                   <ul>
     *                          <li><code style="color: #aa00aa">{p}% - {b} </code>&nbsp; &nbsp; <code style="color: #00aaaa">25% - [####------------]</code></li>
     *                          <li><code style="color: #aa00aa">{p.}% $ {b}</code>&nbsp; &nbsp; <code style="color: #00aaaa">25.00 $ [####------------]</code></li>
     *                          <li><code style="color: #aa00aa">{b} >> {p#}</code>&nbsp; &nbsp; <code style="color: #00aaaa">[####------------] >> 0.25</code></li>
     *                   </ul>
     * @return A string that is a progress bar.
     */
    public static String progressBar(String leftSide, String rightSide, String fill, String empty, String endFill, String startEmpty,
                                     int length, float progress, String format) {
        StringBuilder sb = new StringBuilder();
        sb.append(leftSide);
        for (int i = 0; i <= Math.round(length * progress); i++) {
            sb.append(progress == 1 ? endFill : fill);
        }
        for (int i = 0; i < Math.round(length * (1 - progress)); i++) {
            sb.append(progress == 0 ? startEmpty : empty);
        }
        sb.append(rightSide);
        return format.replace("{b}", sb.toString()).replace("{p}", String.valueOf(Math.round(progress * 100)))
                .replace("{p.}", String.format("%.1f", progress * 100)).replace("{p..}", String.format("%.2f", progress * 100))
                .replace("{p#}", String.valueOf(progress)).replace("{l}", String.valueOf(length));
    }

    /**
     * Let's make a <strong>ProgressBar</strong> !
     *
     * @param leftSide   The string to be placed to the left of the progress bar.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code style="color: #ff0000; font-weight: 900;">[</code><code>####------------]</code><br>
     * @param rightSide  The string to be appended to the right side of the progress bar.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code>[####------------</code><code style="color: #ff0000; font-weight: 900;">]</code><br>
     * @param fillChars  The characters to use to fill the progress bar.<br>
     *                   Example: If we use <code style="color: #555555">[".", "*", "/", "#"]</code>, the fill char will be :<br>
     *                   &nbsp; >> &nbsp; <code style="color: #555555">"."</code> if progress is <code style="color: #00aa00">0%</code> to <code style="color: #00aa00">25%</code>,<br>
     *                   &nbsp; >> &nbsp; <code style="color: #555555">"*"</code> if progress is <code style="color: #00aa00">25%</code> to <code style="color: #00aa00">50%</code>,<br>
     *                   &nbsp; >> &nbsp; <code style="color: #555555">"/"</code> if progress is <code style="color: #00aa00">50%</code> to <code style="color: #00aa00">75%</code>,<br>
     *                   &nbsp; >> &nbsp; <code style="color: #555555">"#"</code> if progress is <code style="color: #00aa00">75%</code> to <code style="color: #00aa00">100%</code>.<br>
     * @param empty      The character to use for the empty part of the bar.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code>[####<code style="color: #ff0000; font-weight: 900;">------------</code>]</code><br>
     * @param endFill    If progress is 100%, the bar will be filled with the fill character.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">100% - </code><code>[<code style="color: #00ff00; font-weight: 900;">****************</code>]</code><br>
     * @param startEmpty In contrary, at 0%, the bar will be filled with the empty character.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp; 0% - </code><code>[<code style="color: #0000ff; font-weight: 900;">~~~~~~~~~~~~~~~~</code>]</code><br>
     * @param length     The length of the progress bar (number of characters).<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code style="color: #aaaaaa;">[####------------]</code>
     *                   &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code style="color: #aaaaaa;">[##------]</code><br>
     * @param progress   The progress of the bar, from 0 to 1.<br>
     *                   Example: &nbsp; <code style="color: #00aa00">&nbsp;25% - </code><code style="color: #aaaaaa;">[##------]</code>
     *                   <code style="color: #00aa00">&nbsp;50% - </code><code style="color: #aaaaaa;">[####----]</code>
     *                   <code style="color: #00aa00">&nbsp;75% - </code><code style="color: #aaaaaa;">[######--]</code><br>
     * @param format     The format of the final bar with 5 vars: <b>{b}</b> | the formatted bar ; <b>{p}</b> | the percentage (int) ;
     *                   <b>{p.}</b> | the percentage (double #0.0) ; <b>{p..}</b> | the percentage (double #0.00) ;
     *                   <b>{p#}</b> | the progression (0 to 1 float) ; <b>{l}</b> | the length of the progress bar<br>
     *                   Example:
     *                   <ul>
     *                          <li><code style="color: #aa00aa">{p}% - {b} </code>&nbsp; &nbsp; <code style="color: #00aaaa">25% - [####------------]</code></li>
     *                          <li><code style="color: #aa00aa">{p.}% $ {b}</code>&nbsp; &nbsp; <code style="color: #00aaaa">25.00 $ [####------------]</code></li>
     *                          <li><code style="color: #aa00aa">{b} >> {p#}</code>&nbsp; &nbsp; <code style="color: #00aaaa">[####------------] >> 0.25</code></li>
     *                   </ul>
     * @return A string that is a progress bar.
     */
    public static String progressBar(String leftSide, String rightSide, String[] fillChars, String empty, String endFill, String startEmpty,
                                     int length, float progress, String format) {
        StringBuilder sb = new StringBuilder();
        sb.append(leftSide);
        for (int i = 0; i < Math.round(length * progress); i++) {
            sb.append(progress == 1.0 ? endFill : fillChars[(int) (progress / (1.0 / fillChars.length))]);
        }
        for (int i = 0; i <= Math.round(length * (1 - progress)); i++) {
            sb.append(progress == 0.0 ? startEmpty : empty);
        }
        sb.append(rightSide);
        return format.replace("{b}", sb.toString()).replace("{p}", String.valueOf(Math.round(progress * 100)))
                .replace("{p.}", String.format("%.1f", progress * 100)).replace("{p..}", String.format("%.2f", progress * 100))
                .replace("{p#}", String.valueOf(progress)).replace("{l}", String.valueOf(length));
    }

    /**
     * It returns the difference between two dates in the format of Xd Xh Xmin Xsec Xms.
     *
     * @param from   The date to start the comparison from.
     * @param to     The date to compare to.
     * @param millis If true, the milliseconds will be included in the output.
     * @return A string that represents the difference between two dates.
     */
    public static String compareDate(Date from, Date to, boolean millis) {
        long diff = to.getTime() - from.getTime();
        int milliseconds = (int) (diff % 1000);
        int seconds = (int) (diff / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        int days = hours / 24;
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
        String out = (days == 0 ? "" : days + "d ") + (hours == 0 ? "" : hours + "h ") + (minutes == 0 ? "" : minutes + "m ")
                + (seconds == 0 ? "" : seconds + "s ") + (millis && milliseconds != 0 ? milliseconds + "ms " : "");
        return out.equals("") ? millis ? "0ms" : "0s" : out.substring(0, out.length() - 1);
    }

    /**
     * It takes a string, hash it, and returns a long
     *
     * @param s The string to hash.
     * @return A hash of the string.
     */
    public static long hashStringToSeed(String s) {
        if (s == null)
            return 0;
        long hash = 0;
        for (char c : s.toCharArray())
            hash = 31L * hash + c;
        return hash;
    }

    /**
     * Calculates the total EXP needed to reach the given level.
     *
     * @param lvl The level you want to calculate the experience for.
     * @return The amount of experience needed to reach a certain level.
     */
    public static long lvlToExp(double lvl) {
        return (long) (((int) lvl <= 16 ? Math.pow((int) lvl, 2) + 6L * (int) lvl
                : (int) lvl <= 31 ? 2.5 * Math.pow((int) lvl, 2) - 40.5 * (int) lvl + 360
                : 4.5 * Math.pow((int) lvl, 2) - 162.5 * (int) lvl + 2220)
                + expToLvlUpAt((int) lvl) * (lvl - (int) lvl));
    }

    /**
     * Calculs the amount of exp needed to level up at a certain level.
     *
     * @param lvl The level of the player.
     * @return The amount of experience needed to level up at the given level.
     */
    public static long expToLvlUpAt(int lvl) {
        return lvl <= 15 ? 2L * lvl + 7
                : lvl <= 30 ? 5L * lvl - 38
                : 9L * lvl - 158;
    }

    /**
     * Converts experience to level.
     *
     * @param exp The amount of experience you want to convert.
     * @return The level of a player based on the amount of experience.
     */
    public static double expToLvl(long exp) {
        return exp <= 352 ? Math.sqrt(exp + 9) - 3
                : exp <= 1507 ? 81.0 / 10.0 + Math.sqrt(2.0 / 5.0 * (exp - 7839.0 / 40.0))
                : 325.0 / 18.0 + Math.sqrt(2.0 / 9.0 * (exp - 54215.0 / 72.0));
    }

    /**
     * It takes an array of ItemStacks and converts it to a Base64 String
     *
     * @param items The ItemStack array to be converted.
     * @return A Base64 {@link String} of the ItemStack array.
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * It decodes and returns a ItemStack array from a given Base64 String.
     *
     * @param data The Base64 String to decode.
     * @return The decoded ItemStack array.
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (/* IOException | ClassNotFoundException */ Exception e) {
//            throw new IOException("Unable to decode class type.", e);
            return new ItemStack[0];
        }
    }

    @Deprecated
    public static String itemStackToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    @Deprecated
    public static ItemStack itemStackFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (/* IOException | ClassNotFoundException */ Exception e) {
            return null;
        }
    }

    public static String objectToBase64(Object o) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(o);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save object.", e);
        }
    }

    public static <T> T objectFromBase64(String data, Class<T> clazz) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            T o = (T) dataInput.readObject();
            dataInput.close();
            return o;
        } catch (/* IOException | ClassNotFoundException */ Exception e) {
            return null;
        }
    }

    public static ExperienceOrb dropEXPOrb(int minSize, int maxSize, Location loc) {
        return dropEXPOrb(new Random().nextInt(maxSize - minSize) + minSize, loc);
    }

    public static ExperienceOrb dropEXPOrb(int size, Location loc) {
        ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        orb.setExperience(size);
        orb.setMetadata("exp", new FixedMetadataValue(Main.instance, size));
        return orb;
    }

    /**
     * This method is used to break lines in a string.<br>
     * It will break lines at the spaces in the string.<br>
     * It will not break lines in the middle of a word.<br>
     * It will not break lines if the word is longer than the max length.<br>
     *
     * <br>
     * <p>
     * Example:<br>
     *
     * <pre>
     *     breakLines("Hello World!", 5);
     *     // Hello
     *     // World!
     * </pre>
     *
     * @param str       The string to break lines in.
     * @param maxLength The max length of a line.
     * @return The string with lines broken.
     */
    public static String breakLines(String str, int maxLength) {
        return Arrays.stream(str.split("\n")).map(s -> {
            String[] words = s.split(" ");
            StringBuilder ret = new StringBuilder();

            int len = 0;
            for (String word : words) {
                if (len + ChatColor.stripColor(word).length() > maxLength) {
                    ret.append("\n");
                    len = 0;
                }
                ret.append(word).append(" ");
                len += ChatColor.stripColor(word).length() + 1;
            }

            return ret.length() > 0 ? ret.substring(0, ret.length() - 1) : ret.toString();
        }).collect(Collectors.joining("\n"));
    }

    public static String heartsDisplay(double health, double absorption) {
        StringBuilder heartsDisplay = new StringBuilder();
        int j = 0;
        for (int i = 0; i < health / 2; i++) {
            if (j % 10 == 0 && j != 0) {
                heartsDisplay.append("\n");
                j = 0;
            }
            heartsDisplay.append("§4❤");
            j++;
        }
        if ((int) health % 2 == 1) {
            if (j % 10 == 0 && j != 0) {
                heartsDisplay.append("\n");
                j = 0;
            }
            heartsDisplay.append("§c❤");
            j++;
        }
        for (int i = 0; i < absorption / 2; i++) {
            if (j % 10 == 0 && j != 0) {
                heartsDisplay.append("\n");
                j = 0;
            }
            heartsDisplay.append("§6❤");
            j++;
        }
        if ((int) absorption % 2 == 1) {
            if (j % 10 == 0 && j != 0) {
                heartsDisplay.append("\n");
                j = 0;
            }
            heartsDisplay.append("§e❤");
            j++;
        }
        while (j % 10 != 0) {
            heartsDisplay.append("§8❤");
            j++;
        }
        StringBuilder inversedHeartsDisplay = new StringBuilder();
        for (String line : heartsDisplay.toString().split("\n")) {
            inversedHeartsDisplay.insert(0, line + "\n");
        }
        return inversedHeartsDisplay.length() == 0 ? ""
                : inversedHeartsDisplay.substring(0, inversedHeartsDisplay.length() - 1);
    }

    public static String rainbowize(String origin) {
        String colors = "123456789abcde";
        Random r = new Random();
        StringBuilder fin = new StringBuilder();
        for(int i = 0; i < origin.length(); i++) {
            fin.append("§").append(colors.charAt(r.nextInt(colors.length()))).append(origin.charAt(i));
        }
        return fin.toString();
    }
}
