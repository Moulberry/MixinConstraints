package com.moulberry.mixinconstraints.testmod;

import com.moulberry.mixinconstraints.MixinConstraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestMod {
	public static final Logger LOGGER = LoggerFactory.getLogger("MixinConstraints TestMod");

    public static boolean modLoadedTruePassed = false;
    public static boolean modAbsentTruePassed = false;

	public static void init() {
		LOGGER.info("init() called");
		LOGGER.info("Platform: {}", MixinConstraints.getLoaderName());

        if (!modLoadedTruePassed) {
            throw new Error("TestIfModLoadedTrue failed!");
        }
        if (!modAbsentTruePassed) {
            throw new Error("TestIfModAbsentTrue failed!");
        }
	}
}
