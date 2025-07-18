package com.moulberry.mixinconstraints;

import com.moulberry.mixinconstraints.util.Abstractions;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

public class FabricAbstractionsImpl extends Abstractions {
    @Override
    protected boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    protected String getModVersion(String modId) {
        return FabricLoader.getInstance().getModContainer(modId)
                .map(container -> container.getMetadata()
                        .getVersion()
                        .getFriendlyString())
                .orElse(null);
    }

    @Override
    protected boolean isVersionInRange(String version, String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive) {
        try {
            Version currentVersion = Version.parse(version);
            Version min = minVersion == null ? null : Version.parse(minVersion);
            Version max = maxVersion == null ? null : Version.parse(maxVersion);

            if (min != null && max != null && min.compareTo(max) > 0) {
                throw new IllegalArgumentException("minVersion (" + minVersion + ") is greater than maxVersion (" + maxVersion + ")");
            }

            if (min != null) {
                if (minInclusive) {
                    if (currentVersion.compareTo(min) < 0) {
                        return false;
                    }
                } else {
                    if (currentVersion.compareTo(min) <= 0) {
                        return false;
                    }
                }
            }

            if (max != null) {
                if (maxInclusive) {
                    if (currentVersion.compareTo(max) > 0) {
                        return false;
                    }
                } else {
                    if (currentVersion.compareTo(max) >= 0) {
                        return false;
                    }
                }
            }

            return true;
        } catch (VersionParsingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getPlatformName() {
        return "Fabric";
    }
}
