package io.github.timtaran.modelengine.commands;

import dev.jorel.commandapi.CommandAPICommand;

/** `dev` subcommand. */
public class DevCommands {
  CommandAPICommand getCommand() {
    return new CommandAPICommand("dev")
        .executes(
            (sender, args) -> {
              sender.sendMessage("Nothing here atm");
            })
        .withPermission("sme.commands.dev");
  }
}
