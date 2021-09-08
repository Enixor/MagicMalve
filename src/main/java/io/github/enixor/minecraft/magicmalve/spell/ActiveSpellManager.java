package io.github.enixor.minecraft.magicmalve.spell;

import io.github.enixor.minecraft.magicmalve.sound.SoundManager;
import io.github.enixor.minecraft.magicmalve.sound.SoundType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActiveSpellManager {

    private final Server server;
    private final SoundManager soundManager;
    private final Map<UUID, Spell> currentSpell = new HashMap<>();
    private final Map<UUID, Long> lastUsed = new HashMap<>();

    public ActiveSpellManager(Server server, SoundManager soundManager) {
        this.server = server;
        this.soundManager = soundManager;
    }

    public Spell getActiveSpell(UUID uuid) {
        return this.currentSpell.get(uuid);
    }

    public void setActiveSpell(UUID uuid, Spell spell) {
        this.currentSpell.put(uuid, spell);
    }

    public void executeCurrentSpell(UUID playerId, PlayerInteractEvent event) {
        HumanEntity player = this.server.getPlayer(playerId);
        if (player == null) {
            throw new IllegalStateException("Player cannot be null.");
        }

        if (this.getActiveSpell(playerId) == null) {
            player.sendMessage(Component.text("You need to choose a spell.", NamedTextColor.RED));
            this.soundManager.play(this, playerId, SoundType.SPELL_NOT_CHOSEN);
            return;
        }

        if (this.getTimeUntilNextSpellCast(playerId) != 0) {
            player.sendMessage(Component.text("You need to wait ", NamedTextColor.RED)
                    .append(Component.text(this.getTimeUntilNextSpellCast(playerId) / 1000 + " seconds", NamedTextColor.YELLOW))
                    .append(Component.text(" before using it again.")));
            this.soundManager.play(this, playerId, SoundType.DELAYED);
            return;
        }

        if (!this.getActiveSpell(playerId).call(playerId, event)) {
            this.soundManager.play(this, playerId, SoundType.FAILED);
            return;
        }

        this.lastUsed.put(playerId, System.currentTimeMillis());
        this.getActiveSpell(playerId).spawnParticle(playerId);
        this.soundManager.play(this, playerId, SoundType.SUCCEED);
    }

    public long getTimeUntilNextSpellCast(UUID playerId) {
        Long lastUsage = this.lastUsed.get(playerId);
        if (lastUsage == null) {
            return 0;
        }

        long waited = System.currentTimeMillis() - lastUsage;
        long delay = this.getActiveSpell(playerId).getDelay();
        if (waited >= delay) {
            return 0;
        }

        return delay - waited;
    }

}
