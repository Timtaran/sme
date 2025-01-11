package io.github.timtaran.modelengine.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jorel.commandapi.CommandAPICommand;
import io.github.timtaran.modelengine.Plugin;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;

/** `dev` subcommand. */
public class DevCommands {
  private final Map<String, Map<String, Map<String, Map<String, Double>>>> animation;
  private int tick = 0;
  private ItemDisplay globalItemDisplay;

  public DevCommands() {
    BukkitScheduler scheduler = Plugin.plugin.getServer().getScheduler();

    animation =
        loadAnimation(
            "{\"ticks\": {\"1\": {\"location\": {\"x\": 0.5, \"y\": 34.99456787109375, \"z\": 0.5}, \"rotation\": {\"axis\": 0.15894417464733124, \"x\": 0.7681571841239929, \"y\": -0.29925602674484253, \"z\": 0.5660290718078613}}, \"2\": {\"location\": {\"x\": 0.5, \"y\": 34.97928237915039, \"z\": 0.5}, \"rotation\": {\"axis\": 0.35622772574424744, \"x\": 0.788798987865448, \"y\": -0.26612797379493713, \"z\": 0.5540560483932495}}, \"3\": {\"location\": {\"x\": 0.5, \"y\": 34.95566177368164, \"z\": 0.5}, \"rotation\": {\"axis\": 0.5652176737785339, \"x\": 0.7959273457527161, \"y\": -0.23220466077327728, \"z\": 0.5590887069702148}}, \"4\": {\"location\": {\"x\": 0.5, \"y\": 34.92522048950195, \"z\": 0.5}, \"rotation\": {\"axis\": 0.7772257328033447, \"x\": 0.7980063557624817, \"y\": -0.1975642889738083, \"z\": 0.5693454146385193}}, \"5\": {\"location\": {\"x\": 0.5, \"y\": 34.889469146728516, \"z\": 0.5}, \"rotation\": {\"axis\": 0.9853852391242981, \"x\": 0.7967000603675842, \"y\": -0.1639929562807083, \"z\": 0.5817013382911682}}, \"6\": {\"location\": {\"x\": 0.5, \"y\": 34.84992980957031, \"z\": 0.5}, \"rotation\": {\"axis\": 1.1808215379714966, \"x\": 0.7920284867286682, \"y\": -0.13441166281700134, \"z\": 0.5955034494400024}}, \"7\": {\"location\": {\"x\": 0.5, \"y\": 34.8081169128418, \"z\": 0.5}, \"rotation\": {\"axis\": 1.345918893814087, \"x\": 0.7822906970977783, \"y\": -0.1156691163778305, \"z\": 0.6120800375938416}}, \"8\": {\"location\": {\"x\": 0.5, \"y\": 34.76554870605469, \"z\": 0.5}, \"rotation\": {\"axis\": 1.4694465398788452, \"x\": 0.7681610584259033, \"y\": -0.11180702596902847, \"z\": 0.6304187178611755}}, \"9\": {\"location\": {\"x\": 0.5, \"y\": 34.72373580932617, \"z\": 0.5}, \"rotation\": {\"axis\": 1.5710111856460571, \"x\": 0.754977285861969, \"y\": -0.1138504296541214, \"z\": 0.6457920074462891}}, \"10\": {\"location\": {\"x\": 0.5, \"y\": 34.68419647216797, \"z\": 0.5}, \"rotation\": {\"axis\": 1.656258225440979, \"x\": 0.7433826923370361, \"y\": -0.11968091130256653, \"z\": 0.6580718159675598}}, \"11\": {\"location\": {\"x\": 0.5, \"y\": 34.64844512939453, \"z\": 0.5}, \"rotation\": {\"axis\": 1.7284865379333496, \"x\": 0.7334043979644775, \"y\": -0.12802404165267944, \"z\": 0.6676285266876221}}, \"12\": {\"location\": {\"x\": 0.5, \"y\": 34.618003845214844, \"z\": 0.5}, \"rotation\": {\"axis\": 1.7899987697601318, \"x\": 0.7248805165290833, \"y\": -0.13794034719467163, \"z\": 0.6749227643013}}, \"13\": {\"location\": {\"x\": 0.5, \"y\": 34.594383239746094, \"z\": 0.5}, \"rotation\": {\"axis\": 1.8426196575164795, \"x\": 0.7176069021224976, \"y\": -0.1486455351114273, \"z\": 0.6804004907608032}}, \"14\": {\"location\": {\"x\": 0.5, \"y\": 34.579097747802734, \"z\": 0.5}, \"rotation\": {\"axis\": 1.887927532196045, \"x\": 0.7113942503929138, \"y\": -0.15943320095539093, \"z\": 0.6844700574874878}}, \"15\": {\"location\": {\"x\": 0.5, \"y\": 34.573665618896484, \"z\": 0.5}, \"rotation\": {\"axis\": 1.9273674488067627, \"x\": 0.7060912251472473, \"y\": -0.16963635385036469, \"z\": 0.6875017285346985}}, \"16\": {\"location\": {\"x\": 0.5, \"y\": 34.579097747802734, \"z\": 0.5}, \"rotation\": {\"axis\": 1.9623135328292847, \"x\": 0.7015911340713501, \"y\": -0.17860522866249084, \"z\": 0.6898333430290222}}, \"17\": {\"location\": {\"x\": 0.5, \"y\": 34.594383239746094, \"z\": 0.5}, \"rotation\": {\"axis\": 1.9941060543060303, \"x\": 0.6978298425674438, \"y\": -0.18569457530975342, \"z\": 0.6917738318443298}}, \"18\": {\"location\": {\"x\": 0.5, \"y\": 34.618003845214844, \"z\": 0.5}, \"rotation\": {\"axis\": 2.0240824222564697, \"x\": 0.694778323173523, \"y\": -0.1902543008327484, \"z\": 0.6936038732528687}}, \"19\": {\"location\": {\"x\": 0.5, \"y\": 34.64844512939453, \"z\": 0.5}, \"rotation\": {\"axis\": 2.0536022186279297, \"x\": 0.6924302577972412, \"y\": -0.19162240624427795, \"z\": 0.6955726742744446}}, \"20\": {\"location\": {\"x\": 0.5, \"y\": 34.68419647216797, \"z\": 0.5}, \"rotation\": {\"axis\": 2.084078073501587, \"x\": 0.6907860040664673, \"y\": -0.18911874294281006, \"z\": 0.6978889107704163}}, \"21\": {\"location\": {\"x\": 0.5, \"y\": 34.72373580932617, \"z\": 0.5}, \"rotation\": {\"axis\": 2.1170084476470947, \"x\": 0.6898345351219177, \"y\": -0.1820376068353653, \"z\": 0.7007071375846863}}, \"22\": {\"location\": {\"x\": 0.5, \"y\": 34.76554870605469, \"z\": 0.5}, \"rotation\": {\"axis\": 2.154021739959717, \"x\": 0.6895320415496826, \"y\": -0.16964036226272583, \"z\": 0.7041077017784119}}, \"23\": {\"location\": {\"x\": 0.5, \"y\": 34.8081169128418, \"z\": 0.5}, \"rotation\": {\"axis\": 2.196930408477783, \"x\": 0.689778745174408, \"y\": -0.1511414349079132, \"z\": 0.7080689072608948}}, \"24\": {\"location\": {\"x\": 0.5, \"y\": 34.84992980957031, \"z\": 0.5}, \"rotation\": {\"axis\": 2.247807025909424, \"x\": 0.6903954148292542, \"y\": -0.12568719685077667, \"z\": 0.712430477142334}}, \"25\": {\"location\": {\"x\": 0.5, \"y\": 34.889469146728516, \"z\": 0.5}, \"rotation\": {\"axis\": 2.2632315158843994, \"x\": 0.6922092437744141, \"y\": -0.05545928329229355, \"z\": 0.7195628881454468}}, \"26\": {\"location\": {\"x\": 0.5, \"y\": 34.92522048950195, \"z\": 0.5}, \"rotation\": {\"axis\": 2.2306928634643555, \"x\": 0.6888381838798523, \"y\": 0.07103853672742844, \"z\": 0.7214258909225464}}, \"27\": {\"location\": {\"x\": 0.5, \"y\": 34.95566177368164, \"z\": 0.5}, \"rotation\": {\"axis\": 2.1786458492279053, \"x\": 0.672421395778656, \"y\": 0.22597606480121613, \"z\": 0.7048293352127075}}, \"28\": {\"location\": {\"x\": 0.5, \"y\": 34.97928237915039, \"z\": 0.5}, \"rotation\": {\"axis\": 2.122553586959839, \"x\": 0.6406721472740173, \"y\": 0.3841559886932373, \"z\": 0.6648032665252686}}, \"29\": {\"location\": {\"x\": 0.5, \"y\": 34.99456787109375, \"z\": 0.5}, \"rotation\": {\"axis\": 2.0779075622558594, \"x\": 0.5999805331230164, \"y\": 0.5174185037612915, \"z\": 0.6101652979850769}}, \"30\": {\"location\": {\"x\": 0.5, \"y\": 35.0, \"z\": 0.5}, \"rotation\": {\"axis\": 2.094395160675049, \"x\": 0.5773502588272095, \"y\": 0.5773502588272095, \"z\": 0.5773502588272095}}}}");
    Plugin.executorService.scheduleAtFixedRate(
        () -> {
          tick++;

          if (globalItemDisplay != null) {
            scheduler.runTaskLater(Plugin.plugin, () -> playAnimation(globalItemDisplay), 0);
          }
        },
        50,
        50,
        TimeUnit.MILLISECONDS);
  }

