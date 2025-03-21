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
@IfModAbsent("thismoddoesntexist")
public class TestIfModAbsentTrue {

    @IfModAbsent("thismoddoesntexist")
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit1(CallbackInfo ci) {
        TestMod.LOGGER.info("TestIfModAbsentTrue succeeded");
        TestMod.modAbsentTruePassed = true;
    }

    @IfModLoaded("thismoddoesntexist")
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit2(CallbackInfo ci) {
        throw new Error("TestIfModAbsentFalse method failed!");
    }

}
