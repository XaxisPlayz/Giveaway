package io.github.xaxisplayz;

import io.github.xaxisplayz.commands.GiveawayItems;
import io.github.xaxisplayz.commands.JoinGiveaway;
import io.github.xaxisplayz.commands.ManualGiveaway;
import io.github.xaxisplayz.manager.GiveawayManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.UUID;


@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {

    public static Component GUI_TITLE = Component.text(colorize("&aGiveaway Items!"));

    public GiveawayManager giveawayManager;

    private BukkitTask task;

    /*
    - Every 4 hours a giveaway starts in chat
    - Clickable Message to join
    - 20s giveaway starts, random winner chosen
    - if inv full choose different player
    - Run a command that opens a gui to place items in it, an item will be randomly chosen for the giveaway.
    - hold an item and run a giveaway with the item in hand for manual giveaway
     */

    public static String colorize(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public void onEnable() {

        new GiveawayItems(this);
        new onInvClose(this);
        new JoinGiveaway(this);
        new ManualGiveaway(this);

        saveDefaultConfig();
        Bukkit.getScheduler().runTaskLater(this, this::runTask,4L*60L*60L*1000L);
        if(getConfig().get("Count") == null){
            getConfig().set("Count",0);
            saveConfig();
        }
    }

    public BukkitTask getTask() {
        return task;
    }

    public GiveawayManager getGiveawayManager() {
        return giveawayManager;
    }

    public void runTask(){
        task = getServer().getScheduler().runTaskTimer(this, ()->{
            new GiveawayManager(this);
        }, 0L, 4L * 60L * 60L * 1000L);
    }

    public void setConfigItems(Inventory inventory){
        for(ItemStack item : inventory.getContents()){
            if(item == null) continue;
            if(item.getType() == Material.AIR) continue;
            getConfig().set("'" + getConfig().getInt("Count") + "'",item);
            getConfig().set("Count",getConfig().getInt("Count") + 1);
        }
        saveConfig();
    }

    @Override
    public void onDisable() {
    }




}
