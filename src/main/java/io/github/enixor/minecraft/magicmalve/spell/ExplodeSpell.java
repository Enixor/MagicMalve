package io.github.enixor.minecraft.magicmalve.spell;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ExplodeSpell implements Spell {

    private final Server server;

    public ExplodeSpell(Server server) {
        this.server = server;
    }

    @Override
    public Sound getSucceedSound() {
        return Sound.ENTITY_VILLAGER_YES;
    }

    @Override
    public Sound getDelayedSound() {
        return Sound.ENTITY_VILLAGER_NO;
    }

    @Override
    public Sound getFailedSound() {
        return Sound.ENTITY_VILLAGER_NO;
    }

    @Override
    public Particle getParticle() {
        return Particle.FLAME;
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

        player.getWorld().spawnParticle(this.getParticle(), player.getEyeLocation(), 500);
    }

}
