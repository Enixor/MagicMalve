package io.github.zrdzn.minecraft.magicmalve;

import io.github.zrdzn.minecraft.magicmalve.command.MagicMalveCommand;
import io.github.zrdzn.minecraft.magicmalve.listener.PlayerInteractListener;
import io.github.zrdzn.minecraft.magicmalve.sound.SoundManager;
import io.github.enixor.minecraft.magicmalve.spell.*;
import io.github.zrdzn.minecraft.magicmalve.spell.*;
import io.github.zrdzn.minecraft.magicmalve.wand.WandParser;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MagicMalvePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        Server server = this.getServer();

        SoundManager soundManager = new SoundManager(this.getServer());

        ActiveSpellManager activeSpellManager = new ActiveSpellManager(this.getServer(), soundManager);

        PluginManager pluginManager = server.getPluginManager();

        ItemStack wandItemStack;
        try {
            ConfigurationSection configuration = this.getConfig();
            if (!configuration.isConfigurationSection("wand")) {
                throw new InvalidConfigurationException("Wand section does not exist.");
            }

            wandItemStack = new WandParser().parse(configuration.getConfigurationSection("wand")).clone();
        } catch (InvalidConfigurationException exception) {
            exception.printStackTrace();
            pluginManager.disablePlugin(this);
            return;
        }

        this.getCommand("magicmalve").setExecutor(new MagicMalveCommand(this, wandItemStack));

        SpellRegistry spellRegistry = new SpellRegistry();

        spellRegistry.register(new HealSpell(server));
        spellRegistry.register(new ExplodeSpell(server));

        SpellMenu spellMenu = new SpellMenu(server, spellRegistry, activeSpellManager, soundManager);

        pluginManager.registerEvents(new PlayerInteractListener(wandItemStack, activeSpellManager, spellMenu), this);
    }

}
