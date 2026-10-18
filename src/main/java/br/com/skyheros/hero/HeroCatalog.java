package br.com.skyheros.hero;

import java.util.List;

public final class HeroCatalog {
    private static boolean registered;

    private HeroCatalog() {}

    public static synchronized void registerDefaults() {
        if (registered) return;

        HeroRegistry.register(new HeroDefinition(
            "spider_man",
            "Homem-Aranha",
            List.of("enhanced_strength", "enhanced_agility", "spider_sense"),
            List.of("web_swing", "web_shot", "web_pull", "web_bind",
                    "spider_sense", "enhanced_leap", "super_strength")
        ));

        registered = true;
    }
}