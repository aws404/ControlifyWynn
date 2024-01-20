package com.github.aws404.controlifywynn.processors;

import com.wynntils.screens.maps.AbstractMapScreen;
import dev.isxander.controlify.controller.Controller;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;

public class WynntilsMapScreenProcessor extends ScreenProcessor<AbstractMapScreen> {
    public WynntilsMapScreenProcessor(AbstractMapScreen screen) {
        super(screen);
    }

    @Override
    protected void handleScreenVMouse(Controller<?, ?> controller, VirtualMouseHandler vmouse) {
        if (controller.bindings().GUI_ABSTRACT_ACTION_2.held()) {
            this.screen.doMouseClicked(vmouse.getCurrentX(0) / 2F, vmouse.getCurrentY(0) / 2F, 2);
        }
    }

    @Override
    public VirtualMouseBehaviour virtualMouseBehaviour() {
        return VirtualMouseBehaviour.ENABLED;
    }
}
