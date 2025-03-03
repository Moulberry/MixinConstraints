package com.moulberry.mixinconstraints.util;

import com.moulberry.mixinconstraints.MixinConstraints;

public abstract class Abstractions {
	private static Abstractions instance = null;

    private static boolean doesClassExist(String className) {
        try {
            Class.forName(className, false, MixinConstraints.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public static Abstractions getInstance() {
        if (instance == null) {
            String name = System.getProperty("mixinconstraints.abstraction");
            if (name == null || name.isEmpty()) {
                if (doesClassExist("net.neoforged.fml.loading.FMLLoader")) {
                    name = "com.moulberry.mixinconstraints.NeoForgeAbstractionsImpl";
                } else if (doesClassExist("net.minecraftforge.fml.loading.FMLLoader")) {
                    name = "com.moulberry.mixinconstraints.ForgeAbstractionsImpl";
                } else if (doesClassExist("net.fabricmc.loader.api.FabricLoader")) {
                    name = "com.moulberry.mixinconstraints.FabricAbstractionsImpl";
                } else {
                    throw new RuntimeException("Could not determine loader");
                }
            }
            try {
                instance = Class.forName(name).asSubclass(Abstractions.class).getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to load " + name, e);
            }
        }
        return instance;
    }

	public boolean isModLoadedWithinVersion(String modId, String minVersion, String maxVersion) {
		String version = this.getModVersion(modId);
		if (version == null) {
			return false;
		}

		return this.isVersionInRange(version, minVersion, maxVersion);
	}

    public abstract boolean isDevEnvironment();
    public abstract String getModVersion(String modId);
    public abstract boolean isVersionInRange(String version, String minVersion, String maxVersion);
    public abstract String getPlatformName();
}
