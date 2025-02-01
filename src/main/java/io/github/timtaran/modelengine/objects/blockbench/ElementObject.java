package io.github.timtaran.modelengine.objects.blockbench;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import lombok.Getter;

/** Element object class. */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"unused", "FieldMayBeFinal"})
@Getter
public class ElementObject {
  private String name;
  private String uuid;
  private double[] from;
  private double[] to;
  private double[] rotation = {0, 0, 0};
  private double[] origin;

  private HashMap<String, FaceObject> faces;
}
