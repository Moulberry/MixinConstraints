package com.moulberry.mixinconstraints.checker;

import com.moulberry.mixinconstraints.MixinConstraints;
import com.moulberry.mixinconstraints.abstraction.FabricAbstractions;
import com.moulberry.mixinconstraints.abstraction.ForgeLikeAbstractions;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

import java.util.Optional;

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
        return switch(MixinConstraints.LOADER) {
            case FABRIC -> FabricAbstractions.checkDevEnvironment();
            case FORGE, NEOFORGE -> ForgeLikeAbstractions.checkDevEnvironment();
            case UNKNOWN -> false;
        };
    }

    public static boolean checkMinecraftVersion(String minVersion, String maxVersion) {
        return isModLoadedWithinVersion("minecraft", minVersion, maxVersion);
    }

    private static boolean isModLoadedWithinVersion(String modId, String minVersion, String maxVersion) {
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
        if (modContainer.isEmpty()) {
            return false;
        }

        return isVersionInRange(modContainer.get().getMetadata().getVersion(), minVersion, maxVersion);
    }

    private static boolean isVersionInRange(Version version, String minVersion, String maxVersion) {
        Version min = minVersion == null || minVersion.isEmpty() ? null : tryParseVersion(minVersion);
        Version max = maxVersion == null || maxVersion.isEmpty() ? null : tryParseVersion(maxVersion);

        // Ensure range is valid (min <= max)
        if (min != null && max != null && min.compareTo(max) > 0) {
            throw new IllegalArgumentException("invalid range: minVersion (" + minVersion + ") > maxVersion (" + maxVersion + ")");
        }

        // Check version >= min
        if (min != null && version.compareTo(min) < 0) {
            return false;
        }

        // Check version <= max
        if (max != null && version.compareTo(max) > 0) {
            return false;
        }

        return true;
    }

    private static Version tryParseVersion(String version) {
        try {
            return Version.parse(version);
        } catch (VersionParsingException e) {
            System.err.println("Invalid version string: " + version + "...");
            throw new RuntimeException(e);
        }
    }

}
