package io.github.xaxisplayz.utils;

import io.github.xaxisplayz.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomConfig {

    private final Plugin plugin;
    private final String fileName;
    private File configFile;
    private FileConfiguration fileConfiguration;

    public enum MessagePath {
        GIVEAWAY_STARTED("messages.giveaway.started", "&aA giveaway has started! &7&nClick here to enter!"),
        GIVEAWAY_ENDING_SOON("messages.giveaway.ending_soon", "&aGiveaway ends in 10 seconds! &7&nClick here to enter!"),
        GIVEAWAY_NO_ONGOING_GIVEAWAYS("messages.giveaway.no_ongoing_giveaways", "&4There is no ongoing giveaways right now!"),
        GIVEAWAY_ALREADY_ENTERED("messages.giveaway.already_entered", "&4You are already in this giveaway!"),
        GIVEAWAY_ENTERED("messages.giveaway.entered", "&aYou have entered the giveaway. Winners are announced in {time} second(s)");

        private final String path;
        private final String defaultValue;

        MessagePath(String path, String defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }

        public String getPath() {
            return path;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    public CustomConfig(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.configFile = new File(plugin.getDataFolder(), fileName);

        createFile();
    }

    public void createFile() {
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
                plugin.getLogger().info("Created " + fileName + " file.");
            }

            fileConfiguration = new YamlConfiguration();
            fileConfiguration.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Add missing default values
        boolean hasChanged = false;
        for (MessagePath messagePath : MessagePath.values()) {
            if (!fileConfiguration.contains(messagePath.getPath())) {
                fileConfiguration.set(messagePath.getPath(), messagePath.getDefaultValue());
                hasChanged = true;
            }
        }

        if (hasChanged) {
            try {
                fileConfiguration.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    public void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
    }

    public String getMessage(MessagePath messagePath) {
        return getConfig().getString(messagePath.getPath());
    }

    public void setMessage(MessagePath messagePath, String message) {
        getConfig().set(messagePath.getPath(), message);
        saveConfig();
    }
}
