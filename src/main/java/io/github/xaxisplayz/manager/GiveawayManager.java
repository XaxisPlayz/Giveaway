package io.github.xaxisplayz.manager;

import io.github.xaxisplayz.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class GiveawayManager {

    private ArrayList<ItemStack> items = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

    private final Main plugin;

    public GiveawayManager(Main plugin, ItemStack item){
        this.plugin = plugin;
        plugin.giveawayManager = this;
        addItem(item);
        startCountdown();
        if(plugin.getTask() != null) {
            plugin.getTask().cancel();
        }
    }
    private BukkitTask task;

    public AtomicInteger i = new AtomicInteger(20);
    public void startCountdown(){
        TextComponent message = Component.text(Main.colorize("&aA giveaway has started! &7&nClick here to enter!"));
        message.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "joingiveaway"));
        TextComponent message2 = Component.text(Main.colorize("&aGiveaway ends in 10 seconds! &7&nClick here to enter!"));
        message2.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "joingiveaway"));

        plugin.getServer().broadcast(message);

        task = plugin.getServer().getScheduler().runTaskTimer(plugin, ()->{

            if(i.get() == 10){
                plugin.getServer().broadcast(message2);
            }

            if(i.get() <= 0){

                if(items.isEmpty() || players.isEmpty()) {
                    cancel();
                    return;
                }

                Random random = new Random();
                ItemStack item = items.get(random.nextInt(items.size()));
                while(true) {
                    Player player = players.get(random.nextInt(players.size()));

                    for (ItemStack i : player.getInventory().getContents()) {
                        if (i == null) {
                            player.getInventory().addItem(item);
                            player.sendMessage(Main.colorize("&aCongratulations! You have won the giveaway!"));
                            plugin.giveawayManager = null;
                            cancel();
                            return;
                        }
                    }

                    player.sendMessage(Main.colorize("&4Your inventory was full so another winner will be chosen!"));
                }
            }


            i.getAndDecrement();
        }, 0L, 20L);
    }

    public void cancel(){
        task.cancel();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public ArrayList<ItemStack> getItems() {
        return items;
    }
    public void addItem(ItemStack item){
        items.add(item);
    }
    public void addItems(Inventory inventory){
        for(ItemStack item : inventory.getContents()){
            if(item == null) continue;
            if(item.getType() == Material.AIR) continue;
            items.add(item);
        }
    }
    public void removeItem(ItemStack item){
        items.remove(item);
    }
    public GiveawayManager(Main plugin) {
        this.plugin = plugin;
        plugin.giveawayManager = this;
        if (plugin.getConfig().getKeys(false).isEmpty()) {
            for (String name : plugin.getConfig().getKeys(false)) {
                ItemStack item = plugin.getConfig().getItemStack(name);
                if (item == null) continue;
                items.add(item);
            }
        }
    }





}
