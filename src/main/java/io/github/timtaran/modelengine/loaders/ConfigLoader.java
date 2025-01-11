package io.github.timtaran.modelengine.loaders;

import io.github.timtaran.modelengine.Plugin;
import io.github.timtaran.modelengine.objects.ConfigObject;
import lombok.Getter;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

public class ConfigLoader {
  @Getter private static ConfigObject config;

  public static void loadConfig() throws ConfigurateException {
    Plugin.plugin.saveDefaultConfig();
    Path configPath = Plugin.plugin.getDataPath().resolve("config.yml");

    final YamlConfigurationLoader loader =
        YamlConfigurationLoader.builder().path(configPath).build();

    CommentedConfigurationNode node = loader.load();
    config = node.get(ConfigObject.class);
  }
}
