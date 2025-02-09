package io.github.timtaran.modelengine.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.timtaran.modelengine.Plugin;
import io.github.timtaran.modelengine.loaders.ModelLoader;
import io.github.timtaran.modelengine.models.Model;
import io.github.timtaran.modelengine.models.blockbench.BbModel;
import io.github.timtaran.modelengine.models.blockbench.ModelElement;
import io.github.timtaran.modelengine.models.blockbench.TextureFace;
import io.github.timtaran.modelengine.models.blockbench.ModelOutliner;
import io.github.timtaran.modelengine.models.blockbench.ModelTexture;
import io.github.timtaran.modelengine.utils.ArrayUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

/**
 * Pack generator class. Saves at pack folder with `sme` namespace and can be used via
 * `minecraft:item_mode` variable.
 */
public class PackGenerator {
  private final Path basePath; // for testing purposes
  private Path namespacePath;
  private Path itemsPath;
  private Path modelsPath;
  private Path texturesPath;

  public PackGenerator() {
    this.basePath = Plugin.packPath;
  }

  public PackGenerator(Path basePath) {
    this.basePath = basePath;
  }

  /**
   * Generate pack with all models from loadedModels. Saves at pack folder with `sme` namespace and
   * can be used via * `minecraft:item_mode` variable.
   */
  public void generatePack() throws URISyntaxException, IOException {
    if (!basePath.resolve("pack.mcmeta").toFile().exists()) {
      Files.copy(
          Objects.requireNonNull(
              PackGenerator.class.getClassLoader().getResourceAsStream("pack/pack.mcmeta")),
          basePath.resolve("pack.mcmeta"));
    }

    namespacePath = basePath.resolve("assets/sme");
    itemsPath = namespacePath.resolve("items");
    modelsPath = namespacePath.resolve("models/item");
    texturesPath = namespacePath.resolve("textures/item");

    Files.createDirectories(namespacePath);
    Files.createDirectories(itemsPath);
    Files.createDirectories(modelsPath);
    Files.createDirectories(texturesPath);

    HashMap<String, Model> loadedModels = ModelLoader.getLoadedModels();
    for (String modelKey : loadedModels.keySet()) {
      Model model = loadedModels.get(modelKey);
      BbModel bbModel = model.getBbModel();

      generateTextures(bbModel);
      generateModels(bbModel);
    }
  }

  private void generateTextures(BbModel bbModel) {
    Base64.Decoder decoder = Base64.getDecoder();

    for (ModelTexture modelTexture : bbModel.getTextures()) {
      if (!modelTexture.getSource().startsWith("data:image/png;base64,")) {
        Plugin.plugin
            .getComponentLogger()
            .error(
                "Failed to load `{}` `{}` texture",
                bbModel.getName(),
                modelTexture.getName());
        continue;
      }

      String base64Image = modelTexture.getSource().split(",")[1];
      byte[] decodedBytes = decoder.decode(base64Image);

      try (FileOutputStream fileOutputStream =
          new FileOutputStream(texturesPath.resolve(modelTexture.getName() + ".png").toString())) {
        fileOutputStream.write(decodedBytes);
      } catch (IOException e) {
        Plugin.plugin
            .getComponentLogger()
            .error(
                "Failed to save `{}` `{}` texture",
                bbModel.getName(),
                modelTexture.getName(),
                e);
      }
    }
  }

  private void generateModels(BbModel bbModel) throws IOException {
    processOutliner(bbModel.getOutliner(), bbModel, new double[] {0, 0, 0}, "");
  }

