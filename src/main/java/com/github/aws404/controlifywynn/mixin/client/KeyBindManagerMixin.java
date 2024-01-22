package com.github.aws404.controlifywynn.mixin.client;

import com.wynntils.core.keybinds.KeyBind;
import com.wynntils.core.keybinds.KeyBindManager;
import com.wynntils.utils.type.Pair;
import dev.isxander.controlify.api.bind.ControlifyBindingsApi;
import dev.isxander.controlify.bindings.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

@Mixin(value = KeyBindManager.class, remap = false)
public class KeyBindManagerMixin {

    /**
     * Get the Wynntils keybinds into the fabric modded bindings registry
     */
    @Redirect(method = "discoverKeyBinds", at = @At(value = "INVOKE", target = "Lcom/wynntils/utils/type/Pair;of(Ljava/lang/Object;Ljava/lang/Object;)Lcom/wynntils/utils/type/Pair;", remap = false))
    private <T, J> Pair<T, J> discoverKeyBinds_includeWynntilsBinds(T a, J b) {
        Identifier identifier = new Identifier("wynntils", ((String) b).toLowerCase());

        ControlifyBindingsApi.get().registerBind(identifier, builder -> builder
                .identifier(identifier)
                .name(Text.of(((KeyBind) a).getKeyMapping().getTranslationKey()))
                .defaultBind(new EmptyBind<>())
                .vanillaOverride(((KeyBind) a).getKeyMapping())
                .radialCandidate(getRadialIcon((KeyBind) a))
                .category(Text.of(KeyBindAccessor.getCATEGORY()))
                .context(getContext((KeyBind) a))
        );

        return Pair.of(a, b);
    }

    private static BindContext getContext(KeyBind bind) {
        if (((KeyBindAccessor) bind).getOnInventoryPress() != null) {
            return BindContexts.GUI;
        }

        return BindContexts.INGAME;
    }

    private static Identifier getRadialIcon(KeyBind keyBind) {
        if (Objects.equals(keyBind.getName(), "Mount Horse")) {
            return RadialIcons.getItem(Items.SADDLE);
        }

        if (Objects.equals(keyBind.getName(), "Open Main Map")) {
            return RadialIcons.getItem(Items.FILLED_MAP);
        }

        if (Objects.equals(keyBind.getName(), "New Waypoint")) {
            return RadialIcons.getItem(Items.FEATHER);
        }

        if (Objects.equals(keyBind.getName(), "Open Quest Book")) {
            return RadialIcons.getItem(Items.BOOK);
        }

        if (Objects.equals(keyBind.getName(), "View player's gear")) {
            return RadialIcons.getItem(Items.GOLDEN_CHESTPLATE);
        }

        if (keyBind.getName().startsWith("Cast ")) {
            return RadialIcons.getItem(Items.STICK);
        }

        if (keyBind.getName().startsWith("Execute ")) {
            return RadialIcons.getItem(Items.COMMAND_BLOCK);
        }

        if (Objects.equals(keyBind.getName(), "Gammabright")) {
            return RadialIcons.getItem(Items.ENDER_EYE);
        }

        if (Objects.equals(keyBind.getName(), "Open Emerald Pouch")) {
            return RadialIcons.getItem(Items.EMERALD);
        }

        if (Objects.equals(keyBind.getName(), "Open Wynntils Menu")) {
            return RadialIcons.getItem(Items.ENCHANTED_BOOK);
        }

        if (Objects.equals(keyBind.getName(), "Open Powder Menu")) {
            return RadialIcons.getItem(Items.RED_DYE);
        }

        if (Objects.equals(keyBind.getName(), "Open Item Guide")) {
            return RadialIcons.getItem(Items.SHEARS);
        }

        return RadialIcons.getItem(Items.BARRIER);

    }
}
