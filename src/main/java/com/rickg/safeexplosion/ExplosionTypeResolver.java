package com.rickg.safeexplosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.world.Explosion;

public final class ExplosionTypeResolver {

    private ExplosionTypeResolver() {}

    public static ExplosionMatchContext resolve(Explosion explosion) {
        ExplosionMatchContext matchContext = new ExplosionMatchContext();

        matchContext.addIdentifier("*");
        addClassIdentifiers(matchContext, "explosion", explosion.getClass());

        Entity exploder = explosion.exploder;
        if (exploder == null) {
            matchContext.addIdentifier("source:none");
            matchContext.addIdentifier("minecraft:unknown");
            matchContext.addIdentifier("unknown");
        } else {
            addEntityIdentifiers(matchContext, "source", exploder);
        }

        EntityLivingBase placer = explosion.getExplosivePlacedBy();
        if (placer == null) {
            matchContext.addIdentifier("placer:none");
        } else {
            addEntityIdentifiers(matchContext, "placer", placer);
        }

        return matchContext;
    }

    private static void addEntityIdentifiers(ExplosionMatchContext matchContext, String role, Entity entity) {
        addClassIdentifiers(matchContext, role, entity.getClass());
        addExplosionAliases(matchContext, entity);
    }

    private static void addClassIdentifiers(ExplosionMatchContext matchContext, String prefix, Class<?> type) {
        matchContext.addIdentifier(prefix + ":" + type.getName());
        matchContext.addIdentifier(prefix + ":" + type.getSimpleName());
    }

    private static void addExplosionAliases(ExplosionMatchContext matchContext, Entity entity) {
        if (entity instanceof EntityMinecartTNT) {
            addAlias(matchContext, "minecraft:tnt_minecart");
        }

        if (entity instanceof EntityTNTPrimed) {
            addAlias(matchContext, "minecraft:tnt");
        }

        if (entity instanceof EntityCreeper) {
            addAlias(matchContext, "minecraft:creeper");
        }

        if (entity instanceof EntityWitherSkull) {
            addAlias(matchContext, "minecraft:wither_skull");
        }

        if (entity instanceof EntityFireball) {
            addAlias(matchContext, "minecraft:fireball");
        }

        if (entity instanceof EntityEnderCrystal) {
            addAlias(matchContext, "minecraft:ender_crystal");
        }

        if (entity instanceof EntityDragonPart) {
            addAlias(matchContext, "minecraft:dragon");
        }
    }

    private static void addAlias(ExplosionMatchContext matchContext, String alias) {
        matchContext.addIdentifier(alias);

        int namespaceSeparator = alias.indexOf(':');
        if (namespaceSeparator >= 0 && namespaceSeparator < alias.length() - 1) {
            matchContext.addIdentifier(alias.substring(namespaceSeparator + 1));
        }
    }
}
