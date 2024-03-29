package fr.luzog.pl.ptk.game;

import fr.luzog.pl.ptk.utils.SpecialChars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPermissions {

    public static enum Type {
        PVP, FRIENDLY_FIRE, MOBS,
        BREAK, BREAKSPE, PLACE, PLACESPE,
        CHAT_GLOBAL, CHAT_TEAM, CHAT_PRIVATE,
        GAME, KICK_WARN, BAN;
    }

    public static enum Definition {
        ON, OFF, DEFAULT;

        public String toFormattedString() {
            return (this == Definition.ON ? "§2§l" + SpecialChars.YES + " ON"
                    : this == Definition.OFF ? "§4§l" + SpecialChars.NO + " OFF"
                    : this == Definition.DEFAULT ?"§7" + SpecialChars.WARNING + " DEFAULT"
                    : "§cnull") + "§r";
        }
    }

    public static class Item {
        private Type type;
        private Definition definition;

        public Item(Type type, Definition definition) {
            this.type = type;
            this.definition = definition;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "type=" + type +
                    ", definition=" + definition +
                    '}';
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public Definition getDefinition() {
            return definition;
        }

        public void setDefinition(Definition definition) {
            this.definition = definition;
        }
    }

    private Map<Type, Definition> permissions;

    public GPermissions(Definition defaultDefinition, Item... permissions) {
        this.permissions = new HashMap<Type, Definition>() {{
            for (Item a : permissions)
                if (containsKey(a.getType()))
                    replace(a.getType(), a.getDefinition());
                else
                    put(a.getType(), a.getDefinition());
            for (Type value : Type.values())
                putIfAbsent(value, defaultDefinition);
        }};
    }

    @Override
    public String toString() {
        return "GPermissions{" +
                "permissions=" + permissions +
                '}';
    }

    public Map<Type, Definition> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<Type, Definition> permissions) {
        this.permissions = permissions;
    }

    public Definition getPermission(Type type) {
        return permissions.getOrDefault(type, null);
    }

    public void setPermission(Type type, Definition definition) {
        if (permissions.containsKey(type))
            permissions.replace(type, definition);
        else
            permissions.put(type, definition);
    }

    public Item getItem(Type type) {
        return permissions.containsKey(type) ? new Item(type, permissions.get(type)) : null;
    }

    public void setItem(Item item) {
        setPermission(item.getType(), item.getDefinition());
    }

    public List<Item> getItems() {
        return new ArrayList<Item>() {{
            permissions.forEach((t, d) -> add(new Item(t, d)));
        }};
    }
}
