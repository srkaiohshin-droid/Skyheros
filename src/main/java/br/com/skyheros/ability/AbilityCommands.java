package br.com.skyheros.ability;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;

public final class AbilityCommands {
    private AbilityCommands() {}

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("skyheros")
                .then(Commands.literal("ability")
                    .then(Commands.argument("id", StringArgumentType.word()).suggests(br.com.skyheros.command.CatalogSuggestions::abilities)
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
                                context.getSource().sendFailure(Component.literal(
                                    "Este comando deve ser usado por um jogador."
                                ));
                                return 0;
                            }
                            return AbilityRuntime.activate(
                                player,
                                StringArgumentType.getString(context, "id")
                            );
                        })
                    )
                )
                .then(Commands.literal("list")
                    .executes(context -> {
                        String ids = AbilityRegistry.getAll().stream()
                            .map(AbilityDefinition::id)
                            .sorted()
                            .limit(40)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("nenhuma");

                        context.getSource().sendSuccess(() -> Component.literal(
                            "Catalogo: " + AbilityAudit.totalCount()
                                + " habilidades. Primeiras 40 por ID: " + ids
                        ), false);
                        return 1;
                    })
                )
                .then(Commands.literal("catalog")
                    .executes(context -> {
                        context.getSource().sendSuccess(() -> Component.literal(
                            "Skyheros | Total: " + AbilityAudit.totalCount()
                                + " | Com executor: " + AbilityAudit.implementedCount()
                                + " | Aguardando executor: " + AbilityAudit.pendingCount()
                        ), false);
                        return 1;
                    })
                )
                .then(Commands.literal("validate")
                    .executes(context -> {
                        int total = AbilityAudit.totalCount();
                        long implemented = AbilityAudit.implementedCount();
                        long pending = AbilityAudit.pendingCount();
                        boolean correct = total == 1000 && implemented + pending == total;

                        context.getSource().sendSuccess(() -> Component.literal(
                            (correct ? "[OK] " : "[ATENCAO] ")
                                + "Catalogo: " + total + "/1000"
                                + " | Executores implementados: " + implemented
                                + " | Pendentes: " + pending
                        ), false);
                        return correct ? 1 : 0;
                    })
                )
        );
    }
}
