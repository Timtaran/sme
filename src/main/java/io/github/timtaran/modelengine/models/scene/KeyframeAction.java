package io.github.timtaran.modelengine.models.scene;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.timtaran.modelengine.enums.scene.ActionTypes;
import io.github.timtaran.modelengine.utils.Vector3dDeserializer;
import io.github.timtaran.modelengine.utils.Vector4dDeserializer;
import lombok.Getter;
import org.joml.Vector3d;
import org.joml.Vector4d;


@Getter
public class KeyframeAction {
  private ActionTypes type;
  private String umid;

  @JsonProperty("object_name")
  private String objectName;

  @JsonProperty("bone_name")
  private String boneName;

  @JsonDeserialize(using = Vector3dDeserializer.class)
  private Vector3d coords;

  @JsonDeserialize(using = Vector4dDeserializer.class)
  private Vector4d rotations;
}
