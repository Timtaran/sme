package io.github.timtaran.modelengine.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.timtaran.modelengine.Plugin;
import io.github.timtaran.modelengine.generators.PackGenerator;
import io.github.timtaran.modelengine.loaders.ModelLoader;
import io.github.timtaran.modelengine.objects.ModelObject;
import io.github.timtaran.modelengine.objects.blockbench.BbModelObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
      Location spawnLocation = player.getLocation();
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
                .withArguments(new GreedyStringArgument("model_name"))
                .executesPlayer(ModelCommands::spawnModel)
                .withUsage("/sme model spawn <model_name>")
                .withShortDescription("Spawn model at player location (dev purposes only!!)")
                .withPermission("sme.commands.model.spawn"))
            .withSubcommand(
                new CommandAPICommand("reload")
                    .executes(ModelCommands::reloadModels)
                    .withShortDescription("Reload all models")
                    .withUsage("/sme model reload")
                    .withPermission("sme.commands.model.reload")
            )
        .withPermission("sme.commands.model");
  }
}
