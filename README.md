# MixinConstraints

A library to enable/disable mixins using annotations.

Annotations can be applied to mixin classes to toggle the whole mixin, or individual fields/methods for more precision.

# Installing

MixinConstraints is available through Maven Central.

The library currently only supports Fabric.

__Gradle__
```groovy
dependencies {
    include(implementation("com.moulberry:mixinconstraints:1.0.1"))
}
```

Next, you will need to bootstrap the library to affect your mixins.

The easiest way to do so is by using the provided Mixin Plugin by adding the following to your modid.mixins.json:
```json
{
    "plugin": "com.moulberry.mixinconstraints.ConstraintsMixinPlugin",
}
```

If you already have your own mixin plugin, you can use the [ConstraintsMixinPlugin](https://github.com/Moulberry/MixinConstraints/blob/master/src/main/java/com/moulberry/mixinconstraints/ConstraintsMixinPlugin.java) class as a reference to add support.

# Using the library

The library provides 4 annotations
- @IfModLoaded (checks if a mod is loaded)
- @IfModAbsent (checks if a mod is absent)
- @IfMinecraftVersion (checks if the Minecraft version matches a range)
- @IfDevEnvironment (checks if the game is running inside a development environment)

These annotations can be applied to classes to control whether the whole mixin is applied

For example, the following mixin will only be applied if sodium or embeddium is present
```java
@IfModLoaded(value = "sodium", aliases = {"embeddium"})
@Mixin(BlockOcclusionCache.class)
public class MixinSodiumBlockOcclusionCache {
   ...
}
```

Additionally, the annotations can also be applied to individual fields/methods

For example, the following mixin will inject only one of the two methods depending on whether the mod is in a dev environment
```java
@Mixin(Minecraft.class)
public class MixinMinecraft {
    @IfDevEnvironment
    @Inject(at = @At("HEAD"), method = "run")
    private void runDev(CallbackInfo info) {
        System.out.println("Hello from a dev environment!");
    }

    @IfDevEnvironment(negate = true)
    @Inject(at = @At("HEAD"), method = "run")
    private void runProd(CallbackInfo info) {
        System.out.println("Hello from production!");
    }
}
```

Some constraints support version ranges, which can be used like so:
```java
@IfModLoaded(value = "modernfix", minVersion = "5.11", maxVersion = "5.15")
```
The version comparison uses Fabric API's [Version](https://github.com/FabricMC/fabric-loader/blob/master/src/main/java/net/fabricmc/loader/api/Version.java). Ensure the version resembles a semver string in order for range comparison to work as expected.

# License
The library is available under the MIT license.
