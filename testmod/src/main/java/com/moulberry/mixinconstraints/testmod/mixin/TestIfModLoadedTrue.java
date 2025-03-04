package com.moulberry.mixinconstraints.testmod.mixin;

import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.moulberry.mixinconstraints.testmod.TestMod;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@IfModLoaded("testmod")
public class TestIfModLoadedTrue {

    @IfModLoaded("testmod")
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit(CallbackInfo ci) {
        TestMod.LOGGER.info("TestIfModLoadedTrue succeeded");
        TestMod.modLoadedTruePassed = true;
    }

    @IfModAbsent("testmod")
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit2(CallbackInfo ci) {
        throw new Error("TestIfModLoadedTrue method failed!");
    }

}
