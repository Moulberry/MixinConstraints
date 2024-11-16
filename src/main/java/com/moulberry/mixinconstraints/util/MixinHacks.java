package com.moulberry.mixinconstraints.util;

import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckClass;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;

/**
 * Most of this class is adapted from MixinExtras's {@code MixinInternals} class, by LlamaLad7.
 * MixinExtras is licensed under the MIT License: <a href="https://github.com/LlamaLad7/MixinExtras/blob/master/LICENSE">here</a>.
 */
@SuppressWarnings("unchecked")
public final class MixinHacks {
	private static final MethodHandle TARGET_CLASS_CONTEXT_MIXINS;
	private static final MethodHandle MIXIN_INFO_GET_STATE;
	private static final MethodHandle STATE_CLASS_NODE;

	private static final MethodHandle EXTENSIONS_EXTENSIONS;
	private static final MethodHandle EXTENSIONS_ACTIVE_EXTENSIONS_GET;
	private static final MethodHandle EXTENSIONS_ACTIVE_EXTENSIONS_SET;

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

			lookup = MethodHandles.privateLookupIn(Extensions.class, MethodHandles.lookup());

			EXTENSIONS_EXTENSIONS = lookup.findGetter(Extensions.class, "extensions", List.class);
			EXTENSIONS_ACTIVE_EXTENSIONS_GET = lookup.findGetter(Extensions.class, "activeExtensions", List.class);
			EXTENSIONS_ACTIVE_EXTENSIONS_SET = lookup.findSetter(Extensions.class, "activeExtensions", List.class);

		} catch (Throwable e) {
			throw unchecked(e);
		}
	}

	public static void registerMixinExtension(IExtension extension) {
		try {
			Extensions extensions = (Extensions) ((IMixinTransformer) MixinEnvironment.getDefaultEnvironment().getActiveTransformer())
					.getExtensions();
			addExtension((List<IExtension>) EXTENSIONS_EXTENSIONS.invokeExact(extensions), extension);

			List<IExtension> activeExtensions = new ArrayList<>((List<IExtension>) EXTENSIONS_ACTIVE_EXTENSIONS_GET.invokeExact(extensions));
			addExtension(activeExtensions, extension);

			EXTENSIONS_ACTIVE_EXTENSIONS_SET.invokeExact(extensions, Collections.unmodifiableList(activeExtensions));
		} catch (Throwable t) {
			throw unchecked(t);
		}
	}

	public static List<Pair<IMixinInfo, ClassNode>> getMixinsFor(ITargetClassContext context) {
		List<Pair<IMixinInfo, ClassNode>> result = new ArrayList<>();
		try {
			// note: can't use invokeExact here because TargetClassContext is not public
			for(IMixinInfo mixin : (SortedSet<IMixinInfo>) TARGET_CLASS_CONTEXT_MIXINS.invoke(context)) {
				ClassNode classNode = (ClassNode) STATE_CLASS_NODE.invoke(MIXIN_INFO_GET_STATE.invoke(mixin));
				result.add(Pair.of(mixin, classNode));
			}
		} catch (Throwable e) {
			throw unchecked(e);
		}
		return result;
	}

	private static void addExtension(List<IExtension> extensions, IExtension newExtension) {
		extensions.add(0, newExtension);

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

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException unchecked(Throwable t) throws T {
		throw (T) t;
	}

	private MixinHacks() {
	}
}
