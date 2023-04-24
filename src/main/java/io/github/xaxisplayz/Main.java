package io.github.xaxisplayz;

import io.github.xaxisplayz.commands.GiveawayItemsCommand;
import io.github.xaxisplayz.commands.JoinGiveawayCommand;
import io.github.xaxisplayz.commands.ManualGiveawayCommand;
import io.github.xaxisplayz.manager.GiveawayManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Main extends JavaPlugin {

    private static final Component GUI_TITLE = Component.text(ChatColor.GREEN + "Giveaway Items!");

    private BukkitTask task;
    private GiveawayManager giveawayManager;

    @Override
    public void onEnable() {
        getCommand("giveawayitems").setExecutor(new GiveawayItemsCommand(this));
        getCommand("joingiveaway").setExecutor(new JoinGiveawayCommand(this));
        getCommand("manualgiveaway").setExecutor(new ManualGiveawayCommand(this));

        saveDefaultConfig();
        long delay = 4L * 60L * 60L * 20L; // 4 hours
        Bukkit.getScheduler().runTaskLater(this, this::startGiveawayTask, delay);

        if(getConfig().get("Count") == null) {
            getConfig().set("Count", 0);
            saveConfig();
        }
    }

    private void startGiveawayTask() {
        giveawayManager = new GiveawayManager(this);
        task = Bukkit.getScheduler().runTaskTimer(this, giveawayManager, 0L, delay);
    }

    public void setConfigItems(Inventory inventory) {
    ConfigurationSection itemsSection = getConfig().createSection("Items");

    for (ItemStack item : inventory.getContents()) {
        if (item != null && item.getType() != Material.AIR) {
            ConfigurationSection itemSection = itemsSection.createSection(String.valueOf(item.hashCode()));
            itemSection.set("item", item);
        }
    }

    saveConfig();
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



    @Override
    public void onDisable() {
        if (task != null) {
            task.cancel();
        }
    }
    
    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public GiveawayManager getGiveawayManager() {
        return giveawayManager;
    }

    public Component getGuiTitle() {
        return GUI_TITLE;
    }

    public BukkitTask getTask() {
        return task;
    }
}
