package com.moulberry.mixinconstraints.mixin;

import com.llamalad7.mixinextras.utils.MixinInternals;

import java.util.HashSet;
import java.util.Set;

public class MixinConstraintsBootstrap {

    private static final Set<String> initializedMixinPackages = new HashSet<>();

    public static void init(String mixinPackage) {
        if (!initializedMixinPackages.contains(mixinPackage)) {
            initializedMixinPackages.add(mixinPackage);
            MixinInternals.registerExtension(new ConstraintsMixinExtension(mixinPackage), true);
        }
    }

}
