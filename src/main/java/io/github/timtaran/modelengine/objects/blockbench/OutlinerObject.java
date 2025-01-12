package io.github.timtaran.modelengine.objects.blockbench;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/** Outliner object class. */
@SuppressWarnings("FieldMayBeFinal")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OutlinerObject {
  private String name = "";
  private String uuid;

  private OutlinerObject[] children = new OutlinerObject[0];

  private boolean isObject = false;

  /**
   * Since blockbench outliner objects can be either outlinerObject or string with object uuid, we
   * process all unserializable objects here.
   *
   * @param data data from jackson
   * @return {@link OutlinerObject} outliner object
   */
  @JsonCreator
  public static OutlinerObject fromJson(String data) {
    OutlinerObject outlinerObject = new OutlinerObject();

    outlinerObject.uuid = data;
    outlinerObject.isObject = true;

    return outlinerObject;
  }
}
