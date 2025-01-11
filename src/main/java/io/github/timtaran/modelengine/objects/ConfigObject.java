package io.github.timtaran.modelengine.objects;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("FieldMayBeFinal")
@Getter
public class ConfigObject {
  private boolean generatePackOnLoad = true;
  private boolean loadModelsOnLoad = true;
}
