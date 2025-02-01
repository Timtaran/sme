package io.github.timtaran.modelengine.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.timtaran.modelengine.Plugin;
import io.github.timtaran.modelengine.loaders.ModelLoader;
import io.github.timtaran.modelengine.objects.ModelObject;
import io.github.timtaran.modelengine.objects.blockbench.BbModelObject;
import io.github.timtaran.modelengine.objects.blockbench.ElementObject;
import io.github.timtaran.modelengine.objects.blockbench.FaceObject;
import io.github.timtaran.modelengine.objects.blockbench.OutlinerObject;
import io.github.timtaran.modelengine.objects.blockbench.TextureObject;
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

    HashMap<String, ModelObject> loadedModels = ModelLoader.getLoadedModels();
    for (String modelKey : loadedModels.keySet()) {
      ModelObject modelObject = loadedModels.get(modelKey);
      BbModelObject bbModelObject = modelObject.getBbModel();

      generateTextures(bbModelObject);
      generateModels(bbModelObject);
    }
  }

  private void generateTextures(BbModelObject bbModelObject) {
    Base64.Decoder decoder = Base64.getDecoder();

    for (TextureObject textureObject : bbModelObject.getTextures()) {
      if (!textureObject.getSource().startsWith("data:image/png;base64,")) {
        Plugin.plugin
            .getComponentLogger()
            .error(
                "Failed to load `{}` `{}` texture",
                bbModelObject.getName(),
                textureObject.getName());
        continue;
      }

      String base64Image = textureObject.getSource().split(",")[1];
      byte[] decodedBytes = decoder.decode(base64Image);

      try (FileOutputStream fileOutputStream =
          new FileOutputStream(texturesPath.resolve(textureObject.getName() + ".png").toString())) {
        fileOutputStream.write(decodedBytes);
      } catch (IOException e) {
        Plugin.plugin
            .getComponentLogger()
            .error(
                "Failed to save `{}` `{}` texture",
                bbModelObject.getName(),
                textureObject.getName(),
                e);
      }
    }
  }

  private void generateModels(BbModelObject bbModelObject) throws IOException {
    processOutliner(bbModelObject.getOutliner(), bbModelObject, new double[]{0, 0, 0}, "");
  }

  private void processOutliner(
      OutlinerObject[] outlinerObjects, BbModelObject bbModelObject, double[] offset, String fileName)
      throws IOException {
    if (bbModelObject.isJavaModel()) {
      offset = ArrayUtils.subtract(offset, new double[] {8, 0, 8});
    }

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode modelNode = objectMapper.createObjectNode();

    modelNode.put("credit", "Created using SimpleModelEngine by timtaran");
    ArrayNode resolutionNode = modelNode.putArray("resolution");
    resolutionNode.add(bbModelObject.getResolution().getWidth());
    resolutionNode.add(bbModelObject.getResolution().getHeight());

    ArrayNode elementsNode = modelNode.putArray("elements");

    HashMap<String, ElementObject> elements = bbModelObject.getElementsAsUuidHashMap();
    ObjectNode texturesNode = objectMapper.createObjectNode();
    JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    for (OutlinerObject outlinerObject : outlinerObjects) {
      if (outlinerObject.isObject()) {
        ElementObject elementObject = elements.get(outlinerObject.getUuid());
        ObjectNode elementNode = objectMapper.createObjectNode();
        elementNode.set("from", jsonNodeFactory.pojoNode(ArrayUtils.subtract(elementObject.getFrom(), offset)));
        elementNode.set("to", jsonNodeFactory.pojoNode(ArrayUtils.subtract(elementObject.getTo(), offset)));

        ObjectNode rotationNode = objectMapper.createObjectNode();
        double angle = 0;
        String axis = "y";

        double[] rotation = elementObject.getRotation();
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
        rotationNode.set("origin", jsonNodeFactory.pojoNode(ArrayUtils.subtract(elementObject.getFrom(), offset)));
        elementNode.set("rotation", rotationNode);

        ObjectNode facesNode = objectMapper.createObjectNode();

        HashMap<String, FaceObject> faces = elementObject.getFaces();
        System.out.println(faces);
        for (String faceName : faces.keySet()) {
          System.out.println(faceName);
          FaceObject faceObject = faces.get(faceName);
          ObjectNode faceNode = objectMapper.createObjectNode();

          if (bbModelObject.isUnusualResolution()) {
            ArrayNode faceArray = faceNode.putArray("uv");

            for (int pointLocation : faceObject.getUv()) {
              faceArray.add(pointLocation * bbModelObject.getUvMultiplier());
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
                    bbModelObject.getTexturesAsIdHashMap().get(textureId).getName()));
          }
          faceNode.put("texture", String.format("#%s", textureId));
          facesNode.set(faceName, faceNode);
        }
        elementNode.set("faces", facesNode);

        elementsNode.add(elementNode);
      } else {
        processOutliner(
            outlinerObject.getChildren(), bbModelObject, outlinerObject.getOrigin(), fileName + "_" + outlinerObject.getName());
      }
    }

    modelNode.set("textures", texturesNode);

    ObjectNode itemNode = objectMapper.createObjectNode();
    ObjectNode itemModelNode = objectMapper.createObjectNode();
    itemModelNode.put("type", "minecraft:model");
    itemModelNode.put("model", "sme:item/" + bbModelObject.getName() + fileName);

    itemNode.set("model", jsonNodeFactory.pojoNode(itemModelNode));

    if (!elementsNode.isEmpty()) {
      objectMapper
          .writer()
          .writeValue(
              new File(
                  modelsPath
                      .resolve(String.format("%s%s.json", bbModelObject.getName(), fileName))
                      .toString()),
              modelNode);

      objectMapper
          .writer()
          .writeValue(
              new File(
                  itemsPath
                      .resolve(String.format("%s%s.json", bbModelObject.getName(), fileName))
                      .toString()),
              itemNode);
    }
  }
}
