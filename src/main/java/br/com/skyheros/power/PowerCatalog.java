package br.com.skyheros.power;

/**
 * Biblioteca inicial: 20 temas x 25 modelos de habilidade = 500 presets.
 * Cada preset contém parâmetros; o comportamento real é fornecido por
 * executores modulares que serão conectados ao jogo.
 */
public final class PowerCatalog {
    private static boolean initialized;

    private PowerCatalog() {}

    private static final String[][] THEMES = {
        {"fire", "Fogo", "1.0"},
        {"ice", "Gelo", "1.0"},
        {"lightning", "Eletricidade", "1.0"},
        {"cosmic", "Energia cósmica", "1.2"},
        {"shadow", "Sombras", "1.0"},
        {"light", "Luz", "1.0"},
        {"nature", "Natureza", "1.0"},
        {"water", "Água", "1.0"},
        {"wind", "Vento", "1.0"},
        {"earth", "Terra", "1.1"},
        {"sound", "Som", "1.0"},
        {"plasma", "Plasma", "1.2"},
        {"gravity", "Gravidade", "1.2"},
        {"time", "Tempo", "1.5"},
        {"space", "Espaço", "1.3"},
        {"psychic", "Psíquico", "1.0"},
        {"technology", "Tecnologia", "1.0"},
        {"magic", "Magia", "1.1"},
        {"radiation", "Radiação", "1.2"},
        {"kinetic", "Energia cinética", "1.0"}
    };

    // id, nome, comportamento, cooldown, energia, magnitude, alvo
    private static final String[][] MECHANICS = {
        {"projectile", "Projétil", "PROJECTILE", "30", "10", "1.0", "true"},
        {"beam", "Raio", "BEAM", "50", "18", "1.2", "true"},
        {"burst", "Rajada", "BURST", "45", "16", "1.3", "true"},
        {"explosion", "Explosão", "EXPLOSION", "100", "30", "2.0", "true"},
        {"wave", "Onda", "AREA", "70", "22", "1.5", "false"},
        {"aura", "Aura", "AURA", "100", "12", "1.0", "false"},
        {"shield", "Escudo", "SHIELD", "100", "20", "1.0", "false"},
        {"barrier", "Barreira", "BARRIER", "120", "25", "1.5", "false"},
        {"dash", "Impulso", "MOVEMENT", "35", "8", "1.0", "false"},
        {"flight", "Voo", "FLIGHT", "0", "5", "1.0", "false"},
        {"leap", "Salto", "MOVEMENT", "40", "8", "1.2", "false"},
        {"speed", "Velocidade", "ATTRIBUTE", "0", "5", "1.0", "false"},
        {"strength", "Superforça", "ATTRIBUTE", "0", "8", "1.0", "false"},
        {"resistance", "Resistência", "ATTRIBUTE", "0", "8", "1.0", "false"},
        {"sense", "Sentido aprimorado", "SENSE", "0", "3", "1.0", "false"},
        {"vision", "Visão especial", "SENSE", "0", "5", "1.0", "false"},
        {"healing", "Cura", "HEAL", "100", "25", "1.0", "false"},
        {"drain", "Drenagem", "DRAIN", "80", "18", "1.0", "true"},
        {"bind", "Imobilização", "CONTROL", "90", "20", "1.0", "true"},
        {"pull", "Atração", "CONTROL", "60", "15", "1.0", "true"},
        {"push", "Repulsão", "CONTROL", "60", "15", "1.0", "true"},
        {"summon", "Invocação", "SUMMON", "140", "30", "1.0", "false"},
        {"construct", "Construção de energia", "CONSTRUCT", "100", "25", "1.0", "false"},
        {"transformation", "Transformação", "TRANSFORM", "0", "0", "1.0", "false"},
        {"utility", "Manipulação", "UTILITY", "50", "12", "1.0", "false"}
    };

    public static synchronized void registerDefaults() {
        if (initialized) return;

        for (String[] theme : THEMES) {
            for (String[] mechanic : MECHANICS) {
                String id = "skyheros:" + theme[0] + "_" + mechanic[0];
                String name = mechanic[1] + " de " + theme[1];
                double themeMultiplier = Double.parseDouble(theme[2]);
                double magnitude =
                        Double.parseDouble(mechanic[5]) * themeMultiplier;

                PowerRegistry.register(new PowerDefinition(
                        id,
                        name,
                        theme[0],
                        mechanic[0],
                        mechanic[2],
                        Integer.parseInt(mechanic[3]),
                        Double.parseDouble(mechanic[4]),
                        magnitude,
                        Boolean.parseBoolean(mechanic[6])
                ));
            }
        }

        initialized = true;
    }
}
