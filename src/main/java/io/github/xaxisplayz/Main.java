package io.github.xaxisplayz;

import io.github.xaxisplayz.commands.GiveawayItems;
import io.github.xaxisplayz.commands.JoinGiveaway;
import io.github.xaxisplayz.commands.ManualGiveaway;
import io.github.xaxisplayz.listeners.onInvClose;
import io.github.xaxisplayz.manager.GiveawayManager;
import io.github.xaxisplayz.manager.LangConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {

    public static final Component GUI_TITLE = Component.text(ChatColor.GREEN + "Giveaway Items!");
    public static long delay = 4L * 60L * 60L * 1000L; // 4 hours

    private BukkitTask task;
    public GiveawayManager giveawayManager;
    private LangConfig langConfig;

    @Override
    public void onEnable() {
        getCommand("giveawayitems").setExecutor(new GiveawayItems(this));
        getCommand("joingiveaway").setExecutor(new JoinGiveaway(this));
        getCommand("manualgiveaway").setExecutor(new ManualGiveaway(this));
        new onInvClose(this);
        saveDefaultConfig();
        Bukkit.getScheduler().runTaskLater(this, this::startGiveawayTask, delay);
        langConfig = new LangConfig(this, "Lang.yml");

        if(getConfig().get("Count") == null) {
            getConfig().set("Count", 0);
            saveConfig();
        }
    }

    private void startGiveawayTask() {
        giveawayManager = new GiveawayManager(this);
        task = Bukkit.getScheduler().runTaskTimer(this, ()-> new GiveawayManager(this), 0L, delay);
    }

    @Override
    public void onDisable() {
        if(giveawayManager == null) return;
        if (giveawayManager.getTask() == null) return;
        giveawayManager.cancel();
    }

    public void setConfigItems(Inventory inventory) {
        getConfig().getKeys(false).forEach(key -> getConfig().set(key, null));
        ConfigurationSection itemsSection = getConfig().createSection("Items");
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
                if(item == null) continue;
                items.add(item);
            }
        }

        return items;
    }
    
    public static String colorize(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public GiveawayManager getGiveawayManager() {
        return giveawayManager;
    }

    public BukkitTask getTask() {
        return task;
    }

    public static String componentToString(Component component){
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }

    public LangConfig getLang() {
        return langConfig;
    }
}