  private void processOutliner(
      ModelOutliner[] outlinerObjects,
      BbModel bbModel,
      double[] offset,
      String fileName)
      throws IOException {

    if (!bbModel.isJavaModel()) {
      offset =
          ArrayUtils.subtract(offset, new double[] {8, 8, 8}); // blockbench auto offset java models
    }

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode modelNode = objectMapper.createObjectNode();

    modelNode.put("credit", "Created using SimpleModelEngine by timtaran");
    ArrayNode resolutionNode = modelNode.putArray("resolution");
    resolutionNode.add(bbModel.getResolution().getWidth());
    resolutionNode.add(bbModel.getResolution().getHeight());

    ArrayNode elementsNode = modelNode.putArray("elements");

    HashMap<String, ModelElement> elements = bbModel.getElementsAsUuidHashMap();
    ObjectNode texturesNode = objectMapper.createObjectNode();
    JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    for (ModelOutliner outlinerObject : outlinerObjects) {
      if (outlinerObject.isObject()) {
        ModelElement modelElement = elements.get(outlinerObject.getUuid());
        ObjectNode elementNode = objectMapper.createObjectNode();

        elementNode.set(
            "from", jsonNodeFactory.pojoNode(ArrayUtils.subtract(modelElement.getFrom(), offset)));
        elementNode.set(
            "to", jsonNodeFactory.pojoNode(ArrayUtils.subtract(modelElement.getTo(), offset)));

        ObjectNode rotationNode = objectMapper.createObjectNode();
        double angle = 0;
        String axis = "y";

        double[] rotation = modelElement.getRotation();
        if (!Arrays.equals(rotation, new double[] {0, 0, 0})) {
          for (int i = 0; i < 3; i++) {
            if (rotation[i] != 0) {
              angle = rotation[i];
              axis = "xyz".charAt(i) + "";
            }
          }
        }

        rotationNode.put("angle", angle);
        rotationNode.put("axis", axis);
        rotationNode.set(
            "origin",
            jsonNodeFactory.pojoNode(ArrayUtils.subtract(modelElement.getOrigin(), offset)));
        elementNode.set("rotation", rotationNode);

        ObjectNode facesNode = objectMapper.createObjectNode();

        HashMap<String, TextureFace> faces = modelElement.getFaces();
        for (String faceName : faces.keySet()) {
          TextureFace faceObject = faces.get(faceName);
          ObjectNode faceNode = objectMapper.createObjectNode();

          if (bbModel.isUnusualResolution()) {
            ArrayNode faceArray = faceNode.putArray("uv");

            for (int pointLocation : faceObject.getUv()) {
              faceArray.add(pointLocation * bbModel.getUvMultiplier());
            }
          } else {
            faceNode.set("uv", jsonNodeFactory.pojoNode(faceObject.getUv()));
          }

          String textureId = String.valueOf(faceObject.getTexture());
          if (texturesNode.get(textureId) == null) {
            texturesNode.put(
                textureId,
                String.format(
                    "sme:item/%s",
                    bbModel.getTexturesAsIdHashMap().get(textureId).getName()));
          }
          faceNode.put("texture", String.format("#%s", textureId));
          facesNode.set(faceName, faceNode);
        }
        elementNode.set("faces", facesNode);

        elementsNode.add(elementNode);
      } else {
        processOutliner(
            outlinerObject.getChildren(),
                bbModel,
            outlinerObject.getOrigin(),
            "_" + outlinerObject.getName());
      }
    }

    modelNode.set("textures", texturesNode);

    ObjectNode itemNode = objectMapper.createObjectNode();
    ObjectNode itemModelNode = objectMapper.createObjectNode();
    itemModelNode.put("type", "minecraft:model");
    itemModelNode.put("model", "sme:item/" + bbModel.getName() + fileName);

    itemNode.set("model", jsonNodeFactory.pojoNode(itemModelNode));

    if (!elementsNode.isEmpty()) {
      objectMapper
          .writer()
          .writeValue(
              new File(
                  modelsPath
                      .resolve(String.format("%s%s.json", bbModel.getName(), fileName))
                      .toString()),
              modelNode);

      objectMapper
          .writer()
          .writeValue(
              new File(
                  itemsPath
                      .resolve(String.format("%s%s.json", bbModel.getName(), fileName))
                      .toString()),
              itemNode);
    }
  }
}
