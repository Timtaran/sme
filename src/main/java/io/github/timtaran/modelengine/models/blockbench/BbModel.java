package io.github.timtaran.modelengine.models.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.timtaran.modelengine.utils.GetterFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;

/** Blockbench model object. */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"unused", "FieldMayBeFinal"})
@Getter
public class BbModel {
  private ModelMeta meta;

  private String name;
  private String parent;

  private ModelResolution resolution;

  private ModelTexture[] textures;
  private ModelOutliner[] outliner;
  private ModelElement[] elements;

  private boolean unusualResolution = false;
  private double uvMultiplier = 1;

  @Getter(AccessLevel.NONE)
  private Cache<Object, Object> cache =
      CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();

  private ArrayList<ModelGroup> allGroups;

  @JsonSetter("resolution")
  private void setResolution(ModelResolution resolution) {
    this.resolution = resolution;

    if (resolution.getWidth() != 16 || resolution.getHeight() != 16) {
      unusualResolution = true;

      uvMultiplier = 16.0 / resolution.getWidth(); // not our problem if texture is not squared fr
    }
  }

  private void processOutliner(ModelOutliner[] outliner, String bone_name, double[] offset) {
    boolean isObjectsInside = false;

    for (ModelOutliner outlinerObject : outliner) {
      if (!outlinerObject.isObject()) {
        processOutliner(
            outlinerObject.getChildren(),
            outlinerObject.getName(),
            outlinerObject.getOrigin());
      } else {
        isObjectsInside = true;
      }
    }

    if (isObjectsInside) {
      allGroups.add(new ModelGroup(name + "_" + bone_name, offset));
    }
  }

  /**
   * All model's groups list of {@link ModelGroup}.
   *
   * @return List of all model's groups
   */
  public List<ModelGroup> getAllGroups() {
    if (allGroups == null) {
      allGroups = new ArrayList<>();
      processOutliner(outliner, name, new double[] {0, 0, 0});
    }

    return Collections.unmodifiableList(allGroups);
  }

  /**
   * Converts array of some objects into {@link HashMap} and caches result.
   *
   * @return {@link HashMap} lombok(?) func -> {@link HashMap}
   */
  private <T> HashMap<String, T> generateObjectsHashMap(
      GetterFunction<T> getterFunction, T[] instance, String key) {
    Object value = cache.getIfPresent(key);
    if (value == null) {
      HashMap<String, T> tempHashMap = new HashMap<>();

      for (T object : instance) {
        tempHashMap.put(getterFunction.apply(object), object);
      }

      value = tempHashMap;
      cache.put(key, tempHashMap);
    }

    //noinspection unchecked
    return (HashMap<String, T>) value;
  }

  /**
   * Uses {@link BbModel#generateObjectsHashMap(GetterFunction, Object[], String)}.
   *
   * @return {@link HashMap} {@link ModelElement#getUuid()} -> {@link ModelElement}
   */
  public HashMap<String, ModelElement> getElementsAsUuidHashMap() {
    return generateObjectsHashMap(ModelElement::getUuid, elements, "elements_uuid");
  }

  /**
   * Uses {@link BbModel#generateObjectsHashMap(GetterFunction, Object[], String)}.
   *
   * @return {@link HashMap} {@link ModelTexture#getUuid()} -> {@link ModelTexture}
   */
  public HashMap<String, ModelTexture> getTexturesAsUuidHashMap() {
    return generateObjectsHashMap(ModelTexture::getUuid, textures, "textures_uuid");
  }

  /**
   * Uses {@link BbModel#generateObjectsHashMap(GetterFunction, Object[], String)}.
   *
   * @return {@link HashMap} {@link ModelTexture#getId()} -> {@link ModelTexture}
   */
  public HashMap<String, ModelTexture> getTexturesAsIdHashMap() {
    return generateObjectsHashMap(ModelTexture::getId, textures, "textures_id");
  }

  /**
   * Checks is model `java_block` or not.
   *
   * @return Is model `java_block` or not
   */
  public boolean isJavaModel() {
    return meta.getModelFormat().equals("java_block");
  }
}
