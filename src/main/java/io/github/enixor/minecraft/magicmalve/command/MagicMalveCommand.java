package io.github.enixor.minecraft.magicmalve.command;

import io.github.enixor.minecraft.magicmalve.MagicMalvePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MagicMalveCommand implements CommandExecutor {

    private final MagicMalvePlugin plugin;

    public MagicMalveCommand(MagicMalvePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("/spells reload/wand");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You cannot execute this argument as console.", NamedTextColor.RED));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            this.plugin.reloadConfig();
            sender.sendMessage(Component.text("Config reloaded", NamedTextColor.GREEN));
            return true;
        } else if (args[0].equalsIgnoreCase("wand")) {
            player.getInventory().addItem(this.plugin.getWandItemStack());
        } else if (args[0].equalsIgnoreCase("test")) {
            if (args.length == 1) {
                return true;
            }

            if (args[1].equalsIgnoreCase("low")) {
                player.setHealth(2);
                player.setFoodLevel(2);
                return true;
            } else if (args[1].equalsIgnoreCase("max")) {
                player.setHealth(player.getHealthScale());
                player.setFoodLevel(20);
                return true;
            }
        }

        return true;
    }

}
