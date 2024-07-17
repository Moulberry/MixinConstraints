package com.moulberry.mixinconstraints.checker;

import com.moulberry.mixinconstraints.MixinConstraints;
import com.moulberry.mixinconstraints.annotations.IfDevEnvironment;
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import java.util.List;

public class AnnotationChecker {

    private static final String IF_MOD_LOADED_DESC = Type.getDescriptor(IfModLoaded.class);
    private static final String IS_MOD_ABSENT_DESC = Type.getDescriptor(IfModAbsent.class);
    private static final String IF_DEV_ENVIRONMENT_DESC = Type.getDescriptor(IfDevEnvironment.class);
    private static final String IF_MINECRAFT_VERSION_DESC = Type.getDescriptor(IfMinecraftVersion.class);

    public static boolean isConstraintAnnotationNode(AnnotationNode node) {
        return IF_MOD_LOADED_DESC.equals(node.desc) || IS_MOD_ABSENT_DESC.equals(node.desc) ||
            IF_DEV_ENVIRONMENT_DESC.equals(node.desc) || IF_MINECRAFT_VERSION_DESC.equals(node.desc);
    }


    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "DuplicatedCode"})
	public static boolean checkAnnotationNode(AnnotationNode node) {
        if (IF_MOD_LOADED_DESC.equals(node.desc)) {
            String value = getAnnotationValue(node, "value", "");
            if (value.isEmpty()) throw new IllegalArgumentException("modid must not be empty");

            List<String> aliases = getAnnotationValue(node, "aliases", List.of());
            String minVersion = getAnnotationValue(node, "minVersion", null);
            String maxVersion = getAnnotationValue(node, "maxVersion", null);

            boolean pass = ConstraintChecker.checkModLoaded(value, aliases, minVersion, maxVersion);

            if (MixinConstraints.VERBOSE) {
                String result = pass ? "PASS" : "FAILED";
                MixinConstraints.LOGGER.info("@IfModLoaded(value={}, minVersion={}, maxVersion={}) {}", value, minVersion, maxVersion, result);
            }

            return pass;
        } else if (IS_MOD_ABSENT_DESC.equals(node.desc)) {
            String value = getAnnotationValue(node, "value", "");
            if (value.isEmpty()) throw new IllegalArgumentException("modid must not be empty");

            List<String> aliases = getAnnotationValue(node, "aliases", List.of());
            String minVersion = getAnnotationValue(node, "minVersion", null);
            String maxVersion = getAnnotationValue(node, "maxVersion", null);

            boolean pass = ConstraintChecker.checkModAbsent(value, aliases, minVersion, maxVersion);

            if (MixinConstraints.VERBOSE) {
                String result = pass ? "PASS" : "FAILED";
                MixinConstraints.LOGGER.info("@IfModAbsent(value={}, minVersion={}, maxVersion={}) {}", value, minVersion, maxVersion, result);
            }

            return pass;
        } else if (IF_DEV_ENVIRONMENT_DESC.equals(node.desc)) {
            boolean negate = getAnnotationValue(node, "negate", false);

            boolean pass = ConstraintChecker.checkDevEnvironment() != negate;

            if (MixinConstraints.VERBOSE) {
                String result = pass ? "PASS" : "FAILED";
                MixinConstraints.LOGGER.info("@IfDevEnvironment(negate={}) {}", negate, result);
            }

            return pass;
        } else if (IF_MINECRAFT_VERSION_DESC.equals(node.desc)) {
            String minVersion = getAnnotationValue(node, "minVersion", null);
            String maxVersion = getAnnotationValue(node, "maxVersion", null);
            boolean negate = getAnnotationValue(node, "negate", false);

            boolean pass = ConstraintChecker.checkMinecraftVersion(minVersion, maxVersion) != negate;

            if (MixinConstraints.VERBOSE) {
                String result = pass ? "PASS" : "FAILED";
                MixinConstraints.LOGGER.info("@IfMinecraftVersion(minVersion={}, maxVersion={}, negate={}) {}", minVersion, maxVersion, negate, result);
            }

            return pass;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getAnnotationValue(AnnotationNode annotation, String key, T defaultValue) {
        if (annotation.values == null) return defaultValue;

        for (int i = 0; i < annotation.values.size(); i += 2) {
            if (key.equals(annotation.values.get(i))) {
                return (T) annotation.values.get(i + 1);
            }
        }

        return defaultValue;
    }

}
