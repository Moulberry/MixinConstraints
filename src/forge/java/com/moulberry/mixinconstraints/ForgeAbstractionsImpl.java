package com.moulberry.mixinconstraints;

import com.moulberry.mixinconstraints.util.Abstractions;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.Restriction;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.List;

public class ForgeAbstractionsImpl extends Abstractions {
	@Override
	protected boolean isDevEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	protected String getModVersion(String modid) {
		List<IModInfo> infoList = ModList.get().getModFileById(modid).getMods();
		if(infoList.isEmpty()) {
			return null;
		} else if(infoList.size() > 1) {
			MixinConstraints.LOGGER.warn("Multiple mods with id {} found, using first one", modid);
		}

		return infoList.getFirst().getVersion().toString();
	}

	@Override
	protected boolean isVersionInRange(String version, String minVersion, String maxVersion) {
		ArtifactVersion currentVersion = new DefaultArtifactVersion(version);
		ArtifactVersion min = minVersion == null ? null : new DefaultArtifactVersion(minVersion);
		ArtifactVersion max = maxVersion == null ? null : new DefaultArtifactVersion(maxVersion);

		if(min != null && max != null && min.compareTo(max) > 0) {
			throw new IllegalArgumentException("minVersion (" + minVersion + ") is greater than maxVersion (" + maxVersion + ")");
		}

		return new Restriction(min, true, max, true).containsVersion(currentVersion);
	}
}
