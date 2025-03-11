package com.moulberry.mixinconstraints.util;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public record Pair<F, S>(F first, S second) {
    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }
}
