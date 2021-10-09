package io.github.zrdzn.minecraft.magicmalve.spell.spells;

import io.github.zrdzn.minecraft.magicmalve.spell.Spell;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HealSpell implements Spell {

    private final Server server;
    private final double healthPoints = 2;

    public HealSpell(Server server) {
        this.server = server;
    }

    @Override
    public Optional<Sound> getSucceedSound() {
        return Optional.of(Sound.ENTITY_VILLAGER_YES);
    }

    @Override
    public Optional<Sound> getDelayedSound() {
        return Optional.of(Sound.ENTITY_VILLAGER_NO);
    }

    @Override
    public Optional<Sound> getFailedSound() {
        return Optional.of(Sound.ENTITY_VILLAGER_NO);
    }

    @Override
    public Optional<Particle> getParticle() {
        return Optional.of(Particle.HEART);
    }

    @Override
    public long getDelay() {
        return 3000;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.HEART_OF_THE_SEA);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.displayName(Component.text("Heal"));

        double healthHearts = ((this.healthPoints / 2) * 10) / 10.0D;

        List<String> loreLines = Arrays.asList("Use this spell to heal yourself.", "Adds " + this.healthPoints + " points, which is " + healthHearts + " hearts.");

        List<Component> lore = new ArrayList<>();
        loreLines.forEach(line -> lore.add(Component.text(line, NamedTextColor.DARK_GRAY)));
        itemMeta.lore(lore);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public boolean call(UUID playerId, PlayerInteractEvent event) {
        Player player = this.server.getPlayer(playerId);
        if (player == null) {
            throw new IllegalStateException("Player cannot be null.");
        }

        double health = player.getHealth();
        double healthScale = player.getHealthScale();

        if (health == healthScale) {
            player.sendMessage(Component.text("You cannot use this spell, because you have full hp.", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1F, 1F);
            return false;
        }

        // Set health manually to prevent from bounding out of array
        if (health + this.healthPoints > healthScale) {
            player.setHealth(healthScale);
            return true;
        }

        player.setHealth(health + this.healthPoints);
        return true;
    }

    @Override
    public void spawnParticle(UUID playerId) {
        HumanEntity player = this.server.getPlayer(playerId);
        if (player == null) {
            throw new IllegalStateException("Player cannot be null.");
        }

        // Create circle around the player on the ground with hearts
        for (int i = 0; i < 360; i++) {
            double angle = (i * Math.PI / 180);
            double size = 1;
            double x = size * Math.cos(angle);
            double z = size * Math.sin(angle);

            Location location = player.getLocation().add(x, 0, z);

            this.getParticle().ifPresent(particle -> player.getWorld().spawnParticle(particle, location, 500));
        }
    }

}
