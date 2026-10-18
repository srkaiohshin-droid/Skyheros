package br.com.skyheros;

import br.com.skyheros.power.PowerCatalog;
import br.com.skyheros.power.PowerRegistry;
import br.com.skyheros.suit.SuitCatalog;
import br.com.skyheros.suit.SuitRegistry;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(SkyherosMod.MOD_ID)
public class SkyherosMod {
    public static final String MOD_ID = "skyheros";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SkyherosMod() {
        br.com.skyheros.entity.FlightRigRegistry.register(
                net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus()
        );
        br.com.skyheros.network.SkyherosNetwork.register();
        SuitCatalog.registerDefaults();
        PowerCatalog.registerDefaults();
        br.com.skyheros.power.ComicPowerCatalog.registerDefaults();
        br.com.skyheros.power.SpecialPowerCatalog.registerDefaults();
        br.com.skyheros.power.ComboPowerCatalog.registerDefaults();
    br.com.skyheros.power.AdvancedPowerCatalog.registerDefaults();
        br.com.skyheros.ability.AbilityCatalog.registerDefaults();
        br.com.skyheros.hero.HeroCatalog.registerDefaults();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(br.com.skyheros.ability.AbilityCommands::register);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(br.com.skyheros.power.PowerCommands::register);

        LOGGER.info("Skyheros iniciado: {} trajes e {} poderes registrados.",
                SuitRegistry.size(), PowerRegistry.size());

        if (PowerRegistry.size() != 900) {
        throw new IllegalStateException(
                "Esperados 900 poderes, encontrados: " + PowerRegistry.size());
    }
    }
}

