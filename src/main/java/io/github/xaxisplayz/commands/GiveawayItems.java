package io.github.xaxisplayz.commands;

import io.github.xaxisplayz.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GiveawayItems implements CommandExecutor {

    private final Main plugin;

    public GiveawayItems(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("GiveawayItems").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player p && p.hasPermission("giveaway.admin")){

            Inventory inv = Bukkit.createInventory(null, 6*9);
            if(plugin.getConfig().getList("Items") != null) {
                for (Object itemStack : plugin.getConfig().getList("Items")) {
                    ItemStack item = (ItemStack) itemStack;
                    inv.addItem(item);
                }
            }
            p.openInventory(inv);
        }

        return true;
    }
}
