package io.github.timtaran.modelengine;

import io.github.timtaran.modelengine.commands.CommandManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class, wow, fuck checkstyle.
 */
public class Plugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        new CommandManager().registerCommands();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(
                Component.text("Hello, " + event.getPlayer().getName() + "!"));
    }

}