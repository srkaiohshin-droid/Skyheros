package br.com.skyheros.suit;

import java.util.List;
import java.util.Objects;

/**
 * Dados centrais de um traje do Skyheros.
 * Os multiplicadores ainda precisam ser aplicados às mecânicas do jogo.
 */
public final class SuitDefinition {
    private final String id;
    private final String displayName;
    private final double strengthMultiplier;
    private final double speedMultiplier;
    private final double resistanceMultiplier;
    private final boolean flightEnabled;
    private final List<String> abilities;

    public SuitDefinition(
            String id,
            String displayName,
            double strengthMultiplier,
            double speedMultiplier,
            double resistanceMultiplier,
            boolean flightEnabled,
            List<String> abilities
    ) {
        this.id = requireText(id, "id");
        this.displayName = requireText(displayName, "displayName");

        if (strengthMultiplier < 0 || speedMultiplier < 0
                || resistanceMultiplier < 0) {
            throw new IllegalArgumentException(
                    "Os multiplicadores não podem ser negativos.");
        }

        this.strengthMultiplier = strengthMultiplier;
        this.speedMultiplier = speedMultiplier;
        this.resistanceMultiplier = resistanceMultiplier;
        this.flightEnabled = flightEnabled;
        this.abilities = List.copyOf(
                Objects.requireNonNull(abilities, "abilities"));
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " não pode ser vazio.");
        }
        return value;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getStrengthMultiplier() {
        return strengthMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public double getResistanceMultiplier() {
        return resistanceMultiplier;
    }

    public boolean isFlightEnabled() {
        return flightEnabled;
    }

    public List<String> getAbilities() {
        return abilities;
    }
}
