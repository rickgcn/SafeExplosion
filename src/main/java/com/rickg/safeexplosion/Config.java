package com.rickg.safeexplosion;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public final class Config {

    public static boolean disableBlockDamage = true;
    public static boolean logProtectedExplosions = false;
    public static boolean logExplosionTypeDecisions = false;
    public static String[] protectedExplosionTypes = new String[] { "*" };

    private static final String CATEGORY_GENERAL = Configuration.CATEGORY_GENERAL;
    private static final List<String> GENERAL_PROPERTY_ORDER = Arrays
        .asList("disableBlockDamage", "protectedExplosionTypes", "logProtectedExplosions", "logExplosionTypeDecisions");
    private static final Set<String> NORMALIZED_PROTECTED_EXPLOSION_TYPES = new LinkedHashSet<String>();
    private static Configuration configuration;

    private Config() {}

    public static void load(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
        }

        synchronizeConfiguration(true);
    }

    public static void synchronizeConfiguration() {
        synchronizeConfiguration(false);
    }

    public static void reloadFromDisk() {
        synchronizeConfiguration(true);
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    private static void synchronizeConfiguration(boolean loadFromDisk) {
        if (configuration == null) {
            throw new IllegalStateException("Configuration accessed before initialization.");
        }

        if (loadFromDisk) {
            configuration.load();
        }

        configuration
            .setCategoryComment(CATEGORY_GENERAL, "Controls which explosion types are allowed to damage blocks.");
        configuration.setCategoryPropertyOrder(CATEGORY_GENERAL, GENERAL_PROPERTY_ORDER);

        Property disableBlockDamageProperty = configuration.get(
            CATEGORY_GENERAL,
            "disableBlockDamage",
            disableBlockDamage,
            "If true, the whitelist below decides which explosions cannot destroy blocks.");
        disableBlockDamageProperty.setLanguageKey("safeexplosion.config.disableBlockDamage");
        disableBlockDamage = disableBlockDamageProperty.getBoolean(disableBlockDamage);

        Property protectedExplosionTypesProperty = configuration.get(
            CATEGORY_GENERAL,
            "protectedExplosionTypes",
            protectedExplosionTypes,
            "Whitelist of explosion identifiers that should not break blocks. Supports '*' for all explosions, "
                + "aliases like 'minecraft:tnt', 'tnt', 'minecraft:creeper', 'creeper', "
                + "'minecraft:wither_skull', 'minecraft:fireball', 'minecraft:ender_crystal', "
                + "or exact identifiers such as 'source:net.minecraft.entity.item.entitytntprimed', "
                + "'placer:net.minecraft.entity.player.entityplayer', and "
                + "'explosion:net.minecraft.world.explosion'. Matching is case-insensitive.");
        protectedExplosionTypesProperty.setLanguageKey("safeexplosion.config.protectedExplosionTypes");
        protectedExplosionTypes = protectedExplosionTypesProperty.getStringList();

        Property logProtectedExplosionsProperty = configuration.get(
            CATEGORY_GENERAL,
            "logProtectedExplosions",
            logProtectedExplosions,
            "If true, log each explosion whose affected block list gets cleared.");
        logProtectedExplosionsProperty.setLanguageKey("safeexplosion.config.logProtectedExplosions");
        logProtectedExplosions = logProtectedExplosionsProperty.getBoolean(logProtectedExplosions);

        Property logExplosionTypeDecisionsProperty = configuration.get(
            CATEGORY_GENERAL,
            "logExplosionTypeDecisions",
            logExplosionTypeDecisions,
            "If true, log the identifiers detected for each explosion and whether the whitelist matched.");
        logExplosionTypeDecisionsProperty.setLanguageKey("safeexplosion.config.logExplosionTypeDecisions");
        logExplosionTypeDecisions = logExplosionTypeDecisionsProperty.getBoolean(logExplosionTypeDecisions);

        rebuildProtectedExplosionTypeCache();

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static boolean matchesProtectedExplosionType(ExplosionMatchContext matchContext) {
        if (!disableBlockDamage) {
            return false;
        }

        for (String identifier : matchContext.getIdentifiers()) {
            if (NORMALIZED_PROTECTED_EXPLOSION_TYPES.contains(identifier)) {
                return true;
            }
        }

        return false;
    }

    private static void rebuildProtectedExplosionTypeCache() {
        NORMALIZED_PROTECTED_EXPLOSION_TYPES.clear();

        for (String protectedExplosionType : protectedExplosionTypes) {
            String normalizedIdentifier = normalizeIdentifier(protectedExplosionType);
            if (!normalizedIdentifier.isEmpty()) {
                NORMALIZED_PROTECTED_EXPLOSION_TYPES.add(normalizedIdentifier);
            }
        }
    }

    public static String normalizeIdentifier(String identifier) {
        return identifier == null ? ""
            : identifier.trim()
                .toLowerCase(Locale.ROOT);
    }

    public static String getProtectedExplosionTypesSummary() {
        return Arrays.toString(protectedExplosionTypes);
    }
}
