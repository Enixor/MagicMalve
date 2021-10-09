package io.github.zrdzn.minecraft.magicmalve.wand;

import io.github.zrdzn.minecraft.magicmalve.spell.ActiveSpellManager;
import io.github.zrdzn.minecraft.magicmalve.spell.SpellMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class WandListener implements Listener {

    private final ItemStack wandItemStack;
    private final ActiveSpellManager activeSpellManager;
    private final SpellMenu spellMenu;

    public WandListener(ItemStack wandItemStack, ActiveSpellManager activeSpellManager, SpellMenu spellMenu) {
        this.wandItemStack = wandItemStack;
        this.activeSpellManager = activeSpellManager;
        this.spellMenu = spellMenu;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        ItemStack heldItem = event.getItem();
        Action action = event.getAction();

        if (!this.wandItemStack.isSimilar(heldItem)) {
            return;
        }

        // Open inventory
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            this.spellMenu.open(playerId);
            return;
        }

        // Use spell
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            this.activeSpellManager.executeCurrentSpell(playerId, event);
        }
    }

}
