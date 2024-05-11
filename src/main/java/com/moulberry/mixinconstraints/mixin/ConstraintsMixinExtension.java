package com.moulberry.mixinconstraints.mixin;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import com.llamalad7.mixinextras.utils.MixinInternals;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

public class ConstraintsMixinExtension implements IExtension {

    private final String mixinPackage;

    public ConstraintsMixinExtension(String mixinPackage) {
        if (!mixinPackage.endsWith(".")) {
            mixinPackage += ".";
        }
        this.mixinPackage = mixinPackage;
    }

    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        for (Pair<IMixinInfo, ClassNode> pair : MixinInternals.getMixinsFor(context)) {
            if (pair.getLeft().getConfig().getMixinPackage().equals(this.mixinPackage)) {
                MixinTransformer.transform(pair.getLeft(), pair.getRight());
            }
        }
    }

    @Override
    public void postApply(ITargetClassContext context) {

    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {

    }
}
