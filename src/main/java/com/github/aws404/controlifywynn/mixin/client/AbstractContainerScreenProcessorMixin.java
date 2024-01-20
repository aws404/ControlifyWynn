package com.github.aws404.controlifywynn.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.controlify.bindings.ControllerBindings;
import dev.isxander.controlify.gui.guide.ContainerGuideCtx;
import dev.isxander.controlify.gui.guide.GuideAction;
import dev.isxander.controlify.gui.guide.GuideActionRenderer;
import dev.isxander.controlify.gui.layout.RowLayoutComponent;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.compat.vanilla.AbstractContainerScreenProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;

import java.util.Optional;

@Mixin(value = AbstractContainerScreenProcessor.class, remap = false)
public abstract class AbstractContainerScreenProcessorMixin<C extends HandledScreen<?>> extends ScreenProcessor<C> {

    public AbstractContainerScreenProcessorMixin(C screen) {
        super(screen);
    }

    /**
     * Add addition GUI actions to the hud of the containers
     */
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Redirect(
            method = "onWidgetRebuild",
            remap = false,
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/isxander/controlify/gui/layout/RowLayoutComponent$Builder;build()Ldev/isxander/controlify/gui/layout/RowLayoutComponent;",
                    ordinal = 2
            )
    )
    private RowLayoutComponent<GuideActionRenderer<ContainerGuideCtx>> onWidgetRebuild_addBlacksmithAndIdentifierAction(RowLayoutComponent.Builder<GuideActionRenderer<ContainerGuideCtx>> instance, @Local ControllerBindings<?> bindings) {
        return instance.element(
                new GuideActionRenderer<>(
                    new GuideAction<>(
                            bindings.GUI_ABSTRACT_ACTION_1,
                            (ctx) -> {
                                if (this.screen.getTitle().getString().contains("Item Identifier")) {
                                    return Optional.of(Text.translatable("controlifywynn.guide.identifier.identify"));
                                }

                                if (this.screen.getTitle().getString().contains("What would you like to sell?")) {
                                    return Optional.of(Text.translatable("controlifywynn.guide.blacksmith.sell"));
                                }

                                if (this.screen.getTitle().getString().contains("What would you like to scrap?")) {
                                    return Optional.of(Text.translatable("controlifywynn.guide.blacksmith.scrap"));
                                }

                                return Optional.empty();
                            }
                    ),
                    true,
                    false
                )
        )
        .build();
    }
}
