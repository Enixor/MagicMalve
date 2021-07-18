package io.github.enixor.minecraft.magicmalve.spell;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface Spell {

    Sound getSucceedSound();

    Sound getDelayedSound();

    Sound getFailedSound();

    Particle getParticle();

    default long getDelay() {
        return 0;
    }

    ItemStack getItemStack();

    boolean call(UUID casterId, PlayerInteractEvent event);

    void spawnParticle(UUID playerId);

}
