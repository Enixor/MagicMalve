package io.github.zrdzn.minecraft.magicmalve.spell;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import java.util.*;

public class ExplodeSpell implements Spell {

    private final Server server;

    public ExplodeSpell(Server server) {
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
        return Optional.of(Particle.FLAME);
    }

    @Override
    public long getDelay() {
        return 6000;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.TNT);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.displayName(Component.text("Exploder"));

        List<String> loreLines = Arrays.asList("Use this spell to blow up the place you are looking at.", "Power of the explode is 3 times more than TNT.");

        List<Component> lore = new ArrayList<>();
        loreLines.forEach(line -> lore.add(Component.text(line, NamedTextColor.DARK_GRAY)));
        itemMeta.lore(lore);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @Override
    public boolean call(UUID playerId, PlayerInteractEvent event) {
        HumanEntity player = this.server.getPlayer(playerId);
        if (player == null) {
            throw new IllegalStateException("Player cannot be null.");
        }

        World world = player.getWorld();
        Location playerLocation = player.getLocation();
        RayTraceResult rayTraceResult = world.rayTraceBlocks(playerLocation, playerLocation.getDirection(), 30, FluidCollisionMode.ALWAYS, false);
        if (rayTraceResult == null) {
            // Temporary solution to prevent from throwing NPE
            player.sendMessage(Component.text("You need to aim on block.", NamedTextColor.RED));
            return false;
        }
        Location location = rayTraceResult.getHitPosition().toLocation(world);
        world.createExplosion(location, 12F);

        return true;
    }

    @Override
    public void spawnParticle(UUID playerId) {
        HumanEntity player = this.server.getPlayer(playerId);
        if (player == null) {
            throw new IllegalStateException("Player cannot be null.");
        }

        this.getParticle().ifPresent(particle -> player.getWorld().spawnParticle(particle, player.getEyeLocation(), 500));
    }

}
