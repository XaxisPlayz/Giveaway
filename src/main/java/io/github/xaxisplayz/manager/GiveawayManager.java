package io.github.xaxisplayz.manager;

import io.github.xaxisplayz.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GiveawayManager {

    private List<ItemStack> items = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();

    private final Main plugin;

    public GiveawayManager(Main plugin, ItemStack item){
        this.plugin = plugin;
        plugin.giveawayManager = this;
        add(item.clone());
        startCountdown();
        if(plugin.getTask() != null) {
            plugin.getTask().cancel();
        }
        i = Long.parseLong(plugin.getLang().getString(MessagePath.GIVEAWAY_COUNTDOWN_TIME));
    }
    private BukkitTask task;

    public BukkitTask getTask() {
        return task;
    }

    public long i;
    public void startCountdown() {
        ClickEvent clickEvent = ClickEvent.runCommand("/joingiveaway");
        HoverEvent<Component> hoverEvent = HoverEvent.showText(Component.text(Main.colorize("Click here to join the giveaway!")));
        TextComponent message = Component.text(Main.colorize("&aA giveaway has started! &7&nClick here to enter!")).clickEvent(clickEvent).hoverEvent(hoverEvent);
        TextComponent message2 = Component.text(Main.colorize("&aGiveaway ends in 10 seconds! &7&nClick here to enter!")).clickEvent(clickEvent).hoverEvent(hoverEvent);

        plugin.getServer().broadcast(message);

        task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            i--;
            if (i == 10 * 20) {
                plugin.getServer().broadcast(message2);
            } else if (i <= 0) {
                selectWinner();
                cancel();
            }
        }, 0L, i);
    }

    private void selectWinner() {
        if (items.isEmpty() || players.isEmpty()) {
            return;
        }

        Random random = new Random();
        ItemStack item = items.get(random.nextInt(items.size()));
        while (true) {
            Player player = players.get(random.nextInt(players.size()));

            for (ItemStack i : player.getInventory().getContents()) {
                if (i == null) {
                    player.getInventory().addItem(item);
                    player.sendMessage(Main.colorize(plugin.getLang().getString(MessagePath.GIVEAWAY_WON)));
                    plugin.giveawayManager = null;
                    return;
                }
            }

            player.sendMessage(Main.colorize(plugin.getLang().getString(MessagePath.INVENTORY_FULL)));
        }
    }

    public void cancel(){
        task.cancel();
    }

    public List<Player> getPlayers() {
        return players;
    }
    public List<ItemStack> getItems() {
        return items;
    }
    public void add(ItemStack item){
        items.add(item);
    }
    public void add(Inventory inventory){
        for(ItemStack item : inventory.getContents()){
            if(item == null) continue;
            if(item.getType() == Material.AIR) continue;
            items.add(item);
        }
    }
    public GiveawayManager(Main plugin) {
        this.plugin = plugin;
        plugin.giveawayManager = this;
        items = plugin.getConfigItems();
    }





}
