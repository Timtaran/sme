package io.github.timtaran.modelengine.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joml.Vector4d;
import java.io.IOException;

public class Vector4dDeserializer extends JsonDeserializer<Vector4d> {
  @Override
  public Vector4d deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    double[] values = p.readValueAs(double[].class);
    if (values.length != 4) {
      throw new IOException("Invalid Vector4d format, expected an array of 4 elements");
    }
    return new Vector4d(values[0], values[1], values[2], values[3]);
  }
}