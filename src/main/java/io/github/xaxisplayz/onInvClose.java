import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Objects;

public class onInvClose implements Listener {

    private final Main plugin;

    public onInvClose(Main plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInvClose(InventoryCloseEvent event){
        Inventory inv = event.getInventory();
        Component title = event.getView().title();

        if (!isPlayerAuthorized(event.getPlayer())) {
            return;
        }

        if (!Lang.GUI_TITLE.equals(title)) {
            return;
        }

        if (isEmptyInventory(inv)) {
            return;
        }

        if (plugin.getGiveawayManager() == null) {
            plugin.setConfigItems(inv);
        } else {
            plugin.getGiveawayManager().getItems().clear();
            plugin.setConfigItems(inv);
            plugin.getGiveawayManager().addItems(inv);
        }

        event.getPlayer().sendMessage(Lang.SUCCESS_SET_ITEMS.toString());
    }

    private boolean isPlayerAuthorized(Player player) {
        return player.hasPermission("giveaway.admin");
    }

    private boolean isEmptyInventory(Inventory inventory) {
        return inventory.isEmpty();
    }
}
