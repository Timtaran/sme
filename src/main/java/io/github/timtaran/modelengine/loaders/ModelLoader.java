package io.github.timtaran.modelengine.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.timtaran.modelengine.Plugin;
import io.github.timtaran.modelengine.objects.ModelObject;
import io.github.timtaran.modelengine.objects.blockbench.BbModelObject;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;
import lombok.Getter;

/** Model loader class. */
public class ModelLoader {
  @Getter private static final HashMap<String, ModelObject> loadedModels = new HashMap<>();

  /**
   * Loads all .bbmodel files in the plugin/PluginName models folder.
   *
   * @throws IOException Throws IOException when failed to {@link Files#walk(Path, int,
   *     FileVisitOption...)} the folder
   */
  public static void loadModels() throws IOException {
    loadModels(Plugin.modelsPath);
  }

  /**
   * Loads all .bbmodel files in the given folder.
   *
   * @throws IOException Throws IOException when failed to {@link Files#walk(Path, int,
   *     FileVisitOption...)} the folder
   */
  public static void loadModels(Path modelsPath) throws IOException {
    try (Stream<Path> paths = Files.walk(modelsPath)) {
      paths
          .filter(Files::isRegularFile)
          .forEach(
              filepath -> {
                try {
                  loadModel(filepath);
                } catch (Exception e) {
                  Plugin.plugin.getComponentLogger().info("Failed to load model {}", filepath, e);
                }
              });
    }
  }

  /** Clears {@link ModelLoader#loadedModels}. */
  public static void unloadModels() {
    ModelLoader.loadedModels.clear();
  }

  /**
   * Converts filename into {@link InputStream} and loads it via {@link #loadModel(InputStream)}.
   *
   * @param filename filename to be converted into {@link InputStream} and loaded
   * @return {@link ModelObject} loaded model
   * @throws IOException when failed to create {@link InputStream} or failed to read model data
   */
  public static ModelObject loadModel(Path filename) throws IOException {
    return loadModel(new BufferedInputStream(new FileInputStream(filename.toFile())));
  }

  /**
   * Loads model from given {@link InputStream}, adds it to {@link ModelLoader#loadedModels} and
   * returns.
   *
   * @param inputStream model data {@link InputStream}
   * @return {@link ModelObject} loaded model
   * @throws IOException when failed to read model data
   */
  public static ModelObject loadModel(InputStream inputStream) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    BbModelObject bbModelObject = mapper.readValue(inputStream, BbModelObject.class);
    ModelObject modelObject = new ModelObject(bbModelObject.getName(), bbModelObject);

    loadedModels.put(bbModelObject.getName(), modelObject);

    return new ModelObject(bbModelObject.getName(), bbModelObject);
  }
}
