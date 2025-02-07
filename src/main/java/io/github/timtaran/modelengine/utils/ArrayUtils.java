package io.github.timtaran.modelengine.utils;

import java.util.stream.IntStream;

/** Utility class for working with arrays. */
public class ArrayUtils {

  /** Subtracts one array from another. */
  public static double[] subtract(double[] array1, double[] array2) {
    return IntStream.range(0, array1.length).mapToDouble(i -> array1[i] - array2[i]).toArray();
  }
}
