package br.com.skyheros.hero;

import java.util.List;

public record HeroDefinition(
    String id,
    String name,
    List<String> powers,
    List<String> abilities
) {
    public HeroDefinition {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Hero id vazio");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Hero name vazio");
        powers = powers == null ? List.of() : List.copyOf(powers);
        abilities = abilities == null ? List.of() : List.copyOf(abilities);
    }
}