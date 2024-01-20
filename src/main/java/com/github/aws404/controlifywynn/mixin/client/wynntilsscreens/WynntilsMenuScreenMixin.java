package com.github.aws404.controlifywynn.mixin.client.wynntilsscreens;

import com.wynntils.screens.wynntilsmenu.WynntilsMenuScreen;
import com.wynntils.screens.wynntilsmenu.widgets.WynntilsMenuButton;
import com.wynntils.utils.render.Texture;
import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(value = WynntilsMenuScreen.class, remap = false)
public abstract class WynntilsMenuScreenMixin extends Screen implements ISnapBehaviour {
    @Shadow(remap = false) @Final private List<List<WynntilsMenuButton>> buttons;

    protected WynntilsMenuScreenMixin(Text title) { super(title); }

    /**
     * Add snap points to the gui
     */
    @Override
    public Set<SnapPoint> getSnapPoints() {
        Set<SnapPoint> snapPoints = new HashSet<>();

        for(int row = 0; row < this.buttons.size(); ++row) {
            for (int col = 0; col < this.buttons.get(row).size(); ++col) {
                int x = col * 35;
                int y = row * 35;

                snapPoints.add(new SnapPoint(x + (this.width - Texture.CONTENT_BOOK_BACKGROUND.width()) / 2 + 35, y + (this.height - Texture.CONTENT_BOOK_BACKGROUND.height()) / 2 + 65, 17));
            }
        }

        return snapPoints;
    }
}
