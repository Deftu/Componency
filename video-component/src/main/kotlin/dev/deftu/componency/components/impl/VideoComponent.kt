package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties

public class VideoComponentProperties(component: VideoComponent) : ComponentProperties<VideoComponent, VideoComponentProperties>(component) {

}

public class VideoComponent : Component<VideoComponent, VideoComponentProperties>(::VideoComponentProperties) {

}
