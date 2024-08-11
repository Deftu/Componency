# Componency
A simple, [declarative][declarative_programming] UI library built from the ground up for use in any environment.

[![wakatime](https://wakatime.com/badge/user/25be8ed5-7461-4fcf-93f7-0d88a7692cca/project/3863d6a3-adb4-4e89-8fae-81c9e8af6809.svg?style=for-the-badge)](https://wakatime.com/badge/user/25be8ed5-7461-4fcf-93f7-0d88a7692cca/project/3863d6a3-adb4-4e89-8fae-81c9e8af6809)

---

## Inspiration

Componency is roughly based on 3 other things:
- [Elementa][elementa]'s design
- [Figma][figma]'s naming conventions
- [Ktor][ktor]'s DSL design

I used concepts taken from all 3 of these projects to create a simple, [declarative][declarative_programming] UI library that can be used in any environment.

---

## Disclaimer

This library is primarily designed for Kotlin, though it does have some additional support for Java.

All the examples from here on out will be in Kotlin, but the library can be used in Java as well.

---

## Set up

### Repository


<details>
    <summary>Groovy (.gradle)</summary>

```gradle
maven {
    name = "Deftu Releases"
    url = "https://maven.deftu.dev/releases"
}
```
</details>

<details>
    <summary>Kotlin (.gradle.kts)</summary>

```kotlin
maven(url = "https://maven.deftu.dev/releases") {
    name = "Deftu Releases"
}
```
</details>

### Dependency

![Repository badge](https://maven.deftu.dev/api/badge/latest/releases/dev/deftu/componency?color=C33F3F&name=Componency)

<details>
    <summary>Groovy (.gradle)</summary>

```gradle
modImplementation "dev.deftu:componency:<VERSION>"
```

</details>

<details>
    <summary>Kotlin (.gradle.kts)</summary>

```gradle
implementation("dev.deftu:componency:<VERSION>")
```

</details>

---

## Usage

### Engine

To use the library at all, you need to create your Componency engine. The engine is responsible for providing Componency with any data it needs and giving it the ability to interact with your rendering system of choice.

```kotlin
package com.example

import dev.deftu.componency.engine.Engine
import dev.deftu.componency.engine.InputEngine
import dev.deftu.componency.engine.RenderEngine
import java.awt.Color

class MyEngine : Engine {

    override val inputEngine: InputEngine by lazy {
        MyInputEngine()
    }

    override val renderEngine: RenderEngine by lazy {
        MyRenderEngine()
    }

}

class MyInputEngine : InputEngine {

    override val mouseX: Float = 0f

    override val mouseY: Float = 0f

}

class MyRenderEngine : RenderEngine {

    override val viewportWidth: Int = 0

    override val viewportHeight: Int = 0

    override val animationFps: Int = 144 // Recommended to be no more than 300

    override fun startFrame() {
        // Start rendering frame (apply any transformations, etc.)
    }

    override fun endFrame() {
        // End rendering frame (apply any transformations, etc.)
    }

    override fun fill(x1: Float, y1: Float, x2: Float, y2: Float, color: Color, radius: Float) {
        // Fill a rectangle with a color and a radius
    }

}
```

### Your UI

Now that you have your engine set up, you can start creating your UI.

```kotlin
object Main {
    
    @JvmStatic
    fun main(args: Array<String>) {
        val engine = MyEngine()
        val ui = MyUI(engine)
        
        // Inside your render loop, you can simply call `ui.render()` to render your UI.
        ui.render() // (This is a placeholder for your actual render loop)
    }
    
}

class MyUI(engine: Engine) {
    
    // Common practice is to use a frame as your root component
    private val frame = FrameComponent().configure {
        // In Componency, we define properties inside their own scope when configuring.
        // The majority of the most commonly used properties have extension variables and functions to make them easier to access.
        properties {
            width = 100.percent
            height = 100.percent
        }
    }.makeRoot(engine) // Finally, we make the frame the root component of this UI by giving it the engine.
    
    private val box = RectangleComponent().configure {
        properties {
            x = 25.percent
            y = 25.percent
            width = 50.percent
            height = 50.percent
            color = Color.RED.asProperty
        }
    }.attachTo(frame) // We make the box a child of the frame.
    
    // From here, you can add more components to the frame or the box.

    // Finally, we provide an external means of rendering our frame.
    fun render() {
        frame.handleRender()
    }

}
```

## Further examples

For more examples, you can check out the [basic examples](./example-basic) directory. ([Java](./example-basic/src/main/java/JavaExampleUI.java), [Kotlin](./example-basic/src/main/kotlin/KotlinExampleUI.kt))

---

[![BisectHosting](https://www.bisecthosting.com/partners/custom-banners/8fb6621b-811a-473b-9087-c8c42b50e74c.png)](https://bisecthosting.com/deftu)

---

**This project is licensed under [LGPL-3.0][lgpl]**\
**&copy; 2024 Deftu**

[elementa]: https://github.com/EssentialGG/Elementa
[figma]: https://www.figma.com/
[ktor]: https://ktor.io/
[declarative_programming]: https://en.wikipedia.org/wiki/Declarative_programming
[lgpl]: https://www.gnu.org/licenses/lgpl-3.0.en.html
