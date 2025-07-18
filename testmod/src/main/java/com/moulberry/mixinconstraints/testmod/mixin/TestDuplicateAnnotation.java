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
@IfModLoaded("thismoddoesntexist")
@IfModLoaded("thismoddoesntexist2")
public class TestDuplicateAnnotation {

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void clinit(CallbackInfo ci) {
        throw new Error("TestDuplicateAnnotation method failed!");
    }

}
