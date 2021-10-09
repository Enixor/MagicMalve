package io.github.zrdzn.minecraft.magicmalve.wand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WandParser {

    public ItemStack parse(ConfigurationSection section) throws InvalidConfigurationException {
        String materialString = section.getString("material", "stick");

        Material material = Material.matchMaterial(materialString);
        if (material == null) {
            throw new InvalidConfigurationException(materialString + " is not recognized as a material.");
        }

        Component displayName = Component.text(section.getString("display-name", "Wand"))
                .color(TextColor.color(section.getInt("display-name-color", 0x55ff55)));

        List<Component> lore = new ArrayList<>();
        section.getStringList("lore").forEach(line -> lore.add(Component.text(line)
                .color(TextColor.color(section.getInt("lore-color", 0xaaaaaa)))));

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(displayName);
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack.clone();
    }

}
