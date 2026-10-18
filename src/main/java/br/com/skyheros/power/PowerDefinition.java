package br.com.skyheros.power;

import java.util.Objects;

public final class PowerDefinition {
    private final String id;
    private final String name;
    private final String theme;
    private final String mechanic;
    private final String behavior;
    private final int cooldownTicks;
    private final double energyCost;
    private final double magnitude;
    private final boolean requiresTarget;

    public PowerDefinition(
            String id, String name, String theme, String mechanic,
            String behavior, int cooldownTicks, double energyCost,
            double magnitude, boolean requiresTarget) {
        this.id = requireText(id);
        this.name = requireText(name);
        this.theme = requireText(theme);
        this.mechanic = requireText(mechanic);
        this.behavior = requireText(behavior);

        if (cooldownTicks < 0 || energyCost < 0 || magnitude < 0) {
            throw new IllegalArgumentException(
                    "Cooldown, custo e magnitude não podem ser negativos.");
        }

        this.cooldownTicks = cooldownTicks;
        this.energyCost = energyCost;
        this.magnitude = magnitude;
        this.requiresTarget = requiresTarget;
    }

    private static String requireText(String value) {
        Objects.requireNonNull(value, "Campo obrigatório");
        if (value.isBlank()) {
            throw new IllegalArgumentException("Campo vazio");
        }
        return value;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getTheme() { return theme; }
    public String getMechanic() { return mechanic; }
    public String getBehavior() { return behavior; }
    public int getCooldownTicks() { return cooldownTicks; }
    public double getEnergyCost() { return energyCost; }
    public double getMagnitude() { return magnitude; }
    public boolean requiresTarget() { return requiresTarget; }
}
