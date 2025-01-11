package io.github.timtaran.modelengine.commands;

import dev.jorel.commandapi.CommandAPICommand;

/** Manages all commands. */
public class CommandManager {

  /** Registering all commands. */
  public void registerCommands() {
    new CommandAPICommand("sme")
        .withPermission("sme.commands")
        .withSubcommand(new DevCommands().getCommand())
        .withSubcommand(new ModelCommands().getCommand())
        .register("simple_model_engine");
  }
}
