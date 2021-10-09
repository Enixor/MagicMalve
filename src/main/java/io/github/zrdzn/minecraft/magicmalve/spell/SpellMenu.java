package io.github.zrdzn.minecraft.magicmalve.spell;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.github.zrdzn.minecraft.magicmalve.sound.SoundService;
import io.github.zrdzn.minecraft.magicmalve.sound.SoundType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;

import java.util.UUID;

public class SpellMenu {

    private final Server server;
    private final SpellRegistry spellRegistry;
    private final ActiveSpellManager activeSpellManager;
    private final SoundService soundService;

    public SpellMenu(Server server, SpellRegistry spellRegistry, ActiveSpellManager activeSpellManager, SoundService soundService) {
        this.server = server;
        this.spellRegistry = spellRegistry;
        this.activeSpellManager = activeSpellManager;
        this.soundService = soundService;
    }

    public void open(UUID playerId) {
        PaginatedGui gui = Gui.paginated()
                .title(Component.text("Spell menu", NamedTextColor.DARK_GREEN))
                .rows(4)
                .disableAllInteractions()
                .create();

        this.spellRegistry.getSpells().forEach(spell ->
                gui.addItem(new GuiItem(spell.getItemStack(), event -> this.activeSpellManager.setActiveSpell(playerId, spell))));

        this.soundService.play(this.activeSpellManager, playerId, SoundType.INVENTORY_CLICKED);

        HumanEntity player = this.server.getPlayer(playerId);
        if (player == null) {
            throw new IllegalStateException("Player cannot be null.");
        }

        gui.open(player);
    }

}
