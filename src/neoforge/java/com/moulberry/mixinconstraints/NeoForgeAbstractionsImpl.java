package com.moulberry.mixinconstraints;

import com.moulberry.mixinconstraints.util.Abstractions;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.Restriction;

public class NeoForgeAbstractionsImpl extends Abstractions {
    @Override
    protected boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    protected String getModVersion(String modid) {
        IModFileInfo info = LoadingModList.get().getModFileById(modid);
        return info == null || info.getMods().isEmpty() ? null : info.versionString();
    }

    @Override
    protected boolean isVersionInRange(String version, String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive) {
        ArtifactVersion currentVersion = new DefaultArtifactVersion(version);
        ArtifactVersion min = minVersion == null ? null : new DefaultArtifactVersion(minVersion);
        ArtifactVersion max = maxVersion == null ? null : new DefaultArtifactVersion(maxVersion);

        if(min != null && max != null && min.compareTo(max) > 0) {
            throw new IllegalArgumentException("minVersion (" + minVersion + ") is greater than maxVersion (" + maxVersion + ")");
        }

        return new Restriction(min, minInclusive, max, maxInclusive).containsVersion(currentVersion);
    }

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }
}
