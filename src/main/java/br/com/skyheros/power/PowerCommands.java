package br.com.skyheros.power;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;

public final class PowerCommands {
    private PowerCommands() {}

    private static final java.util.Map<java.util.UUID, java.util.Map<String, Long>> COOLDOWNS = new java.util.concurrent.ConcurrentHashMap<>();

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("skyheros")
                .then(Commands.literal("power")
                    .then(Commands.argument("id", StringArgumentType.greedyString()).suggests(br.com.skyheros.command.CatalogSuggestions::powers)
                        .executes(context -> {
                            if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
                                context.getSource().sendFailure(
                                    Component.literal("Este comando precisa ser usado por um jogador."));
                                return 0;
                            }

                            String requested = StringArgumentType.getString(context, "id")
                                    .trim().toLowerCase();

                            PowerDefinition selected = null;
                            for (PowerDefinition definition : PowerRegistry.getAll()) {
                                String id = definition.getId().toLowerCase();
                                String shortId = id.startsWith("skyheros:")
                                        ? id.substring("skyheros:".length()) : id;

                                if (id.equals(requested) || shortId.equals(requested)
                                        || definition.getName().equalsIgnoreCase(requested)) {
                                    selected = definition;
                                    break;
                                }
                            }

                            if (selected == null) {
                                context.getSource().sendFailure(Component.literal(
                                    "Poder nao encontrado: " + requested
                                    + ". Use o ID curto, como comic_fireball."));
                                return 0;
                            }

                            long now = player.level().getGameTime();
                            java.util.Map<String, Long> playerCooldowns =
                                    COOLDOWNS.computeIfAbsent(player.getUUID(),
                                        ignored -> new java.util.concurrent.ConcurrentHashMap<>());
                            long readyAt = playerCooldowns.getOrDefault(selected.getId(), 0L);

                            if (now < readyAt) {
                                long secondsLeft = (readyAt - now + 19L) / 20L;
                                context.getSource().sendFailure(Component.literal(
                                    "Este poder esta em recarga. Aguarde " + secondsLeft + " s."));
                                return 0;
                            }

                            String energyError = PowerProgressionRuntime.checkEnergy(player, selected);
                            if (energyError != null) {
                                context.getSource().sendFailure(Component.literal(energyError));
                                return 0;
                            }
String result = PowerRuntime.execute(player, selected);
                            String normalizedResult = java.text.Normalizer.normalize(
                                    result == null ? "" : result,
                                    java.text.Normalizer.Form.NFD)
                                    .replaceAll("\\p{M}", "").toLowerCase(java.util.Locale.ROOT);
                            boolean failed = normalizedResult.startsWith("este poder esta cadastrado")
                                    || normalizedResult.startsWith("nenhum alvo")
                                    || normalizedResult.startsWith("o alvo nao recebeu dano")
                                    || normalizedResult.contains("ainda nao tem uma implementacao")
                                    || normalizedResult.contains("ainda nao tem um efeito")
                                    || normalizedResult.startsWith("nao foi possivel");

                            if (!failed && selected.getCooldownTicks() > 0) {
                                playerCooldowns.put(selected.getId(),
                                        now + selected.getCooldownTicks());
                            }
                            String progressMessage = "";
                            if (!failed) {
                                progressMessage = PowerProgressionRuntime.recordSuccessfulUse(player, selected);
                            }
                            String displayResult = progressMessage.isEmpty() ? result : result + " | " + progressMessage;

                            PowerDefinition chosen = selected;
                            context.getSource().sendSuccess(
                                () -> Component.literal("[" + chosen.getName() + "] " + displayResult),
                                false
                            );
                            return 1;
                        })
                    )
                )
        );
        event.getDispatcher().register(
            Commands.literal("skyheros")
                .then(Commands.literal("flight")
                    .then(Commands.literal("on").executes(context -> {
                        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
                            context.getSource().sendFailure(
                                    Component.literal("Este comando deve ser usado por um jogador."));
                            return 0;
                        }

                        br.com.skyheros.flight.FlightController.enable(player, "manual");
                        player.sendSystemMessage(Component.literal(
                                "Voo ativado. WASD controla a direcao; Espaco sobe e Shift desce."));
                        return 1;
                    }))
                    .then(Commands.literal("off").executes(context -> {
                        if (!(context.getSource().getEntity() instanceof ServerPlayer player)) {
                            context.getSource().sendFailure(
                                    Component.literal("Este comando deve ser usado por um jogador."));
                            return 0;
                        }

                        boolean disabled =
                                br.com.skyheros.flight.FlightController.disable(player);

                        if (!disabled) {
                            context.getSource().sendFailure(
                                    Component.literal("O voo do Skyheros nao esta ativo."));
                            return 0;
                        }

                        player.sendSystemMessage(Component.literal(
                                "Voo do Skyheros desativado; suas permissoes anteriores foram restauradas."));
                        return 1;
                    }))
                )
        );
    }
}
