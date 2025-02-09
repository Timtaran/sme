package io.github.timtaran.modelengine.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joml.Vector3d;
import java.io.IOException;

public class Vector3dDeserializer extends JsonDeserializer<Vector3d> {
  @Override
  public Vector3d deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    double[] values = p.readValueAs(double[].class);
    if (values.length != 3) {
      throw new IOException("Expected array of 3 elements for Vector3d");
    }
    return new Vector3d(values[0], values[1], values[2]);
  }
}