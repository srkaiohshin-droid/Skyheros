package br.com.skyheros.suit;

import java.util.List;

/**
 * Catálogo inicial de trajes oficiais do mod.
 */
public final class SuitCatalog {
    private static boolean initialized;

    private SuitCatalog() {
    }

    public static synchronized void registerDefaults() {
        if (initialized) {
            return;
        }

        SuitRegistry.register(new SuitDefinition(
                "skyheros:superman",
                "Superman",
                10.0,
                3.0,
                10.0,
                true,
                List.of(
                        "super_strength",
                        "flight",
                        "super_speed",
                        "enhanced_durability",
                        "heat_vision",
                        "freeze_breath"
                )
        ));

        initialized = true;
    }
}
