package io.github.enixor.minecraft.magicmalve.sound;

import io.github.enixor.minecraft.magicmalve.MagicMalvePlugin;
import io.github.enixor.minecraft.magicmalve.spell.Spell;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;

import java.util.UUID;

public class SoundManager {

    private final MagicMalvePlugin plugin;

    public SoundManager(MagicMalvePlugin plugin) {
        this.plugin = plugin;
    }

    private Sound getSound(UUID playerId, SoundType state) {
        Spell currentSpell = this.plugin.getActiveSpellManager().getActiveSpell(playerId);

        return switch (state) {
            case SUCCEED -> currentSpell.getSucceedSound();
            case DELAYED -> currentSpell.getDelayedSound();
            case FAILED -> currentSpell.getFailedSound();
            case INVENTORY_CLICKED -> Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
            case SPELL_NOT_CHOSEN -> Sound.ENTITY_VILLAGER_YES;
        };
    }

    public void play(UUID playerId, SoundType state) {
        HumanEntity player = this.plugin.getServer().getPlayer(playerId);
        if (player == null) {
            throw new IllegalStateException("Player cannot be null.");
        }

        player.getWorld().playSound(player.getLocation(), this.getSound(playerId, state), 1.0F, 1.0F);
    }

}
