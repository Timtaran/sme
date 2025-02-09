package io.github.timtaran.modelengine.models.blockbench;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/** Outliner object class. */
@SuppressWarnings({"FieldMayBeFinal", "unused"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ModelOutliner {
  private String name = "";
  private String uuid;
  private double[] origin;

  private ModelOutliner[] children = new ModelOutliner[0];

  private boolean isObject = false;

  /**
   * Since blockbench outliner objects can be either outlinerObject or string with object uuid, we
   * process all unserializable objects here.
   *
   * @param data data from jackson
   * @return {@link ModelOutliner} outliner object
   */
  @JsonCreator
  public static ModelOutliner fromJson(String data) {
    ModelOutliner outlinerObject = new ModelOutliner();

    outlinerObject.uuid = data;
    outlinerObject.isObject = true;

    return outlinerObject;
  }
}
