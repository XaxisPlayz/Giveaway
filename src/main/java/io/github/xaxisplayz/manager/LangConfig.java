package io.github.xaxisplayz.manager;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("deprecation")

public class LangConfig {

    private final Plugin plugin;
    private final String fileName;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    public LangConfig(Plugin plugin, String fileName) {
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

    public String getString(MessagePath path, Object... placeholders) {
        FileConfiguration config = getConfig();
        String message = config.getString(path.getPath(), path.getDefaultValue());

        if (placeholders != null && placeholders.length > 0) {
            message = String.format(message, placeholders);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void setMessage(MessagePath messagePath, String message) {
        getConfig().set(messagePath.getPath(), message);
        saveConfig();
    }
}
