package br.com.skyheros.power;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class PowerRegistry {
    private static final Map<String, PowerDefinition> POWERS =
            new LinkedHashMap<>();

    private PowerRegistry() {}

    public static synchronized void register(PowerDefinition power) {
        if (power == null) {
            throw new IllegalArgumentException("Poder nulo");
        }

        String id = power.getId().toLowerCase(Locale.ROOT);

        if (POWERS.containsKey(id)) {
            throw new IllegalArgumentException("ID de poder duplicado: " + id);
        }

        POWERS.put(id, power);
    }

    public static synchronized Optional<PowerDefinition> get(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(POWERS.get(id.toLowerCase(Locale.ROOT)));
    }

    public static synchronized Collection<PowerDefinition> getAll() {
        return Collections.unmodifiableList(
                new ArrayList<>(POWERS.values()));
    }

    public static synchronized int size() {
        return POWERS.size();
    }
}
