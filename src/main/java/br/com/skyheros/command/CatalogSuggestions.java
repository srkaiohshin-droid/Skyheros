package br.com.skyheros.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

public final class CatalogSuggestions {
    private static final String POWER_REGISTRY = "br.com.skyheros.power.PowerRegistry";
    private static final String ABILITY_REGISTRY = "br.com.skyheros.ability.AbilityRegistry";

    private CatalogSuggestions() {}

    public static CompletableFuture<Suggestions> powers(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder) {
        return suggestFromRegistry(POWER_REGISTRY, builder);
    }

    public static CompletableFuture<Suggestions> abilities(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder) {
        return suggestFromRegistry(ABILITY_REGISTRY, builder);
    }

    private static CompletableFuture<Suggestions> suggestFromRegistry(
            String className,
            SuggestionsBuilder builder) {

        Set<String> ids = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        try {
            Class<?> type = Class.forName(className);

            for (Class<?> current = type;
                 current != null;
                 current = current.getSuperclass()) {

                for (Field field : current.getDeclaredFields()) {
                    if (!Modifier.isStatic(field.getModifiers())
                            || !Map.class.isAssignableFrom(field.getType())) {
                        continue;
                    }

                    try {
                        field.setAccessible(true);
                        Object value = field.get(null);

                        if (value instanceof Map<?, ?> map) {
                            for (Object key : map.keySet()) {
                                if (key == null) {
                                    continue;
                                }

                                String id = key.toString();
                                if (id.matches("[a-zA-Z0-9_.:-]+")) {
                                    ids.add(id);
                                }
                            }
                        }
                    } catch (ReflectiveOperationException | RuntimeException ignored) {
                        // Um campo inacessível não deve impedir as outras sugestões.
                    }
                }
            }
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            // Falhas no carregamento do registro resultam em nenhuma sugestão,
            // sem derrubar o comando ou o jogo.
        }

        return SharedSuggestionProvider.suggest(ids, builder);
    }
}
