package br.com.skyheros.ability;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class AbilityRegistry {
    private static final Map<String, AbilityDefinition> ABILITIES = new LinkedHashMap<>();

    private AbilityRegistry() {}

    public static synchronized void register(AbilityDefinition ability) {
        if (ABILITIES.putIfAbsent(ability.id(), ability) != null) {
            throw new IllegalArgumentException("Habilidade duplicada: " + ability.id());
        }
    }

    public static synchronized Optional<AbilityDefinition> get(String id) {
        return Optional.ofNullable(ABILITIES.get(id));
    }

    public static synchronized Collection<AbilityDefinition> getAll() {
        return Collections.unmodifiableList(java.util.List.copyOf(ABILITIES.values()));
    }

    public static synchronized int size() {
        return ABILITIES.size();
    }
}