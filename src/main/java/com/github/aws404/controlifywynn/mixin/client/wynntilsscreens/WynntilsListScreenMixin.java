package com.github.aws404.controlifywynn.mixin.client.wynntilsscreens;

import com.wynntils.screens.base.WynntilsListScreen;
import com.wynntils.screens.settings.WynntilsBookSettingsScreen;
import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import org.spongepowered.asm.mixin.Mixin;

import com.github.aws404.controlifywynn.mixin.client.WynntilsMenuScreenBaseAccessor;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.Set;
import java.util.stream.Collectors;

@Mixin(value = {WynntilsListScreen.class, WynntilsBookSettingsScreen.class, WynntilsBookSettingsScreen.class}, remap = false)
public abstract class WynntilsListScreenMixin extends Screen implements ISnapBehaviour {
    protected WynntilsListScreenMixin(Text title) { super(title); }

    /**
     * Add snap points to the gui
     */
    @Override
    public Set<SnapPoint> getSnapPoints() {
        return this.children().stream()
                .filter((child) -> child instanceof ClickableWidget)
                .map(ClickableWidget.class::cast)
                .map((widget) -> new SnapPoint(
                        (int) ((widget.getX() + widget.getWidth() / 2) + ((WynntilsMenuScreenBaseAccessor) this).callGetTranslationX()),
                        (int) ((widget.getY() + widget.getHeight() / 2) + ((WynntilsMenuScreenBaseAccessor) this).callGetTranslationY()),
                        widget.getHeight() > 10 ? Math.min(widget.getWidth(), widget.getHeight()) / 2 + 10 : 2
                ))
                .collect(Collectors.toSet());
    }
}
