package io.github.timtaran.modelengine.objects.blockbench;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@SuppressWarnings("FieldMayBeFinal")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class OutlinerObject {
  private String name = "";
  private String uuid;

  private OutlinerObject[] children = new OutlinerObject[0];

  private boolean isObject = false;

  @JsonCreator
  public static OutlinerObject fromJson(String data) {
    OutlinerObject outlinerObject = new OutlinerObject();

    outlinerObject.uuid = data;
    outlinerObject.isObject = true;

    return outlinerObject;
  }
}
