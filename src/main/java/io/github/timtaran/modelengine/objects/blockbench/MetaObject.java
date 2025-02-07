package io.github.timtaran.modelengine.objects.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/** Meta object class. */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"unused", "FieldMayBeFinal"})
@Getter
public class MetaObject {
  @JsonProperty("model_format")
  private String modelFormat;
}
