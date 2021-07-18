package io.github.enixor.minecraft.magicmalve.spell;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.enixor.minecraft.magicmalve.MagicMalvePlugin;
import io.github.enixor.minecraft.magicmalve.sound.SoundType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.HumanEntity;

import java.util.UUID;

public class SpellMenu {

    private final MagicMalvePlugin plugin;

    public SpellMenu(MagicMalvePlugin plugin) {
        this.plugin = plugin;
    }

    public void open(UUID playerId) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("Spell menu", NamedTextColor.DARK_GREEN))
                .rows(4)
                .create();

        ActiveSpellManager activeSpellManager = this.plugin.getActiveSpellManager();
        this.plugin.getSpellRegistry().getSpells().forEach(spell ->
                gui.addItem(new GuiItem(spell.getItemStack(), event -> activeSpellManager.setActiveSpell(playerId, spell))));
        this.plugin.getSoundManager().play(playerId, SoundType.INVENTORY_CLICKED);

        HumanEntity player = this.plugin.getServer().getPlayer(playerId);
        if (player == null) {
            throw new IllegalStateException("Player cannot be null.");
        }

        gui.open(player);
    }

}
