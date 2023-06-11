package fr.luzog.pl.ptk.game;

import fr.luzog.pl.ptk.utils.Items;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@Deprecated
public enum ZRole {

    KING("Roi", 20f, 4, Arrays.asList(
            new EffectItem(PotionEffectType.NIGHT_VISION, 1000000, 0, false),
            new EffectItem(PotionEffectType.FIRE_RESISTANCE, 1000000, 0, false),
            new EffectItem(PotionEffectType.REGENERATION, 1000000, 0, false)
    ), null, null),

    KNIGHT("Chevalier", 4f, 3, Collections.singletonList(
            new EffectItem(PotionEffectType.SPEED, 1000000, 0, false)), null, null),

    SQUIRE("Écuyer", 0f, 2, null, null, null),

    WIZARD("Sorcier", 0f, 3, Collections.singletonList(
            new EffectItem(PotionEffectType.FIRE_RESISTANCE, 1000000, 0, false)), Arrays.asList(
                    new KitItem(Material.POTION, 1, (short) 8226, "§9Potion de Vitesse I", null, null),
                    new KitItem(Material.POTION, 1, (short) 8226, "§9Potion de Vitesse I", null, null),
                    new KitItem(Material.POTION, 1, (short) 16421, "§5Potion de Soin II", null, null),
                    new KitItem(Material.POTION, 1, (short) 16421, "§5Potion de Soin II", null, null),
                    new KitItem(Material.POTION, 1, (short) 16421, "§5Potion de Soin II", null, null),
                    new KitItem(Material.BREWING_STAND_ITEM, 1, (short) 0, "§6Alambic", null, null)
    ), null),

    WITCH("Sorcière", 0f, 3, Collections.singletonList(
            new EffectItem(PotionEffectType.FIRE_RESISTANCE, 1000000, 0, false)), Arrays.asList(
            new KitItem(Material.POTION, 1, (short) 8201, "§4Potion de Force I", null, null),
            new KitItem(Material.POTION, 1, (short) 16388, "§2Potion de Poison I", null, null),
            new KitItem(Material.POTION, 1, (short) 16388, "§2Potion de Poison I", null, null),
            new KitItem(Material.POTION, 1, (short) 16385, "§dPotion de Régénération I", null, null),
            new KitItem(Material.POTION, 1, (short) 16428, "§8Potion de Dégâts II", null, null),
            new KitItem(Material.BREWING_STAND_ITEM, 1, (short) 0, "§6Alambic", null, null)
    ), null),

    ARCHER("Archer", 0f, 3, Collections.singletonList(
            new EffectItem(PotionEffectType.FIRE_RESISTANCE, 1000000, 0, false)), Arrays.asList(
            new KitItem(Material.ARROW, 32, (short) 0, "§fFlèches", null, null),
            new KitItem(Material.ENCHANTED_BOOK, 1, (short) 0, "§eLivre Enchanté : Punch I", null, Collections.singletonMap(Enchantment.ARROW_KNOCKBACK, 1))
    ), new HashMap<Enchantment, Integer>() {{
        put(Enchantment.ARROW_KNOCKBACK, 1);
        put(Enchantment.ARROW_INFINITE, 1);
    }}),

    GUARD("Garde"),

    REGICIDE("Régicide"),

    FALLEN_KNIGHT("Chevalier Déchu"),

    BRIGAND("Brigand"),

    TRAITOR("Traître"),

    PYROMANIAC("Pyromane"),

    ;

    private String name, description;
    private float healthModifier;
    private int armorLimit;
    private List<EffectItem> effects;
    private List<KitItem> kit;
    private Map<Enchantment, Integer> enchantLimit;

    ZRole(String name) {
        this.name = name;
        this.description = null;
        this.healthModifier = 0f;
        this.armorLimit = 4;
        this.effects = new ArrayList<>();
        this.kit = new ArrayList<>();
        this.enchantLimit = new HashMap<>();
    }

    ZRole(String name, String description) {
        this.name = name;
        this.description = description;
        this.healthModifier = 0;
        this.armorLimit = 4;
        this.effects = new ArrayList<>();
        this.kit = new ArrayList<>();
        this.enchantLimit = new HashMap<>();
    }

    ZRole(String name, float healthModifier, int armorLimit, List<EffectItem> effects,
          List<KitItem> kit, Map<Enchantment, Integer> enchantLimit) {
        this.name = name;
        this.description = null;
        this.healthModifier = healthModifier;
        this.armorLimit = armorLimit;
        this.effects = effects == null ? new ArrayList<>() : new ArrayList<>(effects);
        this.kit = kit == null ? new ArrayList<>() : new ArrayList<>(kit);
        this.enchantLimit = enchantLimit == null ? new HashMap<>() : new HashMap<>(enchantLimit);
    }

    ZRole(String name, String description, float healthModifier, int armorLimit, List<EffectItem> effects,
          List<KitItem> kit, Map<Enchantment, Integer> enchantLimit) {
        this.name = name;
        this.description = description;
        this.healthModifier = healthModifier;
        this.armorLimit = armorLimit;
        this.effects = effects == null ? new ArrayList<>() : new ArrayList<>(effects);
        this.kit = kit == null ? new ArrayList<>() : new ArrayList<>(kit);
        this.enchantLimit = enchantLimit == null ? new HashMap<>() : new HashMap<>(enchantLimit);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getHealthModifier() {
        return healthModifier;
    }

    public void setHealthModifier(float healthModifier) {
        this.healthModifier = healthModifier;
    }

    public int getArmorLimit() {
        return armorLimit;
    }

    public void setArmorLimit(int armorLimit) {
        this.armorLimit = armorLimit;
    }

    public List<EffectItem> getEffects() {
        return effects;
    }

    public void setEffects(List<EffectItem> effects) {
        this.effects = effects;
    }

    public List<KitItem> getKit() {
        return kit;
    }

    public void setKit(List<KitItem> kit) {
        this.kit = kit;
    }

    public Map<Enchantment, Integer> getEnchantLimit() {
        return enchantLimit;
    }

    public void setEnchantLimit(Map<Enchantment, Integer> enchantLimit) {
        this.enchantLimit = enchantLimit;
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

    public static class KitItem {
        private Material type;
        private int amount;
        private short durability;
        private String displayName;
        private List<String> lore;
        private Map<Enchantment, Integer> enchants;

        public KitItem(Material type, int amount, short durability, String displayName, List<String> lore, Map<Enchantment, Integer> enchants) {
            this.type = type;
            this.amount = amount;
            this.durability = durability;
            this.displayName = displayName;
            this.lore = lore;
            this.enchants = enchants;
        }

        public ItemStack build() {
            Items.Builder i = Items.builder(type).setAmount(amount).setDurability(durability);
            if (displayName != null)
                i.setName(displayName);
            if (lore != null)
                i.setLore(lore);
            if (enchants != null)
                enchants.forEach(i::addEnchant);
            return i.build();
        }

        public Material getType() {
            return type;
        }

        public void setType(Material type) {
            this.type = type;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public short getDurability() {
            return durability;
        }

        public void setDurability(short durability) {
            this.durability = durability;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public List<String> getLore() {
            return lore;
        }

        public void setLore(List<String> lore) {
            this.lore = lore;
        }

        public Map<Enchantment, Integer> getEnchants() {
            return enchants;
        }

        public void setEnchants(Map<Enchantment, Integer> enchants) {
            this.enchants = enchants;
        }
    }

}
