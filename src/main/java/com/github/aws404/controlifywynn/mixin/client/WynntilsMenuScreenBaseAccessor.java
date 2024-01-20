package com.github.aws404.controlifywynn.mixin.client;

import com.wynntils.screens.base.WynntilsMenuScreenBase;
import com.wynntils.screens.settings.WynntilsBookSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = {WynntilsMenuScreenBase.class, WynntilsBookSettingsScreen.class}, remap = false)
public interface WynntilsMenuScreenBaseAccessor {
    @Invoker(remap = false)
    float callGetTranslationX();

    @Invoker(remap = false)
    float callGetTranslationY();
}
