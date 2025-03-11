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

    private static Abstractions getInstance() {
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

    public static String getLoaderName() {
        Abstractions instance = getInstance();
        return instance.getPlatformName();
    }

    protected abstract boolean isDevEnvironment();
    protected abstract String getModVersion(String modId);
    protected abstract boolean isVersionInRange(String version, String minVersion, String maxVersion);
    protected abstract String getPlatformName();
}
