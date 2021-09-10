package io.github.zrdzn.minecraft.magicmalve.spell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpellRegistry {

    private final List<Spell> spells = new ArrayList<>();

    public void register(Spell spell) {
        this.spells.add(spell);
    }

    public List<Spell> getSpells() {
        return Collections.unmodifiableList(this.spells);
    }

}
