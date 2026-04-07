package com.rickg.safeexplosion;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ExplosionMatchContext {

    private final LinkedHashSet<String> identifiers = new LinkedHashSet<String>();

    public void addIdentifier(String identifier) {
        String normalizedIdentifier = Config.normalizeIdentifier(identifier);
        if (!normalizedIdentifier.isEmpty()) {
            identifiers.add(normalizedIdentifier);
        }
    }

    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(identifiers);
    }

    public String describeIdentifiers() {
        return identifiers.toString();
    }
}
