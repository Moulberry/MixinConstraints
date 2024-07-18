package com.moulberry.mixinconstraints.testmod;

import net.fabricmc.api.ModInitializer;

public class TestModFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		TestMod.init();
	}
}
