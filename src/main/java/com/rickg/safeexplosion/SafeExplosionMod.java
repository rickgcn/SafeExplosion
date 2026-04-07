package com.rickg.safeexplosion;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = SafeExplosionMod.MOD_ID,
    version = Tags.VERSION,
    name = SafeExplosionMod.MOD_NAME,
    acceptedMinecraftVersions = "[1.7.10]",
    acceptableRemoteVersions = "*",
    guiFactory = "com.rickg.safeexplosion.client.gui.SafeExplosionGuiFactory")
public final class SafeExplosionMod {

    public static final String MOD_ID = "safeexplosion";
    public static final String MOD_NAME = "SafeExplosion";
    public static final Logger LOG = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ExplosionProtectionHandler());
        if (FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            FMLCommonHandler.instance()
                .bus()
                .register(new com.rickg.safeexplosion.client.gui.ConfigGuiEventHandler());
        }
        LOG.info(
            "{} {} loaded. Explosion block damage is {}. Whitelist={}.",
            MOD_NAME,
            Tags.VERSION,
            Config.disableBlockDamage ? "configurable" : "enabled",
            Config.getProtectedExplosionTypesSummary());
    }
}
