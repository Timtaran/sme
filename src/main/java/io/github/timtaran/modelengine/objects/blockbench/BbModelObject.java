package io.github.timtaran.modelengine.objects.blockbench;

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
public class BbModelObject {
  private MetaObject meta;

  private String name;
  private String parent;

  private ResolutionObject resolution;

  private TextureObject[] textures;
  private OutlinerObject[] outliner;
  private ElementObject[] elements;

  private boolean unusualResolution = false;
  private double uvMultiplier = 1;

  @Getter(AccessLevel.NONE)
  private Cache<Object, Object> cache =
      CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();

  private ArrayList<GroupObject> allGroups;

  @JsonSetter("resolution")
  private void setResolution(ResolutionObject resolution) {
    this.resolution = resolution;

    if (resolution.getWidth() != 16 || resolution.getHeight() != 16) {
      unusualResolution = true;

      uvMultiplier = 16.0 / resolution.getWidth(); // not our problem if texture is not squared fr
    }
  }

  private void processOutliner(OutlinerObject[] outliner, String group, double[] offset) {
    boolean isObjectsInside = false;

    for (OutlinerObject outlinerObject : outliner) {
      if (!outlinerObject.isObject()) {
        processOutliner(
            outlinerObject.getChildren(),
            group + "_" + outlinerObject.getName(),
            outlinerObject.getOrigin());
      } else isObjectsInside = true;
    }

    if (isObjectsInside) {
      allGroups.add(new GroupObject(group, offset));
    }
  }

  /**
   * All model's groups list of {@link GroupObject}
   *
   * @return List of all model's groups
   */
  public List<GroupObject> getAllGroups() {
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
   * Uses {@link BbModelObject#generateObjectsHashMap(GetterFunction, Object[], String)}.
   *
   * @return {@link HashMap} {@link ElementObject#getUuid()} -> {@link ElementObject}
   */
  public HashMap<String, ElementObject> getElementsAsUuidHashMap() {
    return generateObjectsHashMap(ElementObject::getUuid, elements, "elements_uuid");
  }

  /**
   * Uses {@link BbModelObject#generateObjectsHashMap(GetterFunction, Object[], String)}.
   *
   * @return {@link HashMap} {@link TextureObject#getUuid()} -> {@link TextureObject}
   */
  public HashMap<String, TextureObject> getTexturesAsUuidHashMap() {
    return generateObjectsHashMap(TextureObject::getUuid, textures, "textures_uuid");
  }

  /**
   * Uses {@link BbModelObject#generateObjectsHashMap(GetterFunction, Object[], String)}.
   *
   * @return {@link HashMap} {@link TextureObject#getId()} -> {@link TextureObject}
   */
  public HashMap<String, TextureObject> getTexturesAsIdHashMap() {
    return generateObjectsHashMap(TextureObject::getId, textures, "textures_id");
  }

  /**
   * @return Is model `java_block` or not
   */
  public boolean isJavaModel() {
    return meta.getModelFormat().equals("java_block");
  }
}
