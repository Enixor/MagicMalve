package io.github.zrdzn.minecraft.magicmalve.sound;

import io.github.zrdzn.minecraft.magicmalve.spell.ActiveSpellManager;
import io.github.zrdzn.minecraft.magicmalve.spell.Spell;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;

import java.util.UUID;

public class SoundService {

    private final Server server;

    public SoundService(Server server) {
        this.server = server;
    }

    private Sound getSound(ActiveSpellManager activeSpellManager, UUID playerId, SoundType state) {
        Spell currentSpell = activeSpellManager.getActiveSpell(playerId);

        return switch (state) {
            case SUCCEED -> currentSpell.getSucceedSound().orElse(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
            case DELAYED -> currentSpell.getDelayedSound().orElse(Sound.ENTITY_VILLAGER_NO);
            case FAILED -> currentSpell.getFailedSound().orElse(Sound.BLOCK_CHEST_CLOSE);
            case INVENTORY_CLICKED -> Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
            case SPELL_NOT_CHOSEN -> Sound.ENTITY_VILLAGER_YES;
        };
    }

    public void play(ActiveSpellManager activeSpellManager, UUID playerId, SoundType state) {
        HumanEntity player = this.server.getPlayer(playerId);
        if (player == null) {
            throw new IllegalStateException("Player cannot be null.");
        }

        player.getWorld().playSound(player.getLocation(), this.getSound(activeSpellManager, playerId, state), 1.0F, 1.0F);
    }

}
