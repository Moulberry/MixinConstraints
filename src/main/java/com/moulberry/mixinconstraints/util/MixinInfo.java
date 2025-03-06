package com.moulberry.mixinconstraints.util;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.SortedSet;

public record MixinInfo(SortedSet<IMixinInfo> mixinInfos, IMixinInfo mixin, ClassNode classNode) {
    public void disableMixin() {
        this.mixinInfos.remove(this.mixin);
    }
}