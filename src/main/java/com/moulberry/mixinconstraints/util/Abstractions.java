package com.moulberry.mixinconstraints.util;

import com.moulberry.mixinconstraints.MixinConstraints;

public abstract class Abstractions {
	private static Abstractions instance = null;

    private static Abstractions getInstance() {
        if (instance == null) {
            try {
                String name = switch (MixinConstraints.getLoader()) {
                    case FORGE -> "com.moulberry.mixinconstraints.ForgeAbstractionsImpl";
                    case NEOFORGE -> "com.moulberry.mixinconstraints.NeoForgeAbstractionsImpl";
                    case FABRIC -> "com.moulberry.mixinconstraints.FabricAbstractionsImpl";
                };
                instance = (Abstractions) Class.forName(name).getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

	public static boolean isDevelopmentEnvironment() {
        Abstractions instance = getInstance();
		return instance.isDevEnvironment();
	}

	public static boolean isModLoadedWithinVersion(String modId, String minVersion, String maxVersion) {
        Abstractions instance = getInstance();

		String version = instance.getModVersion(modId);
		if (version == null) {
			return false;
		}

		return instance.isVersionInRange(version, minVersion, maxVersion);
	}

	protected abstract boolean isDevEnvironment();
	protected abstract String getModVersion(String modId);
	protected abstract boolean isVersionInRange(String version, String minVersion, String maxVersion);
}
