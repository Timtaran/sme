package io.github.timtaran.modelengine.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.timtaran.modelengine.loaders.ModelLoader;
import io.github.timtaran.modelengine.objects.ModelObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

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

  private static void spawnModel(CommandSender sender, CommandArguments args) {
    String modelName = (String) args.get("model_name");

    ModelObject modelObject = ModelLoader.getLoadedModels().get(modelName);
    if (modelObject != null) {
      sender.sendMessage("Work in progress...");
    } else {
      sender.sendMessage(Component.text("Model not found", NamedTextColor.RED));
    }
  }

  CommandAPICommand getCommand() {
    return new CommandAPICommand("model")
        .withSubcommand(
            new CommandAPICommand("list")
                .executes(ModelCommands::listModels)
                .withPermission("sme.commands.model.list"))
        .withSubcommand(
            new CommandAPICommand("spawn")
                .withArguments(new GreedyStringArgument("model_name"))
                .executes(ModelCommands::spawnModel)
                .withPermission("sme.commands.model.spawn"))
        .withPermission("sme.commands.model");
  }
}
