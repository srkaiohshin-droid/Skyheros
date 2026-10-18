package br.com.skyheros.flight;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = "skyheros", bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FlightController {
    private static final String KEY = "skyheros_flight_controller";

    private FlightController() {}

    private static CompoundTag state(ServerPlayer player) {
        CompoundTag root = player.getPersistentData();
        CompoundTag data = root.getCompound(KEY);
        root.put(KEY, data);
        return data;
    }

    public static boolean isActive(ServerPlayer player) {
        return state(player).getBoolean("active");
    }

    public static boolean enable(ServerPlayer player, String theme) {
        CompoundTag data = state(player);
        var abilities = player.getAbilities();

        if (!data.getBoolean("active")) {
            data.putBoolean("original_mayfly", abilities.mayfly);
            data.putBoolean("original_flying", abilities.flying);
            data.putFloat("original_flying_speed", abilities.getFlyingSpeed());
        }

        String normalizedTheme = theme == null
                ? "energy"
                : theme.toLowerCase(Locale.ROOT);

        data.putString("theme", normalizedTheme);
        data.putBoolean("active", true);

        applyFlight(player, data);
        return true;
    }

    public static boolean disable(ServerPlayer player) {
        CompoundTag data = state(player);

        if (!data.getBoolean("active")) {
            return false;
        }

        var abilities = player.getAbilities();

        abilities.mayfly = data.getBoolean("original_mayfly");
        abilities.flying = abilities.mayfly
                && data.getBoolean("original_flying");
        abilities.setFlyingSpeed(data.contains("original_flying_speed") ? data.getFloat("original_flying_speed") : 0.05F);

        data.putBoolean("active", false);
        player.fallDistance = 0.0F;
        player.onUpdateAbilities();

        return true;
    }

    private static void applyFlight(ServerPlayer player, CompoundTag data) {
        var abilities = player.getAbilities();

        float speed = flightSpeed(data.getString("theme"));
        boolean changed = !abilities.mayfly
                || !abilities.flying
                || Math.abs(abilities.getFlyingSpeed() - speed) > 0.0001F;

        abilities.mayfly = true;
        abilities.flying = true;
        abilities.setFlyingSpeed(speed);

        if (changed) {
            player.onUpdateAbilities();
        }
    }

    private static float flightSpeed(String theme) {
        if (hasAny(theme, "wind", "storm", "sonic", "kinetic", "speed")) {
            return 0.105F;
        }

        if (hasAny(theme, "cosmic", "space", "quantum", "dimension", "gravity")) {
            return 0.090F;
        }

        if (hasAny(theme, "technology", "tech", "magnetic", "metal")) {
            return 0.075F;
        }

        if (hasAny(theme, "solar", "radiant", "light")) {
            return 0.085F;
        }

        return 0.070F;
    }

    private static boolean hasAny(String value, String... terms) {
        String text = value == null ? "" : value.toLowerCase(Locale.ROOT);

        for (String term : terms) {
            if (text.contains(term)) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END
                || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        CompoundTag data = state(player);

        if (data.getBoolean("active")) {
            applyFlight(player, data);
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player
                && isActive(player)) {
            disable(player);
        }
    }
}