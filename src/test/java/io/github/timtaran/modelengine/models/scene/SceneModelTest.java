package io.github.timtaran.modelengine.models.scene;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

public class SceneModelTest {
  @Test
  public void test() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    InputStream inputStream = ClassLoader.getSystemResourceAsStream("models/scene/scene1.json");
    Scene scene =
        mapper.readValue(
            inputStream,
            Scene.class);

    System.out.println(scene.getName());
    System.out.println();
    assert scene.getName().equals("test_anim_Scene");
    assert scene.getKeyframes().length == 2;

    assert scene.getKeyframes()[0].getFrame() == 1;
    assert scene.getKeyframes()[0].getActions().length == 2;
  }
}
