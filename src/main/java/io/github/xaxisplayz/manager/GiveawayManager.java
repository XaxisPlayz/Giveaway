package io.github.xaxisplayz.manager;

import io.github.xaxisplayz.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
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
        addItem(item);
        startCountdown();
        plugin.getTask().cancel();
    }
    private BukkitTask task;

    public AtomicInteger i = new AtomicInteger(20);
    public void startCountdown(){
        TextComponent message = Component.text(Main.colorize("A giveaway has started! Click here to enter!"));
        message.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "joingiveaway"));
        TextComponent message2 = Component.text(Main.colorize("Giveaway ends in 10 seconds! Click here to enter!"));
        message2.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "joingiveaway"));
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, ()->{

            plugin.getServer().broadcast(message);

            if(i.get() == 10){
                plugin.getServer().broadcast(message2);
            }

            if(i.get() == 0){

                boolean a = true;

                Random random = new Random();
                ItemStack item = getItems().get(random.nextInt(getItems().size()));
                while(a) {
                    Player player = getPlayers().get(random.nextInt(getPlayers().size()));

                    for (ItemStack i : player.getInventory().getContents()) {
                        if (i == null) {
                            player.getInventory().addItem(item);
                            player.sendMessage(Main.colorize("&aCongratulations! You have won the giveaway!"));
                            cancel();
                            a = false;
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
    public void addItems(List<ItemStack> item){
        items.addAll(item);
    }
    public void removeItem(ItemStack item){
        items.remove(item);
    }
    public GiveawayManager(Main plugin){
        this.plugin = plugin;
        for(Object itemStack : plugin.getConfig().getList("Items")){
            ItemStack item = (ItemStack) itemStack;
            if(getItems().contains(item)) continue;
            addItem(item);
        }
    }





}
