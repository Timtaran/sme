package io.github.timtaran.modelengine.models;

import io.github.timtaran.modelengine.models.blockbench.BbModel;
import lombok.Getter;

/** Model object class. */
@Getter
public class Model {
  private final BbModel bbModel;
  private final String name;

  public Model(String name, BbModel bbModel) {
    this.bbModel = bbModel;
    this.name = name;
  }
}
