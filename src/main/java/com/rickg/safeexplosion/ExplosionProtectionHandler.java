package com.rickg.safeexplosion;

import net.minecraftforge.event.world.ExplosionEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class ExplosionProtectionHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (event.world.isRemote || event.getAffectedBlocks()
            .isEmpty()) {
            return;
        }

        ExplosionMatchContext matchContext = ExplosionTypeResolver.resolve(event.explosion);
        boolean shouldProtectExplosion = Config.matchesProtectedExplosionType(matchContext);

        if (Config.logExplosionTypeDecisions) {
            SafeExplosionMod.LOG.info(
                "Explosion identifiers {} matched whitelist {}.",
                matchContext.describeIdentifiers(),
                shouldProtectExplosion);
        }

        if (!shouldProtectExplosion) {
            return;
        }

        int removedBlockCount = event.getAffectedBlocks()
            .size();
        event.getAffectedBlocks()
            .clear();

        if (Config.logProtectedExplosions) {
            SafeExplosionMod.LOG.info(
                "Protected {} blocks from an explosion in dimension {} with identifiers {}.",
                removedBlockCount,
                event.world.provider.dimensionId,
                matchContext.describeIdentifiers());
        }
    }
}
