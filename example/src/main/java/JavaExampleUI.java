import dev.deftu.componency.animations.AnimationTime;
import dev.deftu.componency.animations.ComponentAnimationProperties;
import dev.deftu.componency.animations.Easings;
import dev.deftu.componency.components.Component;
import dev.deftu.componency.components.ComponentProperties;
import dev.deftu.componency.components.impl.FrameComponent;
import dev.deftu.componency.components.impl.RectangleComponent;
import dev.deftu.componency.engine.Engine;
import dev.deftu.componency.properties.impl.ParentRelativeProperty;
import dev.deftu.componency.properties.impl.PixelProperty;
import dev.deftu.componency.properties.impl.StaticColorProperty;
import dev.deftu.componency.utils.Colors;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class JavaExampleUI {

    private final FrameComponent frame = Component.configure(new FrameComponent(), (frame) -> {
        frame.setName("frame");  // A name is optional. Only visible in debugging tools. Names are limited to [a-z0-9_].
        frame.setHidden(false); // Default
        frame.setClipping(true); // Default

        // We can define properties as such
        ComponentProperties properties = frame.getProperties();
        properties.setX(new PixelProperty(0));
        properties.setY(new PixelProperty(0));
        properties.setWidth(new ParentRelativeProperty(100f));
        properties.setHeight(new ParentRelativeProperty(100f));

        // The root component cannot have effects applied to it
    });

    private final RectangleComponent box = Component.configure(new RectangleComponent(), (box) -> {
        box.setName("box");

        // Alternatively, we can configure properties in their own scope for better readability
        Component.properties(box.getProperties(), (properties) -> {
            properties.setX(new PixelProperty(25));
            properties.setY(new PixelProperty(25));
            properties.setWidth(new PixelProperty(25));
            // TODO - properties.setHeight(new LinkedConstraint(properties.getWidth())); // We can link constraints to other constraints
        });

        // TODO - addEffect(new BlurEffect(new PixelConstraint(2))); // Blur with 2px radius
    }).onMouseClick(event -> {
        System.out.println("Box clicked at " + event.getX() + ", " + event.getY());
    }).attachedTo(frame);

    private final JavaFooterComponent footer = Component.configure(new JavaFooterComponent(), (footer) -> {
        footer.setName("footer");

        Component.properties(footer.getProperties(), (properties) -> {
            properties.setWidth(new ParentRelativeProperty(100f));
            properties.setHeight(new PixelProperty(50));
        });
    }).attachedTo(frame);

    public JavaExampleUI(Engine engine) {
        frame.makeRoot(engine); // The root component needs to be attached to our window

        // We can also listen to events after components have been created
        box.onMouseHover(event -> {
            System.out.println("Mouse entered box at " + event.getX() + ", " + event.getY());

            // It's possible to animate our properties on the fly
            ComponentAnimationProperties animationProperties = event.getComponent().beginAnimation();
            animationProperties.animateX(Easings.IN_OUT_CUBIC, new AnimationTime(TimeUnit.MILLISECONDS, 500), new ParentRelativeProperty(50f));
        }).onMouseUnhover(event -> {
            System.out.println("Mouse exited box at " + event.getX() + ", " + event.getY());
        });
    }

    // You'll want to wrap all of your frame's IO in exposed functions like this

    public void render() {
        frame.render();
    }

    // If our component is going too complex (requiring a lot of configuration), we can split it into a separate class.
    // Alternatively, if you just need to have it be reusable, this is the best means of doing so.
    private static class JavaFooterComponent extends Component {

        private final RectangleComponent background = Component.configure(new RectangleComponent(), (background) -> {
            background.setProperties(Component.properties(background.getProperties(), (properties) -> {
                properties.setWidth(new ParentRelativeProperty(100f));
                properties.setHeight(new ParentRelativeProperty(100f));
                properties.setFill(new StaticColorProperty(Colors.withAlphaPercentage(Color.BLACK, 50)));
            }));
        }).attachedTo(this);

        // TODO
        // private TextComponent text = Component.configure(new TextComponent("Hello, world!"), (text) -> {
        //     text.setProperties(Component.properties(text.getProperties(), (properties) -> {
        //         properties.setX(new CenteredProperty());
        //         properties.setY(new CenteredProperty());
        //     }));
        // }).attachTo(this);

    }

}
