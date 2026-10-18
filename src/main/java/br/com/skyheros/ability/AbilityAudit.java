package br.com.skyheros.ability;

import java.util.Locale;
import java.util.Set;

public final class AbilityAudit {
    private static final Set<String> SUPPORTED_BEHAVIORS = Set.of(
        "WEB_SWING", "WEB_SHOT", "WEB_PULL", "WEB_BIND",
        "SPIDER_SENSE", "ENHANCED_LEAP", "SUPER_STRENGTH",
        "PROJECTILE", "BEAM", "BURST", "EXPLOSION", "WAVE",
        "AURA", "SHIELD", "BARRIER", "DASH", "FLIGHT", "LEAP",
        "SPEED", "STRENGTH", "RESISTANCE", "SENSE", "VISION",
        "HEAL", "DRAIN", "BIND", "PULL", "PUSH", "SUMMON",
        "CONSTRUCT", "TRANSFORM", "UTILITY"
    );

    private AbilityAudit() {}

    public static int totalCount() {
        return AbilityRegistry.size();
    }

    public static long implementedCount() {
        return AbilityRegistry.getAll().stream()
                .filter(ability -> ability.behavior() != null
                        && SUPPORTED_BEHAVIORS.contains(
                            ability.behavior().toUpperCase(Locale.ROOT)))
                .count();
    }

    public static long pendingCount() {
        return totalCount() - implementedCount();
    }
}