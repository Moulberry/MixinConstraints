package com.moulberry.mixinconstraints.mixin;

import com.moulberry.mixinconstraints.util.MixinHacks;
import com.moulberry.mixinconstraints.util.MixinInfo;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

public class ConstraintsMixinExtension implements IExtension {

    public ConstraintsMixinExtension() {}

    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        for (MixinInfo mixinInfo : MixinHacks.getMixinsFor(context)) {
            MixinTransformer.transform(mixinInfo);
        }
    }

    @Override
    public void postApply(ITargetClassContext context) {

    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {

    }
}
