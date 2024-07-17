package com.moulberry.mixinconstraints.testmod;

import net.minecraftforge.fml.common.Mod;

@Mod("testmod")
public class TestModForge {
	public TestModForge() {
		TestMod.init();
	}
}
