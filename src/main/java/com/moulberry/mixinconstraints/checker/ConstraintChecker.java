package com.moulberry.mixinconstraints.checker;

import com.moulberry.mixinconstraints.util.Abstractions;

public class ConstraintChecker {
    /**
     * Check if *ANY* of the modIds provided are loaded
     */
    public static boolean checkModLoaded(String main, Iterable<String> aliases, String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive) {
        if (isModLoadedWithinVersion(main, minVersion, maxVersion, minInclusive, maxInclusive)) {
            return true;
        }

        for (String modId : aliases) {
            if (isModLoadedWithinVersion(modId, minVersion, maxVersion, minInclusive, maxInclusive)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if *ALL* the modIds provided are absent
     */
    public static boolean checkModAbsent(String main, Iterable<String> modIds, String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive) {
        return !checkModLoaded(main, modIds, minVersion, maxVersion, minInclusive, maxInclusive);
    }

    public static boolean checkDevEnvironment() {
        return Abstractions.isDevelopmentEnvironment();
    }

    public static boolean checkMinecraftVersion(String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive) {
        return isModLoadedWithinVersion("minecraft", minVersion, maxVersion, minInclusive, maxInclusive);
    }

    private static boolean isModLoadedWithinVersion(String modId, String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive) {
        return Abstractions.isModLoadedWithinVersion(modId, minVersion, maxVersion, minInclusive, maxInclusive);
    }

}
