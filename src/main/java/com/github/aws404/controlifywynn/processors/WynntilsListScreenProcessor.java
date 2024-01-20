package com.github.aws404.controlifywynn.processors;

import com.wynntils.screens.base.WynntilsListScreen;
import com.wynntils.screens.base.widgets.WynntilsButton;
import com.wynntils.screens.wynntilsmenu.WynntilsMenuScreen;
import dev.isxander.controlify.controller.Controller;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;

import com.github.aws404.controlifywynn.mixin.client.wynntilsscreens.WynntilsListScreenMixin;

public class WynntilsListScreenProcessor<T extends WynntilsListScreen<?, ?>> extends ScreenProcessor<T> {
    public WynntilsListScreenProcessor(T screen) {
        super(screen);
    }

    @Override
    protected void handleScreenVMouse(Controller<?, ?> controller, VirtualMouseHandler vmouse) {
        if (controller.bindings().GUI_NEXT_TAB.justPressed()) {
            this.screen.setCurrentPage(this.screen.getCurrentPage() + 1);
        } else if (controller.bindings().GUI_PREV_TAB.justPressed()) {
            this.screen.setCurrentPage(this.screen.getCurrentPage() - 1);
        }
    }

    @Override
    public VirtualMouseBehaviour virtualMouseBehaviour() {
        return VirtualMouseBehaviour.ENABLED;
    }
}
