package br.com.skyheros.power;

public final class ComboPowerCatalog {
    private static boolean initialized = false;

    private ComboPowerCatalog() {}

    public static synchronized void registerDefaults() {
        if (initialized) return;

        add("combo_frostfire_burst", "Frostfire Burst",
            "fire_ice", "blast", "area_blast", 300, 32.0, 10.0, false);

        add("combo_ultimate_singularity", "Ultimate Singularity",
            "cosmic", "nova", "area_blast", 600, 50.0, 14.0, false);

        add("combo_team_regeneration", "Team Regeneration",
            "nature", "aura", "team_regeneration", 360, 35.0, 8.0, false);

        add("combo_energy_recharge", "Energy Recharge",
            "cosmic", "focus", "energy_recharge", 240, 20.0, 7.0, false);

        add("combo_summon_iron_sentinel", "Summon Iron Sentinel",
            "tech", "summon", "summon_guardian", 420, 38.0, 10.0, false);

        add("combo_stormbreaker_strike", "Stormbreaker Strike",
            "storm", "strike", "target_attack", 180, 25.0, 9.0, true);

        add("combo_shadow_bind", "Shadow Bind",
            "shadow", "bind", "control", 220, 26.0, 7.0, true);

        add("combo_radiant_barrier", "Radiant Barrier",
            "radiant", "barrier", "defense", 300, 30.0, 9.0, false);

        add("combo_kinetic_overdrive", "Kinetic Overdrive",
            "kinetic", "overdrive", "movement", 260, 28.0, 9.0, false);

        add("combo_toxic_drain", "Toxic Drain",
            "nature", "drain", "drain_attack", 200, 25.0, 8.0, true);

        add("combo_solar_beam", "Solar Beam",
            "radiant", "beam", "target_attack", 180, 24.0, 9.0, true);

        add("combo_void_wave", "Void Wave",
            "shadow", "wave", "area_blast", 320, 34.0, 10.0, false);

        add("combo_earth_guard", "Earth Guard",
            "nature", "shield", "defense", 280, 28.0, 9.0, false);

        add("combo_plasma_barrage", "Plasma Barrage",
            "tech", "barrage", "area_blast", 300, 34.0, 10.0, false);

        add("combo_phantom_dash", "Phantom Dash",
            "shadow", "dash", "movement", 180, 20.0, 7.0, false);

        add("combo_thunder_field", "Thunder Field",
            "storm", "field", "area_attack", 300, 32.0, 10.0, false);

        add("combo_life_transfer", "Life Transfer",
            "nature", "drain", "life_transfer", 240, 28.0, 8.0, true);

        add("combo_gravity_crush", "Gravity Crush",
            "cosmic", "strike", "target_attack", 240, 30.0, 10.0, true);

        add("combo_aurora_pulse", "Aurora Pulse",
            "radiant", "pulse", "area_blast", 260, 30.0, 9.0, false);

        add("combo_primal_awakening", "Primal Awakening",
            "nature", "ultimate", "ultimate", 600, 50.0, 14.0, false);

        initialized = true;
    }

    private static void add(
            String id, String name, String theme, String mechanic,
            String behavior, int cooldownTicks, double energyCost,
            double magnitude, boolean requiresTarget) {

        PowerRegistry.register(new PowerDefinition(
            id, name, theme, mechanic, behavior,
            cooldownTicks, energyCost, magnitude, requiresTarget
        ));
    }
}