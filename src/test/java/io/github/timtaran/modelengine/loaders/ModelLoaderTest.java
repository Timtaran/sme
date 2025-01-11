package io.github.timtaran.modelengine.loaders;

import io.github.timtaran.modelengine.objects.ModelObject;
import io.github.timtaran.modelengine.objects.blockbench.BbModelObject;
import io.github.timtaran.modelengine.objects.blockbench.ElementObject;
import io.github.timtaran.modelengine.objects.blockbench.OutlinerObject;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ModelLoaderTest {
  @Test
  public void test() throws Exception {
    ModelObject model =
        ModelLoader.loadModel(
            ClassLoader.getSystemResourceAsStream("loaders/model_loader/test.bbmodel"));
    BbModelObject bbModel = model.getBbModel();

    assert bbModel.getName().equals("test");
    assert bbModel.getOutliner().length == 1;

    OutlinerObject outlinerObject0 = bbModel.getOutliner()[0];

    assert outlinerObject0.getName().equals("main");
    assert outlinerObject0.getChildren().length == 3;
    assert outlinerObject0
        .getChildren()[2]
        .getUuid()
        .equals("41b43408-fbdc-9563-770b-da33cfc8b350");

    assert bbModel.getElements().length == 3;

    ElementObject elementObject2 = bbModel.getElements()[2];
    assert Arrays.equals(elementObject2.getFaces().get("east").getUv(), new int[] {14, 32, 16, 39});
    assert elementObject2.getFaces().get("south").getTexture() == 0;

    assert bbModel.getTextures().length == 1;

    assert bbModel.getElementsAsUuidHashMap().get("e773a194-e877-5c33-faf6-bf06deec370f").equals(elementObject2);
    assert bbModel.getTexturesAsUuidHashMap().get("44aa83df-e1a3-43aa-3d5f-5a16f3ba1284").equals(bbModel.getTextures()[0]);
  }
}
