package io.github.timtaran.modelengine.commands;

import dev.jorel.commandapi.CommandAPICommand;

/**
 * `dev` subcommand.
 */
public class DevCommands {
    CommandAPICommand getCommand() {
        return new CommandAPICommand("dev")
                .withPermission("sme.commands.dev");
    }
}
