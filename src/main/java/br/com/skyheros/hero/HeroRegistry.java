package br.com.skyheros.hero;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class HeroRegistry {
    private static final Map<String, HeroDefinition> HEROES = new LinkedHashMap<>();

    private HeroRegistry() {}

    public static synchronized void register(HeroDefinition hero) {
        if (HEROES.putIfAbsent(hero.id(), hero) != null) {
            throw new IllegalArgumentException("Personagem duplicado: " + hero.id());
        }
    }

    public static synchronized Optional<HeroDefinition> get(String id) {
        return Optional.ofNullable(HEROES.get(id));
    }

    public static synchronized Collection<HeroDefinition> getAll() {
        return Collections.unmodifiableList(java.util.List.copyOf(HEROES.values()));
    }

    public static synchronized int size() {
        return HEROES.size();
    }
}