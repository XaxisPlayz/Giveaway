package io.github.xaxisplayz.config;

import io.github.xaxisplayz.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class CustomConfig {

    private final Main plugin;
    private final String fileName;
    private final File file;
    private FileConfiguration fileConfig;

    public CustomConfig(Main plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
        createIfNotExists();
    }

    public void reload() {
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
        InputStream defaultConfigStream = plugin.getResource(fileName);
        if (defaultConfigStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigStream);
            this.fileConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        return this.fileConfig;
    }

    public void save() {
        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createIfNotExists() {
        if (!file.exists()) {
            plugin.getLogger().info("Generating " + fileName + " file...");
            plugin.saveResource(fileName, false);
        }
    }

    public void set(String path, Object value) {
        getConfig().set(path, value);
    }

    public String getString(String path) {
        return getConfig().getString(path);
    }

    public int getInt(String path) {
        return getConfig().getInt(path);
    }

    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public double getDouble(String path) {
        return getConfig().getDouble(path);
    }

    public void createSection(String path) {
        getConfig().createSection(path);
    }

    public boolean contains(String path) {
        return getConfig().contains(path);
    }

    public void remove(String path) {
        getConfig().set(path, null);
    }
}
