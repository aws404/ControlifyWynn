package com.github.aws404.controlifywynn.mixin.client;

import com.wynntils.core.text.StyledText;
import com.wynntils.overlays.NpcDialogueOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = NpcDialogueOverlay.class, remap = false)
public interface NpcDialogueOverlayAccessor {
    @Accessor(remap = false)
    List<?> getConfirmationlessDialogues();

    @Accessor(remap = false)
    List<StyledText> getCurrentDialogue();
}
