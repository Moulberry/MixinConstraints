package com.moulberry.mixinconstraints;

import com.moulberry.mixinconstraints.checker.AnnotationChecker;
import com.moulberry.mixinconstraints.util.Pair;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;

public class MixinConstraints {

    public static final Logger LOGGER = LoggerFactory.getLogger("mixinconstraints");
    public static final boolean VERBOSE = "true".equals(System.getProperty("mixinconstraints.verbose"));
    public static final Loader LOADER = getLoader();

    public static boolean shouldApplyMixin(String mixinClassName) {
        try {
            // Use classNode instead of Class.forName to avoid loading at the wrong time
            ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);

            if (VERBOSE) {
                LOGGER.info("Checking class-level mixin constraints for {}", mixinClassName);
            }

            if (classNode.visibleAnnotations != null) {
                for (AnnotationNode node : classNode.visibleAnnotations) {
                    if (!AnnotationChecker.checkAnnotationNode(node)) {
                        if (VERBOSE) {
                            LOGGER.warn("Preventing application of mixin {} due to failing constraint", mixinClassName);
                        }
                        return false;
                    }
                }
            }

            return true;
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final MethodHandle TARGET_CLASS_CONTEXT_MIXINS;
    private static final MethodHandle MIXIN_INFO_GET_STATE;
    private static final MethodHandle STATE_CLASS_NODE;

    static {
        try {
            Class<?> TargetClassContext = Class.forName("org.spongepowered.asm.mixin.transformer.TargetClassContext");
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(TargetClassContext, MethodHandles.lookup());
            TARGET_CLASS_CONTEXT_MIXINS = lookup.findGetter(TargetClassContext, "mixins", SortedSet.class);

            Class<?> MixinInfo = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo");
            Class<?> MixinInfo$State = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo$State");

            lookup = MethodHandles.privateLookupIn(MixinInfo, MethodHandles.lookup());
            MIXIN_INFO_GET_STATE = lookup.findVirtual(MixinInfo, "getState", MethodType.methodType(MixinInfo$State));

            lookup = MethodHandles.privateLookupIn(MixinInfo$State, MethodHandles.lookup());
            STATE_CLASS_NODE = lookup.findGetter(MixinInfo$State, "classNode", ClassNode.class);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Pair<IMixinInfo, ClassNode>> getMixinsFor(ITargetClassContext context) {
        List<Pair<IMixinInfo, ClassNode>> result = new ArrayList<>();
        try {
            for(IMixinInfo mixin : (SortedSet<IMixinInfo>) TARGET_CLASS_CONTEXT_MIXINS.invoke(context)) {
                ClassNode classNode = (ClassNode) STATE_CLASS_NODE.invoke(MIXIN_INFO_GET_STATE.invoke(mixin));
                result.add(Pair.of(mixin, classNode));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static Loader getLoader() {
        if (doesClassExist("net.fabricmc.loader.api.FabricLoader"))
            return Loader.FABRIC;
        if (doesClassExist("net.minecraftforge.fml.loading.FMLLoader"))
            return Loader.FORGE;
        if (doesClassExist("net.neoforged.fml.loading.FMLLoader"))
            return Loader.NEOFORGE;

        throw new RuntimeException("Could not determine loader");
    }

    private static boolean doesClassExist(String className) {
        try {
            Class.forName(className, false, MixinConstraints.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public enum Loader {
        FORGE, NEOFORGE, FABRIC;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.ROOT)
                    .replace("n", "N")
                    .replace("f", "F");
        }
    }
}
