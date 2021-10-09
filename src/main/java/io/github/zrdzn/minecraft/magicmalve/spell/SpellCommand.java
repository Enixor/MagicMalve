package io.github.zrdzn.minecraft.magicmalve.spell;

import io.github.zrdzn.minecraft.magicmalve.MagicMalvePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpellCommand implements CommandExecutor {

    private final MagicMalvePlugin plugin;
    private final ItemStack wandItemStack;

    public SpellCommand(MagicMalvePlugin plugin, ItemStack wandItemStack) {
        this.plugin = plugin;
        this.wandItemStack = wandItemStack;
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
            player.getInventory().addItem(this.wandItemStack);
        }

        return true;
    }

}
