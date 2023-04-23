package io.github.xaxisplayz;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.Objects;

public class onInvClose implements Listener {

    private final Main plugin;

    public onInvClose(Main plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onPlayerInvClose(InventoryCloseEvent event){
        Inventory inv = event.getInventory();
        Component title = event.getView().title();
        if(!title.equals(Main.GUI_TITLE) && event.getPlayer().hasPermission("giveaway.admin")) return;
        if(inv.isEmpty()) return;
        if(plugin.getGiveawayManager() == null) {
            plugin.setConfigItems(inv);
            event.getPlayer().sendMessage(Main.colorize("&aSuccessfully set items!"));
            return;
        }
        plugin.getGiveawayManager().getItems().clear();
        plugin.setConfigItems(inv);
        plugin.getGiveawayManager().addItems(inv);
        event.getPlayer().sendMessage(Main.colorize("&aSuccessfully set items!"));

    }
}
