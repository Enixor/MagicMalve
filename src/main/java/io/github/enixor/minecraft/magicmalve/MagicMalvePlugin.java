package io.github.enixor.minecraft.magicmalve;

import io.github.enixor.minecraft.magicmalve.command.MagicMalveCommand;
import io.github.enixor.minecraft.magicmalve.listener.PlayerInteractListener;
import io.github.enixor.minecraft.magicmalve.sound.SoundManager;
import io.github.enixor.minecraft.magicmalve.spell.*;
import io.github.enixor.minecraft.magicmalve.wand.WandParser;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MagicMalvePlugin extends JavaPlugin {

    private SpellRegistry spellRegistry;
    private ActiveSpellManager activeSpellManager;
    private ItemStack wandItemStack;
    private SoundManager soundManager;

    @Override
    public void onEnable() {
        this.spellRegistry = new SpellRegistry();
        this.activeSpellManager = new ActiveSpellManager(this);

        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();

        WandParser wandParser = new WandParser();
        try {
            ConfigurationSection configuration = this.getConfig();
            if (!configuration.isConfigurationSection("wand")) {
                throw new InvalidConfigurationException("Wand section does not exist.");
            }

            this.wandItemStack = wandParser.parse(configuration.getConfigurationSection("wand"));
        } catch (InvalidConfigurationException exception) {
            exception.printStackTrace();
            pluginManager.disablePlugin(this);
            return;
        }

        this.soundManager = new SoundManager(this);

        this.saveDefaultConfig();

        this.spellRegistry.register(new HealSpell(server));
        this.spellRegistry.register(new ExplodeSpell(server));

        this.getCommand("magicmalve").setExecutor(new MagicMalveCommand(this));

        pluginManager.registerEvents(new PlayerInteractListener(this, new SpellMenu(this)), this);
    }

    public SpellRegistry getSpellRegistry() {
        return this.spellRegistry;
    }

    public ActiveSpellManager getActiveSpellManager() {
        return this.activeSpellManager;
    }

    public ItemStack getWandItemStack() {
        return this.wandItemStack;
    }

    public SoundManager getSoundManager() {
        return this.soundManager;
    }

}