  CommandAPICommand getCommand() {
    return new CommandAPICommand("dev")
        .withSubcommands(action1Command())
        .withSubcommands(clearCommand())
        .withPermission("sme.commands.dev");
  }

  private CommandAPICommand clearCommand() {
    return new CommandAPICommand("clear")
        .executes(
            (sender, args) -> {
              tick = 0;

              globalItemDisplay.remove();
              globalItemDisplay = null;
            });
  }

  private CommandAPICommand action1Command() {
    return new CommandAPICommand("action1")
        .executesPlayer(
            (player, args) -> {
              for (ItemDisplay itemDisplay :
                  player.getWorld().getEntitiesByClass(ItemDisplay.class)) {

                PersistentDataContainer dataContainer = itemDisplay.getPersistentDataContainer();

                Plugin.plugin
                    .getComponentLogger()
                    .info(
                        dataContainer.get(
                            new NamespacedKey(Plugin.plugin, "model_part_name"),
                            PersistentDataType.STRING));

                if (dataContainer.get(
                        new NamespacedKey(Plugin.plugin, "model_part_name"),
                        PersistentDataType.STRING)
                    != null) {
                  itemDisplay.remove();
                }
              }

              ItemDisplay itemDisplay =
                  player
                      .getWorld()
                      .spawn(new Location(player.getWorld(), 0, -512, 0, 0, 0), ItemDisplay.class);

              ItemStack itemStack = new ItemStack(Material.STICK);
              ItemMeta itemMeta = itemStack.getItemMeta();
              itemMeta.setItemModel(new NamespacedKey("sme", ""));
              itemStack.setItemMeta(itemMeta);

              itemDisplay.setItemStack(ItemStack.of(Material.STICK));

              PersistentDataContainer dataContainer = itemDisplay.getPersistentDataContainer();
              dataContainer.set(
                  new NamespacedKey(Plugin.plugin, "model_part_name"),
                  PersistentDataType.STRING,
                  "test");

              globalItemDisplay = itemDisplay;
            });
  }

