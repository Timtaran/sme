package io.github.timtaran.modelengine.objects.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/** Resolution object class. */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
@Getter
public class ResolutionObject {
  private int width;
  private int height;
}
