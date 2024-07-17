package com.moulberry.mixinconstraints.mixin;

import com.moulberry.mixinconstraints.checker.AnnotationChecker;
import com.moulberry.mixinconstraints.MixinConstraints;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Iterator;
import java.util.List;

public class MixinTransformer {


    public static void transform(IMixinInfo info, ClassNode classNode) {
        if (MixinConstraints.VERBOSE) {
            MixinConstraints.LOGGER.info("Checking inner mixin constraints for {}", info.getClassName());
        }

        if (classNode.visibleAnnotations != null) {
            classNode.visibleAnnotations.removeIf(AnnotationChecker::isConstraintAnnotationNode);
        }

        classNode.fields.removeIf(field -> shouldRemoveTarget("field " + field.name, field.visibleAnnotations));
        classNode.methods.removeIf(method -> shouldRemoveTarget("method " + method.name, method.visibleAnnotations));
    }

    private static boolean shouldRemoveTarget(String targetName, List<AnnotationNode> annotations) {
        if (annotations == null) {
            return false;
        }

        boolean remove = false;

        Iterator<AnnotationNode> annotationIterator = annotations.iterator();
        while (annotationIterator.hasNext()) {
            AnnotationNode annotationNode = annotationIterator.next();

            if (!remove && !AnnotationChecker.checkAnnotationNode(annotationNode)) {
                if (MixinConstraints.VERBOSE) {
                    MixinConstraints.LOGGER.warn("Preventing application of mixin {} due to failing constraint", targetName);
                }

                remove = true;
                annotationIterator.remove();
            } else if (AnnotationChecker.isConstraintAnnotationNode(annotationNode)) {
                annotationIterator.remove();
            }
        }

        return remove;
    }

}
