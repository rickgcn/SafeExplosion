package com.rickg.safeexplosion.client.gui;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import com.rickg.safeexplosion.Config;
import com.rickg.safeexplosion.SafeExplosionMod;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class SafeExplosionConfigGui extends GuiConfig {

    public SafeExplosionConfigGui(GuiScreen parentScreen) {
        super(
            parentScreen,
            getConfigElements(),
            SafeExplosionMod.MOD_ID,
            false,
            false,
            SafeExplosionMod.MOD_NAME + " Config");
    }

    private static List<IConfigElement> getConfigElements() {
        return new ConfigElement(
            Config.getConfiguration()
                .getCategory(Configuration.CATEGORY_GENERAL)).getChildElements();
    }
}
