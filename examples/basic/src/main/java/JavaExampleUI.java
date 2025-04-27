import dev.deftu.componency.color.Color;
import dev.deftu.componency.components.Component;
import dev.deftu.componency.components.SimpleComponentProperties;
import dev.deftu.componency.components.impl.FrameComponent;
import dev.deftu.componency.components.impl.RectangleComponent;
import dev.deftu.componency.components.impl.TextComponent;
import dev.deftu.componency.platform.Platform;
import dev.deftu.componency.properties.VectorProperty;
import dev.deftu.componency.properties.impl.*;
import dev.deftu.textile.SimpleTextHolder;

public class JavaExampleUI {

    public final FrameComponent frame = new FrameComponent().configure(properties -> {
        properties.setName("frame");
        properties.setX(new PixelProperty(0));
        properties.setY(new PixelProperty(0));
        properties.setWidth(new ParentRelativeProperty(100f));
        properties.setHeight(new ParentRelativeProperty(100f));
    });

    private final RectangleComponent box = new RectangleComponent().configure(properties -> {
        properties.setName("box");
        properties.setX(new PixelProperty(25));
        properties.setY(new PixelProperty(25));
        properties.setWidth(new PixelProperty(25));
        properties.setHeight(new LinkedProperty((VectorProperty) properties.getHeight()));
        properties.setFill(new StaticColorProperty(Color.RED));
    }).onPointerClick(event -> {
        System.out.println("Box clicked at " + event.getX() + ", " + event.getY());
    }).attachTo(frame);

    private final JavaFooterComponent footer = new JavaFooterComponent().configure(properties -> {
        properties.setName("footer");
        properties.setWidth(new ParentRelativeProperty(100f));
        properties.setHeight(new PixelProperty(50));
    }).attachTo(frame);

    public JavaExampleUI(Platform platform) {
        frame.makeRoot(platform); // The root component needs to be attached to our window

        // We can also listen to events after components have been created
        box.onHover(event -> {
            System.out.println("Mouse entered box at " + event.getX() + ", " + event.getY());

            // It's possible to animate our properties on the fly
            // TODO
            // ComponentAnimationProperties animationProperties = event.getComponent().beginAnimation();
            // animationProperties.animateX(Easings.IN_OUT_CUBIC, new AnimationTime(TimeUnit.MILLISECONDS, 500), new ParentRelativeProperty(50f));
        }).onUnhover(event -> {
            System.out.println("Mouse exited box at " + event.getX() + ", " + event.getY());
        });
    }

    // If our component is going too complex (requiring a lot of configuration), we can split it into a separate class.
    // Alternatively, if you just need to have it be reusable, this is the best means of doing so.
    private static class JavaFooterComponent extends Component<JavaFooterComponent, SimpleComponentProperties<JavaFooterComponent>> {

        private final RectangleComponent background = new RectangleComponent().configure(properties -> {
            properties.setWidth(new ParentRelativeProperty(100f));
            properties.setHeight(new ParentRelativeProperty(100f));
            properties.setFill(new StaticColorProperty(Color.BLACK.withAlphaPercentage(50)));
        }).attachTo(this);

        private TextComponent text = new TextComponent().configure(properties -> {
            properties.setX(new CenteredProperty());
            properties.setY(new CenteredProperty());
            properties.setText(new SimpleTextHolder("Hello, world!"));
        }).attachTo(this);

        public JavaFooterComponent() {
            super(SimpleComponentProperties<JavaFooterComponent>::new);
        }

    }

}
