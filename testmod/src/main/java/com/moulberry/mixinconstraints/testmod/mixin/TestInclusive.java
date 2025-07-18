package com.moulberry.mixinconstraints.testmod.mixin;

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class TestInclusive {

    @IfMinecraftVersion(minVersion = "1.20.6", maxVersion = "1.20.6", negate = true)
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit(CallbackInfo ci) {
        throw new Error("Version isn't 1.20.6");
    }

    @IfMinecraftVersion(minVersion = "1.20.6", negate = true)
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit2(CallbackInfo ci) {
        throw new Error("Inclusive min didn't include 1.20.6");
    }

    @IfMinecraftVersion(minVersion = "1.20.6", minInclusive = false)
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit3(CallbackInfo ci) {
        throw new Error("Exclusive min included 1.20.6");
    }

    @IfMinecraftVersion(maxVersion = "1.20.6", negate = true)
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit4(CallbackInfo ci) {
        throw new Error("Inclusive max didn't include 1.20.6");
    }

    @IfMinecraftVersion(maxVersion = "1.20.6", maxInclusive = false)
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit5(CallbackInfo ci) {
        throw new Error("Exclusive max included 1.20.6");
    }

}
