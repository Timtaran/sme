package io.github.timtaran.modelengine.objects;

import io.github.timtaran.modelengine.objects.blockbench.BbModelObject;
import lombok.Getter;

/** Model object class. */
@Getter
public class ModelObject {
  private final BbModelObject bbModel;
  private final String name;

  public ModelObject(String name, BbModelObject bbModelObject) {
    this.bbModel = bbModelObject;
    this.name = name;
  }
}
