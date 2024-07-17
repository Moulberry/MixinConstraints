package com.moulberry.mixinconstraints;

public abstract class Abstractions {
	private static final Abstractions instance;
	static {
		try {
			Class<?> clazz = switch(MixinConstraints.LOADER) {
				case FABRIC -> Class.forName("com.moulberry.mixinconstraints.FabricAbstractionsImpl");
				case FORGE -> Class.forName("com.moulberry.mixinconstraints.ForgeAbstractionsImpl");
				case NEOFORGE -> Class.forName("com.moulberry.mixinconstraints.NeoForgeAbstractionsImpl");
				default -> throw new IllegalStateException("Unexpected value: " + MixinConstraints.LOADER);
			};

			instance = (Abstractions) clazz.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isDevelopmentEnvironment() {
		return instance.isDevEnvironment();
	}

	public static boolean isModLoadedWithinVersion(String modId, String minVersion, String maxVersion) {
		String version = instance.getModVersion(modId);
		if(version == null) {
			return false;
		}

		return instance.isVersionInRange(version, minVersion, maxVersion);
	}

	protected abstract boolean isDevEnvironment();
	protected abstract String getModVersion(String modId);
	protected abstract boolean isVersionInRange(String version, String minVersion, String maxVersion);
}
