package io.github.timtaran.modelengine.objects.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
@Getter
public class TextureObject {
  private String id;
  private String name;
  private String uuid;
  private String source;
}
