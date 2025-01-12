package io.github.timtaran.modelengine.utils;

/** Getter function interface. Used to execute function. */
@FunctionalInterface
public interface GetterFunction<T> {
  String apply(T t);
}
