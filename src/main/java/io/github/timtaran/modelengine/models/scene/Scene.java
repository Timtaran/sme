package io.github.timtaran.modelengine.models.scene;

import lombok.Getter;

/** Blender scene object class. */
@Getter
public class Scene {
  private String name;
  private SceneKeyframe[] keyframes;
}
