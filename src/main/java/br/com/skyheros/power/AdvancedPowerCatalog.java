package br.com.skyheros.power;

public final class AdvancedPowerCatalog {
    private static boolean initialized = false;

    private static final String[] THEMES = {
        "cosmic", "fire", "ice", "storm",
        "shadow", "radiant", "nature", "kinetic",
        "tech"
    };

    private static final String[] MECHANICS = {
        "bolt", "blast", "beam", "nova", "pulse",
        "wave", "strike", "barrage", "field", "aura",
        "shield", "barrier", "dash", "surge", "focus",
        "drain", "bind", "scan", "overdrive", "ultimate"
    };

    private AdvancedPowerCatalog() {}

    public static synchronized void registerDefaults() {
        if (initialized) return;

        int registered = 0;

        for (String theme : THEMES) {
            for (String mechanic : MECHANICS) {
                String id = "adv_" + theme + "_" + mechanic;
                String name = title(theme) + " " + title(mechanic);

                int cooldown = cooldownFor(mechanic);
                double energy = energyFor(mechanic);
                double magnitude = magnitudeFor(mechanic);
                boolean target = requiresTarget(mechanic);

                PowerRegistry.register(new PowerDefinition(
                    id,
                    name,
                    theme,
                    mechanic,
                    mechanic,
                    cooldown,
                    energy,
                    magnitude,
                    target
                ));

                registered++;
            }
        }

        if (registered != 180) {
            throw new IllegalStateException(
                "Esperados 180 poderes avancados, registrados: " + registered
            );
        }

        initialized = true;
    }

    private static String title(String value) {
        if (value == null || value.isEmpty()) return value;
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    private static int cooldownFor(String mechanic) {
        switch (mechanic) {
            case "ultimate":   return 600;
            case "nova":
            case "overdrive":  return 360;
            case "barrage":
            case "barrier":
            case "field":      return 240;
            case "blast":
            case "wave":
            case "shield":     return 180;
            case "bolt":
            case "beam":
            case "strike":     return 100;
            case "dash":
            case "surge":      return 120;
            default:           return 160;
        }
    }

    private static double energyFor(String mechanic) {
        switch (mechanic) {
            case "ultimate":   return 50.0;
            case "nova":
            case "overdrive":  return 35.0;
            case "barrage":
            case "barrier":
            case "field":      return 28.0;
            case "blast":
            case "wave":
            case "shield":     return 24.0;
            case "bolt":
            case "beam":
            case "strike":     return 15.0;
            case "dash":
            case "surge":      return 12.0;
            default:           return 18.0;
        }
    }

    private static double magnitudeFor(String mechanic) {
        switch (mechanic) {
            case "ultimate":   return 14.0;
            case "nova":
            case "overdrive":  return 12.0;
            case "barrage":
            case "barrier":
            case "field":      return 10.0;
            case "blast":
            case "wave":
            case "shield":     return 9.0;
            case "bolt":
            case "beam":
            case "strike":     return 7.0;
            case "dash":
            case "surge":      return 6.0;
            default:           return 5.0;
        }
    }

    private static boolean requiresTarget(String mechanic) {
        switch (mechanic) {
            case "bolt":
            case "beam":
            case "strike":
            case "drain":
            case "bind":
                return true;
            default:
                return false;
        }
    }
}