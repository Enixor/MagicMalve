package io.github.enixor.minecraft.magicmalve.listener;

import io.github.enixor.minecraft.magicmalve.MagicMalvePlugin;
import io.github.enixor.minecraft.magicmalve.spell.ActiveSpellManager;
import io.github.enixor.minecraft.magicmalve.spell.SpellMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerInteractListener implements Listener {

    private final MagicMalvePlugin plugin;
    private final SpellMenu spellMenu;

    public PlayerInteractListener(MagicMalvePlugin plugin, SpellMenu spellMenu) {
        this.plugin = plugin;
        this.spellMenu = spellMenu;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        ItemStack heldItem = event.getItem();
        ItemStack wandItem = this.plugin.getWandItemStack();
        Action action = event.getAction();
        ActiveSpellManager activeSpellManager = this.plugin.getActiveSpellManager();

        if (!wandItem.isSimilar(heldItem)) {
            return;
        }

        // Open inventory
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            this.spellMenu.open(playerId);
            return;
        }

        // Use spell
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            activeSpellManager.executeCurrentSpell(playerId, event);
        }
    }

}
