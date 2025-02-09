package io.github.timtaran.modelengine.models;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/** Config object class. */
@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
@Getter
public class ConfigModel {
  private boolean generatePackOnLoad = true;
  private boolean loadModelsOnLoad = true;
}
