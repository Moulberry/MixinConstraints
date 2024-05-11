package com.moulberry.mixinconstraints.abstraction;

import net.fabricmc.loader.api.FabricLoader;

import java.lang.invoke.MethodHandles;

public class FabricAbstractions {
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    public static boolean checkDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
