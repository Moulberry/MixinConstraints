package com.moulberry.mixinconstraints.checker;

import com.moulberry.mixinconstraints.util.Abstractions;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ConstraintChecker {
    /**
     * Check if *ANY* of the modIds provided are loaded
     */
    public static boolean checkModLoaded(String main, Iterable<String> aliases, String minVersion, String maxVersion) {
        if (isModLoadedWithinVersion(main, minVersion, maxVersion)) {
            return true;
        }

        for (String modId : aliases) {
            if (isModLoadedWithinVersion(modId, minVersion, maxVersion)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if *ALL* the modIds provided are absent
     */
    public static boolean checkModAbsent(String main, Iterable<String> modIds, String minVersion, String maxVersion) {
        return !checkModLoaded(main, modIds, minVersion, maxVersion);
    }

    public static boolean checkDevEnvironment() {
        return Abstractions.isDevelopmentEnvironment();
    }

    public static boolean checkMinecraftVersion(String minVersion, String maxVersion) {
        return isModLoadedWithinVersion("minecraft", minVersion, maxVersion);
    }

    private static boolean isModLoadedWithinVersion(String modId, String minVersion, String maxVersion) {
        return Abstractions.isModLoadedWithinVersion(modId, minVersion, maxVersion);
    }

}
