package org.fu.fly_fu.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.fu.fly_fu.Fly_fu;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyMapping toggleFlight;

    public static void register() {
        toggleFlight = new KeyMapping(
                "key." + Fly_fu.MOD_ID + ".toggle_flight",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G, // 默认按键G
                "category." + Fly_fu.MOD_ID + ".controls"
        );

        net.minecraftforge.client.event.RegisterKeyMappingsEvent event = null;
        if (event != null) {
            event.register(toggleFlight);
        }
    }
}