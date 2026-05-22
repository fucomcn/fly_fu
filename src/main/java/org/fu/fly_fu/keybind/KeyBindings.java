package org.fu.fly_fu.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import org.fu.fly_fu.Fly_fu;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final Lazy<KeyMapping> TOGGLE_FLIGHT = Lazy.of(() -> new KeyMapping(
            "key.fly_fu.toggle_flight",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.fly_fu"
    ));

    public static void register() {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener((RegisterKeyMappingsEvent event) -> {
            event.register(TOGGLE_FLIGHT.get());
        });
    }
}