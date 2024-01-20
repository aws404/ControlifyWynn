package com.github.aws404.controlifywynn.mixin.client;

import com.wynntils.core.keybinds.KeyBind;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.screen.slot.Slot;

import java.util.function.Consumer;

@Mixin(value = KeyBind.class, remap = false)
public interface KeyBindAccessor {
    @Accessor(remap = false)
    static String getCATEGORY() {
        throw new UnsupportedOperationException();
    }

    @Accessor(remap = false)
    Consumer<Slot> getOnInventoryPress();
}
