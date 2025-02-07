package io.github.timtaran.modelengine.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.timtaran.modelengine.Plugin;
import io.github.timtaran.modelengine.generators.PackGenerator;
import io.github.timtaran.modelengine.loaders.ModelLoader;
import io.github.timtaran.modelengine.objects.ModelObject;
import io.github.timtaran.modelengine.objects.blockbench.BbModelObject;
import io.github.timtaran.modelengine.objects.blockbench.GroupObject;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/** All commands for models such as `list`, `load`, etc. */
public class ModelCommands {
  private static void listModels(CommandSender sender, CommandArguments args) {
    final TextComponent textComponent =
        Component.text("All loaded models:\n")
            .append(
                Component.text(
                    String.join(", ", ModelLoader.getLoadedModels().keySet()),
                    NamedTextColor.GREEN));

    sender.sendMessage(textComponent);
  }

  private static void spawnModel(Player player, CommandArguments args) {
    String modelName = (String) args.get("model_name");

    ModelObject modelObject = ModelLoader.getLoadedModels().get(modelName);
    if (modelObject != null) {
      BbModelObject bbModelObject = modelObject.getBbModel();
      Location location = (Location) Objects.requireNonNull(args.get("location"));

      for (GroupObject group : bbModelObject.getAllGroups()) {
        player.sendMessage(
            Component.text("[DEV]", NamedTextColor.GREEN)
                .append(Component.text(" spawning " + group.name(), NamedTextColor.WHITE)));

        ItemDisplay itemDisplay =
            player
                .getWorld()
                .spawn(
                    location
                        .clone()
                        .add(
                            group.origin()[0] * -0.0625,
                            group.origin()[1] * 0.0625,
                            group.origin()[2]
                                * -0.0625), // Using negative multiply to create offset otherwise
                                            // model would be broken,
                    ItemDisplay.class);

        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setItemModel(new NamespacedKey("sme", group.name()));
        itemStack.setItemMeta(itemMeta);

        itemDisplay.setItemStack(itemStack);

        PersistentDataContainer dataContainer = itemDisplay.getPersistentDataContainer();
        dataContainer.set(
            new NamespacedKey(Plugin.plugin, "model_part_name"),
            PersistentDataType.STRING,
            group.name());
      }
    } else {
      player.sendMessage(Component.text("Model not found", NamedTextColor.RED));
    }
  }

  private static void reloadModels(CommandSender sender, CommandArguments ignoredArgs) {
    PackGenerator packGenerator = new PackGenerator();

    try {
      ModelLoader.unloadModels();
      ModelLoader.loadModels();
      packGenerator.generatePack();
    } catch (Exception e) {
      sender.sendMessage(Component.text("Failed to reload models", NamedTextColor.RED));
      Plugin.plugin.getComponentLogger().error("Error while reloading models", e);
    }
  }

  CommandAPICommand getCommand() {
    return new CommandAPICommand("model")
        .withSubcommand(
            new CommandAPICommand("list")
                .executes(ModelCommands::listModels)
                .withShortDescription("List all loaded models")
                .withUsage("/sme model list")
                .withPermission("sme.commands.model.list"))
        .withSubcommand(
            new CommandAPICommand("spawn")
                .withArguments(
                    new LocationArgument("location", LocationType.PRECISE_POSITION, true),
                    new GreedyStringArgument("model_name"))
                .executesPlayer(ModelCommands::spawnModel)
                .withUsage("/sme model spawn <model_name> <coords>")
                .withShortDescription("Spawn model (dev purposes only!!)")
                .withPermission("sme.commands.model.spawn"))
        .withSubcommand(
            new CommandAPICommand("reload")
                .executes(ModelCommands::reloadModels)
                .withShortDescription("Reload all models")
                .withUsage("/sme model reload")
                .withPermission("sme.commands.model.reload"))
        .withPermission("sme.commands.model");
  }
}
