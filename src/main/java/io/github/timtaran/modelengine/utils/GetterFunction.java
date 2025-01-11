package io.github.timtaran.modelengine.utils;

@FunctionalInterface
public interface GetterFunction<T> {
  String apply(T t);
}
