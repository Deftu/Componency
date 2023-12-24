<div align="center">

# Componency
A simple, declarative Minecraft UI library.

</div>

[![BisectHosting](https://www.bisecthosting.com/partners/custom-banners/8fb6621b-811a-473b-9087-c8c42b50e74c.png)](https://bisecthosting.com/deftu)

A simple library with many aspects taken from [Elementa][elementa], a
library, aiming to make GUI creation easier and more developer-friendly
between Minecraft versions. Anyone who is somewhat familiar with the
browser DOM should already have somewhat of an understanding of Componency's
inner workings.

As taken from Elementa,
> The library is based around the idea of being [declarative][declarative_programming].
This is a shift from how one would normally do graphics programming in Minecraft, or most other coding in general.
In Elementa, you do not have to write code to calculate _how_ to place a component at a certain point on the screen,
instead you simply have to describe _what_ you want.

## Usage
### Repository
It is recommended to use my [Gradle Toolkit][gradle-toolkit] as it will handle the repositories needed to use
the library for you.
<details>
    <summary>Without Gradle Toolkit</summary>

If you're not using my [Gradle Toolkit][gradle-toolkit], add the following code to your `repositories` block:

<details>
    <summary>Groovy (.gradle)</summary>

```gradle
maven {
    url = "https://maven.deftu.dev/releases"
}
```
</details>

<details>
    <summary>Kotlin (.gradle.kts)</summary>

Kotlin
```kotlin
maven(url = "https://maven.deftu.dev/releases")
```
</details>

</details>

### Dependency

<details>
    <summary>Forge</summary>

If you're using the Gradle Toolkit, you can use the following code to add the library as a dependency:
```gradle
dependencies {
    implementation(shade("dev.deftu:Componency-<MC_VERSION>-<MC_LOADER>:<VERSION>"))
}
```
For Kotlin Gradle DSL:
```kotlin
dependencies {
    implementation(shade("dev.deftu:Componency-<MC_VERSION>-<MC_LOADER>:<VERSION>")!!)
}
```

If you're **not** using the Gradle Toolkit, you will need to find your own way to implement shading/bundling the library into your JAR.

</details>

<details>
    <summary>Fabric</summary>

You can add the dependency to your Gradle project using the following code:
```gradle
modImplementation(include("dev.deftu:Componency-<MC_VERSION>-<MC_LOADER>:<VERSION>"))
```

For Kotlin Gradle DSL:
```gradle
modImplementation(include("dev.deftu:Componency-<MC_VERSION>-<MC_LOADER>:<VERSION>")!!)
```

If you don't plan on embedding the library inside of your final JAR, and want users to download it separately, simply remove the `include` configuration.

You will also need to include this inside your `fabric.mod.json` or `quilt.mod.json` file:
```json
"dependencies": {
    "componency": "<VERSION>"
}
```

</details>

### Versions
<details>
    <summary>Versions</summary>

| Minecraft Version | Library Version                                                                                                                           |
|-------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
| 1.8.9 Forge       | ![1.8.9 Forge Badge](https://maven.deftu.xyz/api/badge/latest/releases/xyz/deftu/componency-1.8.9-forge?color=C91212&name=Componency)     |
| 1.12.2 Forge      | ![1.12.2 Forge Badge](https://maven.deftu.xyz/api/badge/latest/releases/xyz/deftu/componency-1.12.2-forge?color=C91212&name=Componency)   |
| 1.14.4 Forge      | ![1.14.4 Forge Badge](https://maven.deftu.xyz/api/badge/latest/releases/xyz/deftu/componency-1.14.4-forge?color=C91212&name=Componency)   |
| 1.14.4 Fabric     | ![1.14.4 Fabric Badge](https://maven.deftu.xyz/api/badge/latest/releases/xyz/deftu/componency-1.14.4-fabric?color=C91212&name=Componency) |
| 1.16.5 Fabric     | ![1.16.5 Fabric Badge](https://maven.deftu.xyz/api/badge/latest/releases/xyz/deftu/componency-1.16.5-fabric?color=C91212&name=Componency) |
| 1.17.1 Fabric     | ![1.17.1 Fabric Badge](https://maven.deftu.xyz/api/badge/latest/releases/xyz/deftu/componency-1.17.1-fabric?color=C91212&name=Componency) |
| 1.18.2 Fabric     | ![1.18.2 Fabric Badge](https://maven.deftu.xyz/api/badge/latest/releases/xyz/deftu/componency-1.18.2-fabric?color=C91212&name=Componency) |
| 1.19.2 Fabric     | ![1.19.2 Fabric Badge](https://maven.deftu.xyz/api/badge/latest/releases/xyz/deftu/componency-1.19.2-fabric?color=C91212&name=Componency) |
| 1.19.3 Fabric     | ![1.19.3 Fabric Badge](https://maven.deftu.xyz/api/badge/latest/releases/xyz/deftu/componency-1.19.3-fabric?color=C91212&name=Componency) |

</details>

[elementa]: https://github.com/EssentialGG/Elementa
[declarative_programming]: https://en.wikipedia.org/wiki/Declarative_programming
[gradle-toolkit]: https://github.com/Deftu/Gradle-Toolkit
