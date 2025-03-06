package com.moulberry.mixinconstraints.testmod.mixin;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@IfModLoaded("doesntexist")
@Mixin(targets = "com.doesnt.exist.Main")
public class TestMixinIntoNonExistentClass {

    /*
     * Without the annotation there would be an error message in the logs like:
     * Error loading class: com/doesnt/exist/Main (java.lang.ClassNotFoundException: com/doesnt/exist/Main)
     *
     * This is undesirable, so the @IfModLoaded annotation should prevent the loading early enough for this message to not appear
     */

}
