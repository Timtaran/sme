package io.github.timtaran.modelengine.objects.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonSetter;
import io.github.timtaran.modelengine.utils.GetterFunction;
import lombok.AccessLevel;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"unused", "FieldMayBeFinal"})
@Getter
public class BbModelObject {
  private String name;
  private String parent;

  private ResolutionObject resolution;

  private TextureObject[] textures;
  private OutlinerObject[] outliner;
  private ElementObject[] elements;

  private boolean unusualResolution = false;
  private double uvMultiplier = 1;

  @JsonSetter("resolution")
  private void setResolution(ResolutionObject resolution) {
    this.resolution = resolution;

    if (resolution.getWidth() != 16 || resolution.getHeight() != 16) {
      unusualResolution = true;

      uvMultiplier = 16.0 / resolution.getWidth(); // not our problem if texture is not squared fr
    }
  }

  @Getter(AccessLevel.NONE)
  private final HashMap<Object, Object> cachedHashMap = new HashMap<>();

  /**
   * Converts array of some objects into {@link HashMap<>} and caches result
   *
   * @return {@link HashMap<>} lombok(?) func -> {@link HashMap<>}
   */
  private <T> HashMap<String, T> generateObjectsHashMap(GetterFunction<T> getterFunction, T[] instance, String key) {
    Object value = cachedHashMap.get(key);
    if (value == null) {
      HashMap<String, T> tempHashMap = new HashMap<>();

      for (T object : instance) {
        tempHashMap.put(getterFunction.apply(object), object);
      }

      value = tempHashMap;
      cachedHashMap.put(key, tempHashMap);
    }

    //noinspection unchecked
    return (HashMap<String, T>) value;
  }

  /**
   * {@link BbModelObject#generateObjectsHashMap(GetterFunction, Object[], String)}
   * @return {@link HashMap<>} {@link ElementObject#getUuid()} -> {@link ElementObject}
   */
  public HashMap<String, ElementObject> getElementsAsUuidHashMap() {
    return generateObjectsHashMap(ElementObject::getUuid, elements, "elements_uuid");
  }

  /**
   * {@link BbModelObject#generateObjectsHashMap(GetterFunction, Object[], String)}
   * @return {@link HashMap<>} {@link TextureObject#getUuid()} -> {@link TextureObject}
   */
  public HashMap<String, TextureObject> getTexturesAsUuidHashMap() {
    return generateObjectsHashMap(TextureObject::getUuid, textures, "textures_uuid");
  }

  /**
   * {@link BbModelObject#generateObjectsHashMap(GetterFunction, Object[], String)}
   * @return {@link HashMap<>} {@link TextureObject#getId()} -> {@link TextureObject}
   */
  public HashMap<String, TextureObject> getTexturesAsIdHashMap() {
    return generateObjectsHashMap(TextureObject::getId, textures, "textures_id");
  }
}
