package io.github.xaxisplayz;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class onInvClose implements Listener {

    private final Main plugin;

    public onInvClose(Main plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onPlayerInvClose(InventoryCloseEvent event){
        Inventory inv = event.getInventory();
        String title = event.getView().title().toString();
        if(!title.equalsIgnoreCase(Main.GUI_TITLE) && event.getPlayer().hasPermission("giveaway.admin")) return;
        if(plugin.getGiveawayManager() == null) return;
        plugin.getGiveawayManager().getItems().clear();
        plugin.getGiveawayManager().addItems(Arrays.stream(inv.getStorageContents()).filter(itemStack -> itemStack.getType() != Material.AIR).toList());
        event.getPlayer().sendMessage(Main.colorize("&aSuccessfully set items!"));
    }
}
