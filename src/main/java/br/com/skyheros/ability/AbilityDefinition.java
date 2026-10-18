package br.com.skyheros.ability;

import java.util.List;

public record AbilityDefinition(
    String id,
    String name,
    String description,
    String behavior,
    List<String> requiredPowers,
    int cooldownTicks,
    int energyCost
) {
    public AbilityDefinition {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Ability id vazio");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Ability name vazio");
        if (behavior == null || behavior.isBlank()) throw new IllegalArgumentException("Ability behavior vazio");
        requiredPowers = requiredPowers == null ? List.of() : List.copyOf(requiredPowers);
        cooldownTicks = Math.max(0, cooldownTicks);
        energyCost = Math.max(0, energyCost);
    }
}