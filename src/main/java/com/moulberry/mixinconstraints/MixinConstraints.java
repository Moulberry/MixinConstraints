package com.moulberry.mixinconstraints;

import com.moulberry.mixinconstraints.checker.AnnotationChecker;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.service.MixinService;

import java.io.IOException;

public class MixinConstraints {

    public static final Logger LOGGER = LoggerFactory.getLogger("mixinconstraints");
    public static boolean VERBOSE = "true".equals(System.getProperty("mixinconstraints.verbose"));

    public static boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
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

}
