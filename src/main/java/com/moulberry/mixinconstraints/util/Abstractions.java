package com.moulberry.mixinconstraints.util;

import com.moulberry.mixinconstraints.MixinConstraints;

import java.util.ArrayList;
import java.util.List;

public abstract class Abstractions {
    private static List<Abstractions> abstractions = null;

    private static boolean doesClassExist(String className) {
        try {
            Class.forName(className, false, MixinConstraints.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static List<Abstractions> getAbstractions() {
        if (abstractions == null) {
            abstractions = new ArrayList<>();

            String name = System.getProperty("mixinconstraints.abstraction");
            if (name != null && !name.isBlank()) {
                Abstractions instance = tryLoadAbstractionsFromClassname(name);
                if (instance != null) {
                    abstractions.add(instance);
                }
            }

            if (doesClassExist("net.neoforged.fml.loading.FMLLoader")) {
                Abstractions instance = tryLoadAbstractionsFromClassname("com.moulberry.mixinconstraints.NeoForgeAbstractionsImpl");
                if (instance != null) {
                    abstractions.add(instance);
                }
            }
            if (doesClassExist("net.minecraftforge.fml.loading.FMLLoader")) {
                Abstractions instance = tryLoadAbstractionsFromClassname("com.moulberry.mixinconstraints.ForgeAbstractionsImpl");
                if (instance != null) {
                    abstractions.add(instance);
                }
            }
            if (doesClassExist("net.fabricmc.loader.api.FabricLoader")) {
                Abstractions instance = tryLoadAbstractionsFromClassname("com.moulberry.mixinconstraints.FabricAbstractionsImpl");
                if (instance != null) {
                    abstractions.add(instance);
                }
            }

            if (abstractions.isEmpty()) {
                throw new RuntimeException("Could not determine loader");
            }
        }
        return abstractions;
    }

    private static Abstractions tryLoadAbstractionsFromClassname(String name) {
        try {
            return Class.forName(name).asSubclass(Abstractions.class).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            MixinConstraints.LOGGER.error("Failed to load {}", name, e);
            return null;
        }
    }

    public static boolean isDevelopmentEnvironment() {
        for (Abstractions instance : getAbstractions()) {
            if (instance.isDevEnvironment()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isModLoadedWithinVersion(String modId, String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive) {
        for (Abstractions instance : getAbstractions()) {
            String version = instance.getModVersion(modId);
            if (version == null) {
                continue;
            }
            if (instance.isVersionInRange(version, minVersion, maxVersion, minInclusive, maxInclusive)) {
                return true;
            }
        }
        return false;
    }

    public static String getLoaderName() {
        for (Abstractions instance : getAbstractions()) {
            return instance.getPlatformName();
        }
        throw new RuntimeException("Could not determine loader");
    }

    protected abstract boolean isDevEnvironment();
    protected abstract String getModVersion(String modId);
    protected abstract boolean isVersionInRange(String version, String minVersion, String maxVersion, boolean minInclusive, boolean maxInclusive);
    protected abstract String getPlatformName();
}
