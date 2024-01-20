package com.github.aws404.controlifywynn.processors;

import com.wynntils.core.consumers.screens.WynntilsScreen;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;

public class WynntilsScreenProcessor<T extends WynntilsScreen> extends ScreenProcessor<T> {
    public WynntilsScreenProcessor(T screen) {
        super(screen);
    }

    @Override
    public VirtualMouseBehaviour virtualMouseBehaviour() {
        return VirtualMouseBehaviour.ENABLED;
    }
}
