package com.moulberry.mixinconstraints.util;

public final class ExceptionUtil {
	private ExceptionUtil() {
	}

	@SuppressWarnings("unchecked")
	public static <T extends Throwable> RuntimeException unchecked(Throwable t) throws T {
		throw (T) t;
	}
}
