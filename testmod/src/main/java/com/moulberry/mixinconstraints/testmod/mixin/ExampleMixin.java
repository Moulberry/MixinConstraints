package com.moulberry.mixinconstraints.testmod.mixin;

import com.moulberry.mixinconstraints.annotations.IfDevEnvironment;
import com.moulberry.mixinconstraints.testmod.TestMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screens.TitleScreen;

@Mixin(TitleScreen.class)
public class ExampleMixin {

	@IfDevEnvironment
	@Inject(method = "init()V", at = @At("HEAD"))
	private void initDev(CallbackInfo info) {
		TestMod.LOGGER.info("in dev environment");
	}

	@IfDevEnvironment(negate = true)
	@Inject(method = "init()V", at = @At("HEAD"))
	private void initProd(CallbackInfo info) {
		TestMod.LOGGER.info("in production");
	}
}
