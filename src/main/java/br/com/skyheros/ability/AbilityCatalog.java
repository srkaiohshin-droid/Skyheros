package br.com.skyheros.ability;

import java.util.List;

public final class AbilityCatalog {
    private static boolean registered;

    private AbilityCatalog() {}

    public static synchronized void registerDefaults() {
        if (registered) return;

        // Habilidades-base com IDs estaveis para personagens e comandos existentes.
        add("web_swing", "Balanco com teia",
            "Usa um ponto de ancoragem para impulsionar o heroi.",
            "WEB_SWING", List.of(), 12, 5);
        add("web_shot", "Disparo de teia",
            "Dispara contra uma criatura na direcao da mira.",
            "WEB_SHOT", List.of(), 10, 3);
        add("web_pull", "Puxao de teia",
            "Puxa uma criatura atingida em direcao ao heroi.",
            "WEB_PULL", List.of(), 20, 5);
        add("web_bind", "Imobilizacao com teia",
            "Reduz temporariamente a mobilidade de um alvo.",
            "WEB_BIND", List.of(), 40, 8);
        add("spider_sense", "Sentido-aranha",
            "Melhora temporariamente mobilidade e defesa.",
            "SPIDER_SENSE", List.of(), 100, 10);
        add("enhanced_leap", "Salto aprimorado",
            "Impulsiona o heroi para cima e para a frente.",
            "ENHANCED_LEAP", List.of("enhanced_agility"), 30, 5);
        add("super_strength", "Forca aprimorada",
            "Concede temporariamente forca de combate adicional.",
            "SUPER_STRENGTH", List.of("enhanced_strength"), 100, 10);

        String[][] themes = {
            {"fire","Fogo"}, {"ice","Gelo"}, {"lightning","Eletricidade"},
            {"water","Agua"}, {"wind","Vento"}, {"earth","Terra"},
            {"plant","Plantas"}, {"nature","Natureza"}, {"light","Luz"},
            {"shadow","Sombras"}, {"cosmic","Energia cosmica"},
            {"plasma","Plasma"}, {"radiation","Radiacao"}, {"sound","Som"},
            {"gravity","Gravidade"}, {"magnetic","Magnetismo"},
            {"psychic","Energia psiquica"}, {"magic","Magia"},
            {"technology","Tecnologia"}, {"quantum","Energia quantica"},
            {"space","Espaco"}, {"time","Tempo"}, {"dimension","Dimensoes"},
            {"crystal","Cristais"}, {"metal","Metal"}, {"sand","Areia"},
            {"lava","Lava"}, {"storm","Tempestade"}, {"solar","Energia solar"},
            {"lunar","Energia lunar"}, {"void","Vazio"}, {"energy","Energia"},
            {"kinetic","Energia cinetica"}, {"life","Vida"}, {"death","Morte"},
            {"dream","Sonhos"}, {"illusion","Ilusao"}, {"blood","Sangue"},
            {"spider","Aranha"}, {"sonic","Energia sonica"}
        };

        // Cada familia representa uma forma de habilidade reutilizavel.
        // Os executores de todas as familias serao implementados progressivamente.
        String[][] mechanics = {
            {"projectile","Projetil","PROJECTILE"},
            {"beam","Feixe","BEAM"},
            {"burst","Rajada","BURST"},
            {"explosion","Explosao","EXPLOSION"},
            {"wave","Onda","WAVE"},
            {"aura","Aura","AURA"},
            {"shield","Escudo","SHIELD"},
            {"barrier","Barreira","BARRIER"},
            {"dash","Avanco","DASH"},
            {"flight","Voo","FLIGHT"},
            {"leap","Salto","LEAP"},
            {"speed","Velocidade","SPEED"},
            {"strength","Forca","STRENGTH"},
            {"resistance","Resistencia","RESISTANCE"},
            {"sense","Percepcao","SENSE"},
            {"vision","Visao especial","VISION"},
            {"healing","Cura","HEAL"},
            {"drain","Drenagem","DRAIN"},
            {"bind","Imobilizacao","BIND"},
            {"pull","Atracao","PULL"},
            {"push","Repulsao","PUSH"},
            {"summon","Invocacao","SUMMON"},
            {"construct","Construcao","CONSTRUCT"},
            {"transformation","Transformacao","TRANSFORM"},
            {"utility","Utilidade","UTILITY"}
        };

        int generated = 0;
        final int target = 1000;

        outer:
        for (String[] theme : themes) {
            for (String[] mechanic : mechanics) {
                if (AbilityRegistry.size() >= target) break outer;

                String id = theme[0] + "_" + mechanic[0];
                if (AbilityRegistry.get(id).isPresent()) continue;

                String name = mechanic[1] + " de " + theme[1];
                String description = mechanic[1] + " com tema de " + theme[1]
                    + ". Esta definicao utiliza a arquitetura modular do Skyheros.";

                int cooldown = 20 + (generated % 9) * 10;
                int energy = 3 + (generated % 8) * 2;

                add(id, name, description, mechanic[2], List.of(), cooldown, energy);
                generated++;
            }
        }

        registered = true;

        if (AbilityRegistry.size() != target) {
            throw new IllegalStateException(
                "Catalogo Skyheros deveria conter " + target +
                " habilidades, mas contem " + AbilityRegistry.size()
            );
        }
    }

    private static void add(
        String id,
        String name,
        String description,
        String behavior,
        List<String> powers,
        int cooldown,
        int energy
    ) {
        AbilityRegistry.register(new AbilityDefinition(
            id, name, description, behavior, powers, cooldown, energy
        ));
    }
}