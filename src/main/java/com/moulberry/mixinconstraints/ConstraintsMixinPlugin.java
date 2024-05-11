package com.moulberry.mixinconstraints;

import com.moulberry.mixinconstraints.mixin.MixinConstraintsBootstrap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ConstraintsMixinPlugin implements IMixinConfigPlugin {

    private String mixinPackage;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (this.mixinPackage != null && !mixinClassName.startsWith(this.mixinPackage)) {
            return true;
        }
        return MixinConstraints.shouldApplyMixin(targetClassName, mixinClassName);
    }

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;
        MixinConstraintsBootstrap.init(mixinPackage);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }


    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
