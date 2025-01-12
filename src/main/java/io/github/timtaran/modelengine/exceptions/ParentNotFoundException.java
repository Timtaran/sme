package io.github.timtaran.modelengine.exceptions;

import lombok.Getter;

/**
 * Thrown when {@link io.github.timtaran.modelengine.loaders.ModelLoader} cannot find parent model.
 */
@Getter
public class ParentNotFoundException extends Exception {
  private final String parentName;

  public ParentNotFoundException(String parentName) {
    this.parentName = parentName;
  }

  public ParentNotFoundException(String message, String parentName) {
    super(message);
    this.parentName = parentName;
  }
}
