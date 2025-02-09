package io.github.timtaran.modelengine.models.scene;

import lombok.Getter;

@Getter
public class SceneKeyframe {
  private int frame;
  private KeyframeAction[] actions;
}
