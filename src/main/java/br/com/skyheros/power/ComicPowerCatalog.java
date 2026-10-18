package br.com.skyheros.power;

public final class ComicPowerCatalog {
    private ComicPowerCatalog() {}

    private static final String[][] ENTRIES = {
        {"comic_burst_dash","Burst Dash","mobility","dash","dash_forward","60","8","1.8","false"},
        {"comic_air_drift","Air Drift","mobility","air_control","air_drift","40","5","0.8","false"},
        {"comic_wall_run","Wall Run","mobility","wall_run","run_along_wall","80","10","1.0","false"},
        {"comic_phase_step","Phase Step","mobility","short_teleport","teleport_short","100","15","5.0","false"},
        {"comic_heroic_flight","Heroic Flight","mobility","flight","flight","100","20","1.0","false"},

        {"comic_super_strength","Super Strength","physical","strength","super_strength","80","12","2.5","false"},
        {"comic_titan_strike","Titan Strike","physical","melee","heavy_melee","100","15","3.0","true"},
        {"comic_ground_breaker","Ground Breaker","physical","area_attack","ground_slam","120","20","4.0","false"},
        {"comic_leaping_assault","Leaping Assault","physical","leap","enhanced_leap","60","8","1.7","false"},
        {"comic_kinetic_grip","Kinetic Grip","physical","grapple","grapple_target","80","10","1.5","true"},

        {"comic_impact_shield","Impact Shield","defense","shield","impact_shield","100","15","3.0","false"},
        {"comic_adaptive_armor","Adaptive Armor","defense","resistance","damage_resistance","160","20","0.5","false"},
        {"comic_energy_barrier","Energy Barrier","defense","barrier","energy_barrier","120","18","4.0","false"},
        {"comic_reflex_guard","Reflex Guard","defense","parry","timed_guard","60","8","1.0","false"},
        {"comic_regenerative_shell","Regenerative Shell","defense","regeneration","regenerate","200","25","2.0","false"},

        {"comic_eagle_vision","Eagle Vision","senses","vision","enhanced_vision","80","6","24.0","false"},
        {"comic_thermal_sight","Thermal Sight","senses","thermal_vision","thermal_vision","80","6","24.0","false"},
        {"comic_sonar_pulse","Sonar Pulse","senses","sonar","sonar_pulse","100","10","16.0","false"},
        {"comic_threat_instinct","Threat Instinct","senses","danger_sense","danger_sense","120","12","1.0","false"},
        {"comic_tracking_focus","Tracking Focus","senses","tracking","track_target","100","10","32.0","true"},

        {"comic_energy_bolt","Energy Bolt","energy","projectile","energy_projectile","40","8","2.0","true"},
        {"comic_power_beam","Power Beam","energy","beam","energy_beam","80","15","3.0","true"},
        {"comic_energy_burst","Energy Burst","energy","area_burst","energy_burst","120","20","4.0","false"},
        {"comic_absorb_energy","Energy Absorption","energy","absorption","absorb_energy","140","15","2.0","false"},
        {"comic_overcharge","Overcharge","energy","self_buff","energy_overcharge","180","25","2.5","false"},

        {"comic_mind_push","Mind Push","psychic","telekinesis","telekinetic_push","80","12","2.0","true"},
        {"comic_mind_grip","Mind Grip","psychic","telekinesis","telekinetic_hold","100","15","1.0","true"},
        {"comic_psychic_blast","Psychic Blast","psychic","psychic_attack","psychic_blast","100","18","3.0","true"},
        {"comic_mental_veil","Mental Veil","psychic","concealment","mental_veil","160","18","1.0","false"},
        {"comic_focus_break","Focus Break","psychic","disruption","interrupt_target","100","12","1.0","true"},

        {"comic_fireball","Fireball","fire","projectile","fire_projectile","50","8","2.0","true"},
        {"comic_flame_wave","Flame Wave","fire","area_attack","flame_wave","100","15","3.0","false"},
        {"comic_thermal_aura","Thermal Aura","fire","aura","heat_aura","140","18","2.0","false"},
        {"comic_ember_trail","Ember Trail","fire","movement","ember_trail","80","10","1.5","false"},
        {"comic_inferno_burst","Inferno Burst","fire","explosion","inferno_burst","180","25","4.5","false"},

        {"comic_ice_shard","Ice Shard","ice","projectile","ice_projectile","50","8","2.0","true"},
        {"comic_frost_breath","Frost Breath","ice","cone_attack","frost_breath","100","15","3.0","false"},
        {"comic_ice_wall","Ice Wall","ice","barrier","ice_wall","140","18","3.0","false"},
        {"comic_freezing_touch","Freezing Touch","ice","melee_effect","freeze_target","80","10","1.0","true"},
        {"comic_blizzard_field","Blizzard Field","ice","area_effect","blizzard_field","180","25","4.0","false"},

        {"comic_lightning_bolt","Lightning Bolt","lightning","projectile","lightning_bolt","50","8","2.5","true"},
        {"comic_chain_lightning","Chain Lightning","lightning","chain_attack","chain_lightning","120","18","3.0","true"},
        {"comic_electric_pulse","Electric Pulse","lightning","area_pulse","electric_pulse","100","14","2.5","false"},
        {"comic_static_field","Static Field","lightning","field","static_field","140","18","2.0","false"},
        {"comic_overload_strike","Overload Strike","lightning","melee_attack","overload_strike","100","15","3.0","true"},

        {"comic_shadow_step","Shadow Step","shadow","teleport","shadow_teleport","100","15","6.0","false"},
        {"comic_darkness_field","Darkness Field","shadow","field","darkness_field","140","18","8.0","false"},
        {"comic_shadow_bind","Shadow Bind","shadow","bind","shadow_bind","100","15","1.0","true"},
        {"comic_shadow_clone","Shadow Clone","shadow","decoy","shadow_clone","180","25","1.0","false"},
        {"comic_void_strike","Void Strike","shadow","melee_attack","void_strike","100","15","3.0","true"},

        {"comic_blinding_flash","Blinding Flash","light","flash","blinding_flash","100","12","1.0","false"},
        {"comic_light_lance","Light Lance","light","projectile","light_lance","60","10","2.5","true"},
        {"comic_holographic_double","Holographic Double","light","illusion","light_illusion","160","20","1.0","false"},
        {"comic_photon_shield","Photon Shield","light","shield","photon_shield","120","18","3.0","false"},
        {"comic_radiant_pulse","Radiant Pulse","light","area_pulse","radiant_pulse","100","14","2.5","false"},

        {"comic_gravity_well","Gravity Well","gravity","field","gravity_well","160","22","3.0","false"},
        {"comic_gravity_push","Gravity Push","gravity","force","gravity_push","80","12","2.0","true"},
        {"comic_gravity_crush","Gravity Crush","gravity","target_effect","gravity_crush","120","20","3.5","true"},
        {"comic_low_gravity","Low Gravity","gravity","area_effect","low_gravity","140","18","8.0","false"},
        {"comic_gravity_anchor","Gravity Anchor","gravity","immobilize","gravity_anchor","100","15","1.0","true"},

        {"comic_wind_blast","Wind Blast","wind","projectile","wind_blast","60","8","2.0","true"},
        {"comic_whirlwind","Whirlwind","wind","area_attack","whirlwind","120","18","3.0","false"},
        {"comic_air_cushion","Air Cushion","wind","defense","air_cushion","100","12","2.0","false"},
        {"comic_gale_dash","Gale Dash","wind","movement","gale_dash","60","8","2.0","false"},
        {"comic_vacuum_pocket","Vacuum Pocket","wind","field","vacuum_pocket","140","18","3.0","false"},

        {"comic_water_jet","Water Jet","water","projectile","water_jet","50","8","2.0","true"},
        {"comic_tidal_surge","Tidal Surge","water","area_attack","tidal_surge","120","18","3.5","false"},
        {"comic_water_whip","Water Whip","water","grapple","water_whip","80","10","2.0","true"},
        {"comic_mist_form","Mist Form","water","transformation","mist_form","160","20","1.0","false"},
        {"comic_pressure_burst","Pressure Burst","water","area_pulse","pressure_burst","100","14","3.0","false"},

        {"comic_sonic_scream","Sonic Scream","sound","cone_attack","sonic_scream","100","15","3.0","false"},
        {"comic_sound_bolt","Sound Bolt","sound","projectile","sound_bolt","60","9","2.0","true"},
        {"comic_resonance_shield","Resonance Shield","sound","shield","resonance_shield","120","15","2.5","false"},
        {"comic_silence_field","Silence Field","sound","field","silence_field","140","18","8.0","false"},
        {"comic_vibration_strike","Vibration Strike","sound","melee_attack","vibration_strike","80","12","2.5","true"},

        {"comic_fast_healing","Fast Healing","biology","regeneration","fast_healing","180","20","2.0","false"},
        {"comic_muscle_boost","Muscle Boost","biology","self_buff","muscle_boost","120","15","2.0","false"},
        {"comic_bone_armor","Bone Armor","biology","defense","bone_armor","140","18","3.0","false"},
        {"comic_adaptive_mutation","Adaptive Mutation","biology","resistance","adaptive_mutation","200","25","2.0","false"},
        {"comic_bioelectric_touch","Bioelectric Touch","biology","melee_effect","bioelectric_touch","80","10","2.0","true"},

        {"comic_drone_scan","Drone Scan","technology","scan","drone_scan","100","12","24.0","false"},
        {"comic_micro_missile","Micro Missile","technology","projectile","micro_missile","80","12","3.0","true"},
        {"comic_hardlight_construct","Hardlight Construct","technology","construct","hardlight_construct","140","20","3.0","false"},
        {"comic_tactical_cloak","Tactical Cloak","technology","stealth","tactical_cloak","160","20","1.0","false"},
        {"comic_grappling_line","Grappling Line","technology","grapple","grappling_line","80","10","12.0","true"},

        {"comic_star_bolt","Star Bolt","space","projectile","star_bolt","60","10","3.0","true"},
        {"comic_cosmic_aura","Cosmic Aura","space","aura","cosmic_aura","160","22","3.0","false"},
        {"comic_meteor_call","Meteor Call","space","area_attack","meteor_call","200","30","5.0","false"},
        {"comic_void_resistance","Void Resistance","space","resistance","void_resistance","180","22","2.0","false"},
        {"comic_orbital_pulse","Orbital Pulse","space","area_pulse","orbital_pulse","140","20","4.0","false"},

        {"comic_vine_snare","Vine Snare","nature","bind","vine_snare","100","12","1.0","true"},
        {"comic_root_wall","Root Wall","nature","barrier","root_wall","140","18","3.0","false"},
        {"comic_pollen_cloud","Pollen Cloud","nature","area_effect","pollen_cloud","120","15","3.0","false"},
        {"comic_thorn_volley","Thorn Volley","nature","projectile","thorn_volley","80","12","2.5","true"},
        {"comic_nature_regrowth","Nature Regrowth","nature","regeneration","nature_regrowth","180","22","2.0","false"},

        {"comic_iron_skin","Iron Skin","metal","defense","iron_skin","160","20","3.0","false"},
        {"comic_metal_shards","Metal Shards","metal","projectile","metal_shards","60","10","2.5","true"},
        {"comic_magnetic_pull","Magnetic Pull","metal","force","magnetic_pull","100","15","2.0","true"},
        {"comic_metal_barrier","Metal Barrier","metal","barrier","metal_barrier","140","18","3.5","false"},
        {"comic_magnetic_pulse","Magnetic Pulse","metal","area_pulse","magnetic_pulse","120","16","3.0","false"}
    };

    public static void registerDefaults() {
        for (String[] e : ENTRIES) {
            if (e.length != 9) {
                throw new IllegalStateException("Entrada invalida no catalogo: " + e[0]);
            }
            PowerRegistry.register(new PowerDefinition(
                "skyheros:" + e[0],
                e[1],
                e[2],
                e[3],
                e[4],
                Integer.parseInt(e[5]),
                Double.parseDouble(e[6]),
                Double.parseDouble(e[7]),
                Boolean.parseBoolean(e[8])
            ));
        }
    }

    public static int size() {
        return ENTRIES.length;
    }
}