package br.com.skyheros.suit;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registro central dos trajes conhecidos pelo Skyheros.
 */
public final class SuitRegistry {
    private static final Map<String, SuitDefinition> SUITS =
            new LinkedHashMap<>();

    private SuitRegistry() {
    }

    public static synchronized void register(SuitDefinition suit) {
        if (suit == null) {
            throw new IllegalArgumentException("O traje não pode ser nulo.");
        }

        String id = suit.getId().toLowerCase(java.util.Locale.ROOT);

        if (SUITS.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Já existe um traje registrado com o ID: " + id);
        }

        SUITS.put(id, suit);
    }

    public static synchronized Optional<SuitDefinition> get(String id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(
                SUITS.get(id.toLowerCase(java.util.Locale.ROOT)));
    }

    public static synchronized Collection<SuitDefinition> getAll() {
        return Collections.unmodifiableList(
                new java.util.ArrayList<>(SUITS.values()));
    }

    public static synchronized int size() {
        return SUITS.size();
    }
}
