package io.github.xaxisplayz.manager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public void setConfigItems(Inventory inventory) {
        ConfigurationSection itemsSection = getConfig().createSection("Items");

        getConfig().getKeys(false).forEach(key -> getConfig().set(key, null));
        saveConfig();

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                ConfigurationSection itemSection = itemsSection.createSection(String.valueOf(item.hashCode()));
                itemSection.set("item", item);
            }
        }

        saveConfig();
    }

    public List<ItemStack> getConfigItems() {
        List<ItemStack> items = new ArrayList<>();
        ConfigurationSection itemsSection = getConfig().getConfigurationSection("Items");

        if (itemsSection != null) {
            Set<String> keys = itemsSection.getKeys(false);

            for (String key : keys) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                ItemStack item = itemSection.getItemStack("item");
                items.add(item);
            }
        }

        return items;
    }

    public ItemStack getConfigItem(int hashCode) {
        ConfigurationSection itemsSection = getConfig().getConfigurationSection("Items");
        if (itemsSection != null && itemsSection.contains(String.valueOf(hashCode))) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(String.valueOf(hashCode));
            if (itemSection != null && itemSection.contains("item")) {
                return itemSection.getItemStack("item");
            }
        }
        return null;
    }

    public void setMessage(MessagePath messagePath, String message) {
        getConfig().set(messagePath.getPath(), message);
        saveConfig();
    }
}
