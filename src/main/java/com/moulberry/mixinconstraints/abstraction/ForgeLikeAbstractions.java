package com.moulberry.mixinconstraints.abstraction;

import com.moulberry.mixinconstraints.MixinConstraints;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class ForgeLikeAbstractions {
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    public static boolean checkDevEnvironment() {
        try {
            // Get the classes
            Class<?> FMLLoaderClass;
            if (MixinConstraints.LOADER == MixinConstraints.Loader.FORGE) {
                FMLLoaderClass = Class.forName("net.minecraftforge.fml.loading.FMLLoader");
            } else if (MixinConstraints.LOADER == MixinConstraints.Loader.NEOFORGE) {
                FMLLoaderClass = Class.forName("net.neoforged.fml.loading.FMLLoader");
            } else {
                throw new RuntimeException("Forge method called when loader is Fabric");
            }

            // Get the method handles
            MethodHandle isProduction = lookup.findVirtual(FMLLoaderClass, "isProduction", MethodType.methodType(boolean.class));

            return !(boolean) isProduction.invoke();
        } catch (Throwable e) {
            MixinConstraints.LOGGER.error(e.toString());
            return false;
        }
    }
}
