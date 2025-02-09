package io.github.timtaran.modelengine.models.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/** Resolution object class. */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
@Getter
public class ModelResolution {
  private int width;
  private int height;
}
