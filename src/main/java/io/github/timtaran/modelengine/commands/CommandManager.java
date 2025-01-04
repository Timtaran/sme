package io.github.timtaran.modelengine.commands;

import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;

/**
 * Manages all commands.
 */
public class CommandManager {

    /**
     * Registering all commands.
     */
    public void registerCommands() {
        new CommandAPICommand("sme")
                .withPermission("sme.commands")
                .executes((sender, args) -> {
                    sender.sendMessage(Component.text("In development..."));
                })
                .withSubcommand(new DevCommands().getCommand())
                .register("simple_model_engine");

    }
}
