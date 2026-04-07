package com.rickg.safeexplosion.client.gui;

import com.rickg.safeexplosion.Config;
import com.rickg.safeexplosion.SafeExplosionMod;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ConfigGuiEventHandler {

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if (SafeExplosionMod.MOD_ID.equals(event.modID)) {
            Config.synchronizeConfiguration();
        }
    }
}
