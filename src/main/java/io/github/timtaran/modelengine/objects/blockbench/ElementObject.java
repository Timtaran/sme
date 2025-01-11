package io.github.timtaran.modelengine.objects.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"unused", "FieldMayBeFinal"})
@Getter
public class ElementObject {
  private String name;
  private String uuid;
  private int[] from;
  private int[] to;
  private int[] rotation = {0, 0, 0};
  private int[] origin;

  private HashMap<String, FaceObject> faces;
}