  private Map<String, Map<String, Map<String, Map<String, Double>>>> loadAnimation(String data) {
    try {
      /* {
       *   "ticks": {
       *    "n": {
       *      "location": {"x": 9.11, ...}
       *      "rotation: {"x": 9.11, ...}
       *      }
       *    }
       * }
       */
      /*
       *  "ticks"    "n"       "loc, rot"    "data"
       *  ↓           ↓            ↓           ↓
       * Map<String, Map<String, Map<String, Map<String, Float>>>>
       */

      //noinspection unchecked
      return new ObjectMapper().readValue(data, HashMap.class);
    } catch (JsonProcessingException e) {
      //noinspection CallToPrintStackTrace
      e.printStackTrace();
      Plugin.plugin.getComponentLogger().trace(e.getMessage());
      return null;
    }
  }

  private void playAnimation(ItemDisplay itemDisplay) {
    Map<String, Map<String, Double>> tick_data =
        animation.get("ticks").get(String.valueOf(tick % 30 + 1));
    Map<String, Double> rotation = tick_data.get("rotation");
    Map<String, Double> location = tick_data.get("location");

    Location tpLocation = itemDisplay.getLocation();
    tpLocation.setX(location.get("x"));
    tpLocation.setY(location.get("y"));
    tpLocation.setZ(location.get("z"));

    itemDisplay.teleport(tpLocation);

    Transformation transformation = itemDisplay.getTransformation();
    AxisAngle4f leftRotation =
        new AxisAngle4f(
            rotation.get("axis").floatValue(),
            rotation.get("x").floatValue(),
            rotation.get("y").floatValue(),
            rotation.get("z").floatValue());
    transformation.getLeftRotation().set(leftRotation);
    itemDisplay.setTransformation(transformation);
  }
}
