package com.moulberry.mixinconstraints.mixin;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.IExtensionRegistry;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckClass;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static com.moulberry.mixinconstraints.util.ExceptionUtil.unchecked;

public class MixinConstraintsBootstrap {

    private static final MethodHandle getExtensions;
    private static final MethodHandle getActiveExtensions;
    private static final MethodHandle setActiveExtensions;

    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(Extensions.class, MethodHandles.lookup());

            getExtensions = lookup.findGetter(Extensions.class, "extensions", List.class);
            getActiveExtensions = lookup.findGetter(Extensions.class, "activeExtensions", List.class);
            setActiveExtensions = lookup.findSetter(Extensions.class, "activeExtensions", List.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw unchecked(e);
        }
    }

    private static final Set<String> initializedMixinPackages = new HashSet<>();

    @SuppressWarnings("unchecked")
    public static void init(String mixinPackage) {
		if(initializedMixinPackages.contains(mixinPackage)) return;

		initializedMixinPackages.add(mixinPackage);
        IExtension extension = new ConstraintsMixinExtension(mixinPackage);

        try {
            IMixinTransformer transformer = (IMixinTransformer) MixinEnvironment.getDefaultEnvironment().getActiveTransformer();
            IExtensionRegistry extensions = transformer.getExtensions();
            List<IExtension> extensionsList = (List<IExtension>) getExtensions.invoke(extensions);
            addExtension(extensionsList, extension);
            List<IExtension> activeExtensions = new ArrayList<>((List<IExtension>) getActiveExtensions.invoke(extensions));
            addExtension(activeExtensions, extension);
            setActiveExtensions.invoke(extensions, Collections.unmodifiableList(activeExtensions));
        } catch (Throwable t) {
            throw unchecked(t);
        }
	}

    private static void addExtension(List<IExtension> extensions, IExtension newExtension) {
		extensions.addFirst(newExtension);

		// If this runs before our extensions it will fail since we're not done generating our bytecode.
        List<IExtension> lateExtensions = new ArrayList<>();
        for (ListIterator<IExtension> it = extensions.listIterator(); it.hasNext(); ) {
            IExtension extension = it.next();
            if (extension instanceof ExtensionCheckClass) {
                it.remove();
                lateExtensions.add(extension);
            }
        }
        extensions.addAll(lateExtensions);
    }

}
