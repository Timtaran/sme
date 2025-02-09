package io.github.timtaran.modelengine.models.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/** Texture object class. */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
@Getter
public class ModelTexture {
  private String id;
  private String name;
  private String uuid;
  private String source;
}
