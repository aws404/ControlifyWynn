package com.github.aws404.controlifywynn.mixin.client;

import com.wynntils.features.overlays.NpcDialogueOverlayFeature;
import com.wynntils.overlays.NpcDialogueOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = NpcDialogueOverlayFeature.class, remap = false)
public interface NpcDialogueOverlayFeatureAccessor {
    @Accessor(remap = false)
    NpcDialogueOverlay getNpcDialogueOverlay();
}
