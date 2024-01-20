package com.github.aws404.controlifywynn;

import com.wynntils.core.consumers.screens.WynntilsScreen;
import com.wynntils.screens.base.WynntilsListScreen;
import com.wynntils.screens.maps.AbstractMapScreen;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;

import com.github.aws404.controlifywynn.processors.WynntilsListScreenProcessor;
import com.github.aws404.controlifywynn.processors.WynntilsMapScreenProcessor;
import com.github.aws404.controlifywynn.processors.WynntilsScreenProcessor;

public class WynnControlifyEntrypoint implements ControlifyEntrypoint {
    @Override
    public void onControlifyPreInit(ControlifyApi controlifyApi) {
        ScreenProcessorProvider.registerProvider(
                WynntilsScreen.class,
                WynntilsScreenProcessor::new
        );

        ScreenProcessorProvider.registerProvider(
                WynntilsListScreen.class,
                WynntilsListScreenProcessor::new
        );

        ScreenProcessorProvider.registerProvider(
                AbstractMapScreen.class,
                WynntilsMapScreenProcessor::new
        );
    }

    @Override
    public void onControllersDiscovered(ControlifyApi controlifyApi) {

    }

}
