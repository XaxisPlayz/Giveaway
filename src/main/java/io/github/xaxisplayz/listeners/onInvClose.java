package io.github.xaxisplayz.listeners;

import io.github.xaxisplayz.Main;
import io.github.xaxisplayz.manager.LangConfig;
import io.github.xaxisplayz.manager.MessagePath;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class onInvClose implements Listener {

    private final Main plugin;

    public onInvClose(Main plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInvClose(InventoryCloseEvent event){
        Inventory inv = event.getInventory();
        LangConfig lang = plugin.getLang();
        String title = Main.componentToString(event.getView().title());

        if (!isPlayerAuthorized((Player) event.getPlayer())) {
            return;
        }

        if (!lang.getString(MessagePath.GUI_TITLE).equalsIgnoreCase(title)) {
            return;
        }

        if (isEmptyInventory(inv)) {
            return;
        }

        if (plugin.getGiveawayManager() == null) {
            plugin.getLang().setConfigItems(inv);
        } else {
            plugin.getGiveawayManager().getItems().clear();
            plugin.getLang().setConfigItems(inv);
            plugin.getGiveawayManager().add(inv);
        }

        event.getPlayer().sendMessage(lang.getString(MessagePath.SUCCESS_SET_ITEMS));
    }

    private boolean isPlayerAuthorized(Player player) {
        return player.hasPermission("giveaway.admin");
    }

    private boolean isEmptyInventory(Inventory inventory) {
        return inventory.isEmpty();
    }
}
