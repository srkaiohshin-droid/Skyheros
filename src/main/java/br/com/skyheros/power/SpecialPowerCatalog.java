package br.com.skyheros.power;

public final class SpecialPowerCatalog {
    private SpecialPowerCatalog() {}

    public static void registerDefaults() {
        // TRANSFORMAÇÕES (20)
        add("spec_hulk_form", "Hulk Form", "physical", "transformation", "hulk_form", 240, 25, 8, false);
        add("spec_titan_form", "Titan Form", "physical", "transformation", "titan_form", 240, 25, 8, false);
        add("spec_beast_mode", "Beast Mode", "physical", "transformation", "beast_mode", 180, 18, 7, false);
        add("spec_berserker_form", "Berserker Form", "physical", "transformation", "berserker_form", 200, 22, 8, false);
        add("spec_phoenix_ascension", "Phoenix Ascension", "fire", "transformation", "phoenix_ascension", 300, 32, 8, false);
        add("spec_kryptonian_overdrive", "Kryptonian Overdrive", "solar", "transformation", "kryptonian_overdrive", 260, 28, 8, false);
        add("spec_speedster_overdrive", "Speedster Overdrive", "speed", "transformation", "speedster_overdrive", 180, 20, 8, false);
        add("spec_shadow_form", "Shadow Form", "shadow", "transformation", "shadow_form", 180, 18, 6, false);
        add("spec_cosmic_avatar", "Cosmic Avatar", "cosmic", "transformation", "cosmic_avatar", 300, 32, 8, false);
        add("spec_elemental_avatar", "Elemental Avatar", "elemental", "transformation", "elemental_avatar", 260, 28, 8, false);
        add("spec_energy_form", "Energy Form", "energy", "transformation", "energy_form", 220, 24, 7, false);
        add("spec_diamond_skin_form", "Diamond Skin Form", "defense", "transformation", "diamond_skin_form", 220, 22, 7, false);
        add("spec_elastic_form", "Elastic Form", "physical", "transformation", "elastic_form", 180, 18, 6, false);
        add("spec_giant_form", "Giant Form", "physical", "transformation", "giant_form", 260, 28, 8, false);
        add("spec_micro_form", "Micro Form", "stealth", "transformation", "micro_form", 180, 18, 5, false);
        add("spec_adaptive_evolution", "Adaptive Evolution", "evolution", "transformation", "adaptive_evolution", 300, 32, 8, false);
        add("spec_battle_focus", "Battle Focus", "combat", "transformation", "battle_focus", 140, 14, 6, false);
        add("spec_rage_state", "Rage State", "physical", "transformation", "rage_state", 180, 18, 7, false);
        add("spec_ultimate_mode", "Ultimate Mode", "energy", "transformation", "ultimate_mode", 320, 36, 8, false);
        add("spec_omega_state", "Omega State", "cosmic", "transformation", "omega_state", 320, 36, 8, false);

        // INVOCACOES (20)
        add("spec_wolf_pack_call", "Wolf Pack Call", "summoning", "summon", "summon_wolves", 200, 18, 5, false);
        add("spec_iron_guardian_call", "Iron Guardian Call", "summoning", "summon", "summon_iron_golem", 300, 30, 8, false);
        add("spec_snow_guardian_call", "Snow Guardian Call", "summoning", "summon", "summon_snow_golem", 220, 22, 6, false);
        add("spec_skeleton_archers", "Skeleton Archers", "summoning", "summon", "summon_skeleton_archers", 260, 24, 7, false);
        add("spec_bee_swarm", "Bee Swarm", "summoning", "summon", "summon_bees", 180, 15, 5, false);
        add("spec_companion_spirits", "Companion Spirits", "summoning", "summon", "summon_spirit_companions", 240, 22, 6, false);
        add("spec_shadow_familiar", "Shadow Familiar", "shadow", "summon", "summon_shadow_familiar", 220, 20, 6, false);
        add("spec_elemental_familiar", "Elemental Familiar", "elemental", "summon", "summon_elemental_familiar", 240, 24, 7, false);
        add("spec_spirit_wolves", "Spirit Wolves", "summoning", "summon", "summon_spirit_wolves", 240, 22, 6, false);
        add("spec_guardian_golem", "Guardian Golem", "summoning", "summon", "summon_guardian_golem", 300, 30, 8, false);
        add("spec_phantom_guard", "Phantom Guard", "summoning", "summon", "summon_phantom_guard", 260, 25, 7, false);
        add("spec_bone_legion", "Bone Legion", "summoning", "summon", "summon_bone_legion", 300, 30, 8, false);
        add("spec_forest_guardians", "Forest Guardians", "nature", "summon", "summon_forest_guardians", 260, 24, 7, false);
        add("spec_swarm_summon", "Swarm Summon", "summoning", "summon", "summon_swarm", 220, 20, 6, false);
        add("spec_sentinel_call", "Sentinel Call", "summoning", "summon", "summon_sentinels", 280, 28, 7, false);
        add("spec_battle_hounds", "Battle Hounds", "summoning", "summon", "summon_battle_hounds", 240, 22, 6, false);
        add("spec_spectral_companion", "Spectral Companion", "shadow", "summon", "summon_spectral_companion", 240, 22, 6, false);
        add("spec_minion_horde", "Minion Horde", "summoning", "summon", "summon_minion_horde", 320, 32, 8, false);
        add("spec_guardian_call", "Guardian Call", "summoning", "summon", "summon_guardians", 260, 25, 7, false);
        add("spec_familiar_call", "Familiar Call", "summoning", "summon", "summon_familiar", 200, 18, 5, false);

        // CLIMA, TEMPO E CONTROLE AMBIENTAL (15)
        add("spec_storm_call", "Storm Call", "weather", "environment", "storm_call", 260, 26, 8, false);
        add("spec_lightning_storm_domain", "Lightning Storm Domain", "electric", "environment", "lightning_storm_domain", 300, 32, 8, false);
        add("spec_rain_domain", "Rain Domain", "weather", "environment", "rain_domain", 180, 16, 5, false);
        add("spec_fog_domain", "Fog Domain", "weather", "environment", "fog_domain", 180, 18, 5, false);
        add("spec_blizzard_domain", "Blizzard Domain", "ice", "environment", "blizzard_domain", 260, 26, 8, false);
        add("spec_heatwave_domain", "Heatwave Domain", "fire", "environment", "heatwave_domain", 240, 24, 7, false);
        add("spec_sandstorm_domain", "Sandstorm Domain", "earth", "environment", "sandstorm_domain", 220, 22, 7, false);
        add("spec_earthquake_pulse", "Earthquake Pulse", "earth", "environment", "earthquake_pulse", 180, 18, 7, false);
        add("spec_seismic_domain", "Seismic Domain", "earth", "environment", "seismic_domain", 260, 26, 8, false);
        add("spec_time_stop", "Time Stop", "time", "environment", "time_stop", 320, 36, 8, false);
        add("spec_temporal_stasis", "Temporal Stasis", "time", "environment", "temporal_stasis", 280, 30, 8, false);
        add("spec_time_dilation", "Time Dilation", "time", "environment", "time_dilation", 240, 25, 7, false);
        add("spec_gravity_field", "Gravity Field", "gravity", "environment", "gravity_field", 240, 24, 7, false);
        add("spec_low_gravity_field", "Low Gravity Field", "gravity", "environment", "low_gravity_field", 200, 20, 6, false);
        add("spec_magnetic_field", "Magnetic Field", "magnetic", "environment", "magnetic_field", 220, 22, 7, false);

        // HABILIDADES PASSIVAS E ADAPTACOES (20)
        add("spec_aqua_breathing", "Aqua Breathing", "water", "passive", "aqua_breathing", 100, 8, 4, false);
        add("spec_deep_sea_adaptation", "Deep Sea Adaptation", "water", "passive", "deep_sea_adaptation", 140, 12, 5, false);
        add("spec_thermal_immunity", "Thermal Immunity", "fire", "passive", "thermal_immunity", 140, 12, 5, false);
        add("spec_toxic_immunity", "Toxic Immunity", "defense", "passive", "toxic_immunity", 140, 12, 5, false);
        add("spec_kinetic_absorption", "Kinetic Absorption", "defense", "passive", "kinetic_absorption", 180, 18, 6, false);
        add("spec_impact_resistance", "Impact Resistance", "defense", "passive", "impact_resistance", 160, 16, 6, false);
        add("spec_regenerative_cells", "Regenerative Cells", "healing", "passive", "regenerative_cells", 200, 20, 7, false);
        add("spec_enhanced_reflexes", "Enhanced Reflexes", "speed", "passive", "enhanced_reflexes", 140, 12, 5, false);
        add("spec_danger_instinct", "Danger Instinct", "perception", "passive", "danger_instinct", 140, 12, 5, false);
        add("spec_night_stalker", "Night Stalker", "stealth", "passive", "night_stalker", 160, 16, 5, false);
        add("spec_predator_senses", "Predator Senses", "perception", "passive", "predator_senses", 140, 12, 5, false);
        add("spec_xray_perception", "X-Ray Perception", "perception", "passive", "xray_perception", 160, 16, 5, false);
        add("spec_magnetic_sense", "Magnetic Sense", "magnetic", "passive", "magnetic_sense", 140, 12, 5, false);
        add("spec_energy_reserves", "Energy Reserves", "energy", "passive", "energy_reserves", 180, 18, 6, false);
        add("spec_rapid_recovery", "Rapid Recovery", "healing", "passive", "rapid_recovery", 180, 18, 6, false);
        add("spec_elemental_resistance", "Elemental Resistance", "defense", "passive", "elemental_resistance", 180, 18, 6, false);
        add("spec_poison_purge", "Poison Purge", "healing", "passive", "poison_purge", 140, 12, 5, false);
        add("spec_status_immunity", "Status Immunity", "defense", "passive", "status_immunity", 220, 22, 7, false);
        add("spec_silent_movement", "Silent Movement", "stealth", "passive", "silent_movement", 140, 12, 5, false);
        add("spec_wall_crawling", "Wall Crawling", "movement", "passive", "wall_crawling", 160, 16, 5, false);

        // MENTE, ESPACO E MANIPULACAO (15)
        add("spec_telekinetic_grip", "Telekinetic Grip", "psychic", "control", "telekinetic_grip", 140, 14, 6, true);
        add("spec_mind_control_pulse", "Mind Control Pulse", "psychic", "control", "mind_control_pulse", 180, 18, 6, true);
        add("spec_mind_shield", "Mind Shield", "psychic", "passive", "mind_shield", 160, 16, 5, false);
        add("spec_astral_projection", "Astral Projection", "psychic", "utility", "astral_projection", 220, 22, 6, false);
        add("spec_dimensional_shift", "Dimensional Shift", "space", "movement", "dimensional_shift", 180, 18, 6, false);
        add("spec_portal_step", "Portal Step", "space", "movement", "portal_step", 140, 14, 5, false);
        add("spec_pocket_dimension", "Pocket Dimension", "space", "movement", "pocket_dimension", 220, 22, 7, false);
        add("spec_spatial_anchor", "Spatial Anchor", "space", "control", "spatial_anchor", 140, 14, 5, true);
        add("spec_gravity_lock", "Gravity Lock", "gravity", "control", "gravity_lock", 160, 16, 6, true);
        add("spec_gravity_reverse", "Gravity Reverse", "gravity", "control", "gravity_reverse", 180, 18, 6, true);
        add("spec_matter_manipulation", "Matter Manipulation", "matter", "attack", "matter_manipulation", 180, 18, 7, true);
        add("spec_molecular_disruption", "Molecular Disruption", "matter", "attack", "molecular_disruption", 200, 22, 8, true);
        add("spec_phase_shift", "Phase Shift", "space", "utility", "phase_shift", 160, 16, 6, false);
        add("spec_energy_construct", "Energy Construct", "energy", "defense", "energy_construct", 180, 18, 6, false);
        add("spec_force_construct", "Force Construct", "force", "defense", "force_construct", 180, 18, 6, false);

        // ATAQUES DE ENERGIA AVANCADA (10)
        add("spec_omega_beam", "Omega Beam", "cosmic", "attack", "omega_beam", 180, 20, 8, true);
        add("spec_sonic_spear", "Sonic Spear", "sonic", "attack", "sonic_spear", 100, 10, 6, true);
        add("spec_photon_lance", "Photon Lance", "light", "attack", "photon_lance", 120, 12, 7, true);
        add("spec_quantum_blast", "Quantum Blast", "quantum", "attack", "quantum_blast", 180, 20, 8, true);
        add("spec_neutron_burst", "Neutron Burst", "nuclear", "attack", "neutron_burst", 200, 22, 8, true);
        add("spec_antimatter_ray", "Antimatter Ray", "cosmic", "attack", "antimatter_ray", 220, 25, 8, true);
        add("spec_plasma_storm", "Plasma Storm", "plasma", "attack", "plasma_storm", 200, 22, 8, true);
        add("spec_ricochet_blast", "Ricochet Blast", "energy", "attack", "ricochet_blast", 120, 12, 6, true);
        add("spec_piercing_laser", "Piercing Laser", "light", "attack", "piercing_laser", 100, 10, 6, true);
        add("spec_energy_barrage", "Energy Barrage", "energy", "attack", "energy_barrage", 180, 20, 8, true);
    }

    private static void add(String id, String name, String theme, String mechanic,
                            String behavior, int cooldownTicks, double energyCost,
                            double magnitude, boolean requiresTarget) {
        PowerRegistry.register(new PowerDefinition(
                id, name, theme, mechanic, behavior,
                cooldownTicks, energyCost, magnitude, requiresTarget
        ));
    }
}