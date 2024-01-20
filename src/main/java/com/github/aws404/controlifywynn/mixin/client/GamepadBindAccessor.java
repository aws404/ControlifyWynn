package com.github.aws404.controlifywynn.mixin.client;

import dev.isxander.controlify.bindings.GamepadBind;
import dev.isxander.controlify.controller.gamepad.BuiltinGamepadTheme;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.util.Identifier;

@Mixin(value = GamepadBind.class, remap = false)
public interface GamepadBindAccessor {
    @Invoker(remap = false)
    Identifier callGetTexture(BuiltinGamepadTheme theme);
}
