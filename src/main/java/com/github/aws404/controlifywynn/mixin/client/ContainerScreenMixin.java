package com.github.aws404.controlifywynn.mixin.client;

import com.wynntils.core.components.Models;
import com.wynntils.core.text.StyledText;
import com.wynntils.models.containers.type.InteractiveContainerType;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.BindRenderer;
import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.compatibility.ControlifyCompat;
import dev.isxander.controlify.controller.Controller;
import org.apache.commons.lang3.ArrayUtils;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mixin(GenericContainerScreen.class)
public abstract class ContainerScreenMixin extends HandledScreen<GenericContainerScreenHandler> implements ScreenHandlerProvider<GenericContainerScreenHandler>, ISnapBehaviour {
    private static final int[] IGNORED_GOLDEN_SHOVEL_ITEMS = {4, 9, 11, 23, 24, 25};
    private static final Predicate<ItemStack> IS_GUI_ITEM = stack ->
            (stack.isOf(Items.GOLDEN_SHOVEL) && ArrayUtils.contains(IGNORED_GOLDEN_SHOVEL_ITEMS, stack.getDamage())) ||
            stack.isOf(Items.STRUCTURE_VOID) ||
            stack.getName().getString().isBlank() ||
            stack.getName().getString().contains("Click on an item to purchase it");
    private static final Predicate<Slot> NOT_GUI = (slot) -> !IS_GUI_ITEM.test(slot.getStack());
    private static final Predicate<Slot> NOT_GUI_OR_EMPTY = NOT_GUI.and(Slot::hasStack);
    private static final Predicate<Slot> IS_PLAYER_OR_NOT_GUI_OR_EMPTY = NOT_GUI_OR_EMPTY.or(slot -> slot.inventory instanceof PlayerInventory);

    public ContainerScreenMixin(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) { super(handler, inventory, title); }

    /**
     * Override the snap points to remove gui elements
     */
    @Override
    public Set<SnapPoint> getSnapPoints() {
        // By default, only snap to filled slots that aren't GUI items
        Predicate<Slot> filter = NOT_GUI_OR_EMPTY;

        if (Models.Bank.getCurrentContainer() != null || StyledText.fromComponent(this.title).endsWith(" Pouch")) {
            // In banks and pouches, snap to all items except GUI items
            filter = NOT_GUI;
        } else if (Models.Container.isLootOrRewardChest(this)) {
            // In loot and reward chests, snap to all player inventory slots and only filled chest slots
            filter = IS_PLAYER_OR_NOT_GUI_OR_EMPTY;
        }

        return this.getScreenHandler().slots.stream()
                .filter(filter)
                .map(slot -> new SnapPoint(new Vector2i(this.x + slot.x + 8, this.y + slot.y + 8), 17))
                .collect(Collectors.toSet());
    }


    /**
     * Render the page-based navigation guide on Wynntils page based guis
     */
    @Inject(method = "render", at = @At("RETURN"))
    private void render_renderPageNavGuide(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Controlify.instance().currentInputMode().isController()) {
            Controlify.instance().getCurrentController().ifPresent(controller -> {
                if (controller.config().showScreenGuide) {
                    this.renderControllerButtonOverlay(context, controller);
                }
            });
        }
    }

    private void renderControllerButtonOverlay(DrawContext graphics, Controller<?, ?> controller) {
        ControlifyCompat.ifBeginHudBatching();

        // Render left scroll if available
        Optional<Integer> scrollLeft = InteractiveContainerType.getScrollButton(this, true);;
        if (scrollLeft.isPresent() && this.handler.getSlot(scrollLeft.get()).hasStack()) {
            BindRenderer renderer = controller.bindings().GUI_PREV_TAB.renderer();
            renderer.render(graphics, this.x - renderer.size().width() - 4, this.y - 8);
        }

        // Render right scroll if available
        Optional<Integer> scrollRight = InteractiveContainerType.getScrollButton(this, false);;
        if (scrollRight.isPresent() && this.handler.getSlot(scrollRight.get()).hasStack()) {
            BindRenderer renderer = controller.bindings().GUI_NEXT_TAB.renderer();
            renderer.render(graphics, this.x + this.backgroundWidth + 4, this.y - 8);
        }

        ControlifyCompat.ifEndHudBatching();
    }

}
