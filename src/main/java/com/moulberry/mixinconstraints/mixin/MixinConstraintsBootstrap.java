package com.moulberry.mixinconstraints.mixin;

import com.moulberry.mixinconstraints.util.MixinHacks;

public class MixinConstraintsBootstrap {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;
        MixinHacks.registerMixinExtension(new ConstraintsMixinExtension());
    }

    /**
     * @deprecated use {@link #init()} instead.
     */
    @Deprecated
    public static void init(String mixinPackage) {
		init();
	}

}
