package io.github.timtaran.modelengine.models.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/** Face object class. */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
@Getter
public class TextureFace {
  private int[] uv;
  private int texture;
}
