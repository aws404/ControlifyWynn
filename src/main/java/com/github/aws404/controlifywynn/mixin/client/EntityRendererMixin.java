package com.github.aws404.controlifywynn.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wynntils.core.components.Models;
import com.wynntils.models.items.WynnItem;
import com.wynntils.models.items.items.game.GatheringToolItem;
import com.wynntils.utils.mc.McUtils;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.ControllerBinding;
import dev.isxander.controlify.controller.gamepad.GamepadConfig;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    private static final Pattern GATHER_CLICK_PATTERN = Pattern.compile("(Left|Right)-Click for (.*)");

    @Shadow @Final private TextRenderer textRenderer;

    /**
     * Inject into the nametag renderer to modify the gathering instructions.
     *
     * TODO: Would love to make this injection cleaner.
     */
    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
            method = "renderLabelIfPresent",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=553648127",
                    shift = At.Shift.BY,
                    by = -5
            ),
            cancellable = true)
    protected void renderLabelIfPresent_renderGatheringGuide(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci, @Local LocalRef<Text> textRef, @Local(ordinal = 2) LocalFloatRef hRef, @Local Matrix4f matrixRef) {
        // Check that entity is an ArmorStand, and a controller is the current input source
        if (entity instanceof ArmorStandEntity &&
                ControlifyApi.get().getCurrentController().isPresent() &&
                ControlifyApi.get().getCurrentController().get().config().showIngameGuide &&
                Controlify.instance().inGameButtonGuide().isPresent()
        ) {
            // Check the text of the nametag matches the gathering instructions pattern
            Matcher gatherMatch = GATHER_CLICK_PATTERN.matcher(textRef.get().getString());
            if (gatherMatch.find()) {
                // Check if the user is holding a gather tool, don't render the guide unless they are
                Optional<WynnItem> item = Models.Item.getWynnItem(McUtils.inventory().getMainHandStack());
                if (item.isEmpty() || !(item.get() instanceof GatheringToolItem)) {
                    matrices.pop();
                    ci.cancel();
                    return;
                }

                // Set the new text value
                textRef.set(Text.literal("Gather " + gatherMatch.group(2)).formatted(Formatting.GRAY));

                // Get the binding for the left or right click
                ControllerBinding binding = Objects.equals(gatherMatch.group(1), "Right") ? ControlifyApi.get().getCurrentController().get().bindings().USE : ControlifyApi.get().getCurrentController().get().bindings().ATTACK;

                // Make the space between the nameplates larger
                matrices.translate(0F, Objects.equals(gatherMatch.group(1), "Right") ? 4.0F: -3.5F, 0.0F);

                // Move the text to the right, so it is centered when combined with the guide glyph
                hRef.set(hRef.get() + binding.renderer().size().width() + 6);

                int padding = (this.textRenderer.getWidth(textRef.get()) + binding.renderer().size().width() + 6) / 2;

                // Render the guide glyph
                Identifier texture = ((GamepadBindAccessor) binding.getBind()).callGetTexture(((GamepadConfig)binding.getBind().controller().config()).theme);
                Matrix4f glyphMatrix = new Matrix4f(matrixRef);

                RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram);
                RenderSystem.setShaderTexture(0, texture);

                // Render the guide background
                RenderLayer backgroundLayer = RenderLayer.getTextSeeThrough(texture);
                BufferBuilder backgroundBuffer = (BufferBuilder) vertexConsumers.getBuffer(backgroundLayer);
                backgroundBuffer.vertex(glyphMatrix, -padding, -7.0F, 0.0F).color(1F, 1F, 1F, 0.25F).texture(0, 0).light(light).next();
                backgroundBuffer.vertex(glyphMatrix, -padding, 15.0F, 0.0F).color(1F, 1F, 1F, 0.25F).texture(0, 1).light(light).next();
                backgroundBuffer.vertex(glyphMatrix, -padding + 22.0F, 15.0F, 0.0F).color(1F, 1F, 1F, 0.25F).texture(1, 1).light(light).next();
                backgroundBuffer.vertex(glyphMatrix, -padding + 22.0F, -7.0F, 0.0F).color(1F, 1F, 1F, 0.25F).texture(1, 0).light(light).next();
                ((VertexConsumerProvider.Immediate) vertexConsumers).draw(backgroundLayer);

                // Render the guide foreground
                RenderLayer layerForeground = RenderLayer.getText(texture);
                BufferBuilder foregroundBuffer = (BufferBuilder) vertexConsumers.getBuffer(layerForeground);
                foregroundBuffer.vertex(glyphMatrix, -padding, -7.0F, 0.0F).color(1F, 1F, 1F, 1F).texture(0, 0).light(light).next();
                foregroundBuffer.vertex(glyphMatrix, -padding, 15.0F, 0.0F).color(1F, 1F, 1F, 1F).texture(0, 1).light(light).next();
                foregroundBuffer.vertex(glyphMatrix, -padding + 22.0F, 15.0F, 0.0F).color(1F, 1F, 1F, 1F).texture(1, 1).light(light).next();
                foregroundBuffer.vertex(glyphMatrix, -padding + 22.0F, -7.0F, 0.0F).color(1F, 1F, 1F, 1F).texture(1, 0).light(light).next();
                ((VertexConsumerProvider.Immediate) vertexConsumers).draw(layerForeground);
            }
        }
    }
}
