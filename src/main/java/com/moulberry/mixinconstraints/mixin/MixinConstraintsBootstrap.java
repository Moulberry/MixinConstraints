package com.moulberry.mixinconstraints.mixin;

import com.moulberry.mixinconstraints.util.MixinHacks;
import java.util.HashSet;
import java.util.Set;


public class MixinConstraintsBootstrap {

    private static final Set<String> initializedMixinPackages = new HashSet<>();

    public static void init(String mixinPackage) {
        if(initializedMixinPackages.contains(mixinPackage)) return;

        initializedMixinPackages.add(mixinPackage);
        MixinHacks.registerMixinExtension(new ConstraintsMixinExtension(mixinPackage));
    }

}
