package io.github.timtaran.modelengine;

import io.github.timtaran.modelengine.commands.CommandManager;
import io.github.timtaran.modelengine.generators.PackGenerator;
import io.github.timtaran.modelengine.loaders.ConfigLoader;
import io.github.timtaran.modelengine.loaders.ModelLoader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

/** Main class, wow, fuck checkstyle. */
public class Plugin extends JavaPlugin implements Listener {
  public static Plugin plugin;
  public static ScheduledExecutorService executorService;

  public static Path modelsPath;
  public static Path packPath;

  @Override
  public void onEnable() {
    plugin = this;
    executorService = Executors.newSingleThreadScheduledExecutor();

    modelsPath = getDataPath().resolve("models/");
    packPath = getDataPath().resolve("pack/");

    Bukkit.getPluginManager().registerEvents(this, this);

    createFolders();
    try {
      ConfigLoader.loadConfig();
    } catch (ConfigurateException e) {
      throw new RuntimeException("Failed to read config", e);
    }

    if (ConfigLoader.getConfig().isLoadModelsOnLoad()) {
      try {
        ModelLoader.loadModels();
      } catch (IOException e) {
        throw new RuntimeException("Failed to load models", e);
      }
    }

    if (ConfigLoader.getConfig().isGeneratePackOnLoad()) {
      try {
        new PackGenerator().generatePack();
      } catch (IOException | URISyntaxException e) {
        throw new RuntimeException("Failed to generate pack", e);
      }
    }

    new CommandManager().registerCommands();
  }

  private void createFolders() {
    try {
      Files.createDirectories(modelsPath);
      Files.createDirectories(packPath);
    } catch (IOException e) {
      getComponentLogger().warn("Failed to create directories", e);
    }
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));
  }
}
