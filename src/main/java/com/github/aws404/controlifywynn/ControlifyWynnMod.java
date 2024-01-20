package com.github.aws404.controlifywynn;

import com.wynntils.core.components.Managers;
import com.wynntils.core.components.Models;
import com.wynntils.features.ui.ContainerScrollFeature;
import com.wynntils.utils.mc.McUtils;
import com.wynntils.utils.wynn.ContainerUtils;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.api.ingameguide.ActionLocation;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import com.github.aws404.controlifywynn.mixin.client.HandledScreenAccessor;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.util.Window;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.Optional;

public class ControlifyWynnMod implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// Add Wynntils keybinds to the ingame guWWide
		ControlifyEvents.INGAME_GUIDE_REGISTRY.register((controllerBindings, ingameGuideRegistry) -> {
			controllerBindings.registry().entrySet().stream()
					.filter(entry -> Objects.equals(entry.getKey().getNamespace(), "wynntils"))
					.forEach((entry) -> ingameGuideRegistry.registerGuideAction(
							entry.getValue(),
							ActionLocation.RIGHT,
							ingameGuideContext -> Optional.of(Text.of(entry.getValue().override().keyMapping().getTranslationKey())))
					);
		});

		// Logic for additional controller features
		ControlifyEvents.CONTROLLER_STATE_UPDATE.register(controller -> {
			if (McUtils.mc() == null || McUtils.player() == null || McUtils.containerMenu() == null) {
				return;
			}
			Screen screen = McUtils.mc().currentScreen;

			if (!(screen instanceof GenericContainerScreen gui)) return;

			// Go to next page if next tab is pressed in a recognised menu
			if (controller.bindings().GUI_NEXT_TAB.justPressed()) {
				boolean scrollUp = Managers.Feature.getFeatureInstance(ContainerScrollFeature.class).invertScroll.get();
				Optional<Integer> slot = Models.Container.getScrollSlot(gui, scrollUp);
				if (slot.isEmpty()) return;

				ContainerUtils.clickOnSlot(
						slot.get(),
						gui.getScreenHandler().syncId,
						GLFW.GLFW_MOUSE_BUTTON_LEFT,
						gui.getScreenHandler().getStacks()
				);
			}

			// Go to previous page if next tab is pressed in a recognised menu
			if (controller.bindings().GUI_PREV_TAB.justPressed()) {
				boolean scrollUp = !Managers.Feature.getFeatureInstance(ContainerScrollFeature.class).invertScroll.get();
				Optional<Integer> slot = Models.Container.getScrollSlot(gui, scrollUp);
				if (slot.isEmpty()) return;

				ContainerUtils.clickOnSlot(
						slot.get(),
						gui.getScreenHandler().syncId,
						GLFW.GLFW_MOUSE_BUTTON_LEFT,
						gui.getScreenHandler().getStacks()
				);
			}

			// Identifier and Blacksmith quick action hotkey
			if (
					controller.bindings().GUI_ABSTRACT_ACTION_1.justPressed() &&
					(screen.getTitle().getString().contains("Item Identifier") || screen.getTitle().getString().matches("What would you like to (scrap|sell)\\?"))
			) {
				if (Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled()) {
					Window window = McUtils.mc().getWindow();
					Slot slot = gui.getScreenHandler().getSlot(8);
					int slotXPos = ((HandledScreenAccessor) gui).getX() + slot.x + 8;
					int slotYPos = ((HandledScreenAccessor) gui).getY() + slot.y + 8;

					Vector2d scaleFactor = new Vector2d((double)window.getScaledWidth() / (double)window.getWidth(), (double)window.getScaledHeight() / (double)window.getHeight());

					Controlify.instance().virtualMouseHandler().snapToPoint(new SnapPoint(slotXPos, slotYPos, 0), scaleFactor);
				}

				// Clear the state so no further actions occur
				controller.clearState();
			}
		});
	}
}