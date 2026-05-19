package org.fu.fly_fu.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.fu.fly_fu.Fly_fu;
import org.fu.fly_fu.network.FlightInputPacket;
import org.fu.fly_fu.network.NetworkHandler;
import org.fu.fly_fu.network.ToggleFlightPacket;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Fly_fu.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    public static final KeyMapping TOGGLE_FLIGHT_KEY = new KeyMapping(
            "key.fly_fu.toggle_flight",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.movement"
    );

    private static boolean lastJump = false;
    private static boolean lastSneak = false;

    @Mod.EventBusSubscriber(modid = Fly_fu.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class KeyRegistry {
        @SubscribeEvent
        public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
            event.register(TOGGLE_FLIGHT_KEY);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        if (TOGGLE_FLIGHT_KEY.consumeClick()) {
            NetworkHandler.INSTANCE.sendToServer(new ToggleFlightPacket());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean jump = mc.options.keyJump.isDown();
        boolean sneak = mc.options.keyShift.isDown();

        if (jump != lastJump || sneak != lastSneak) {
            lastJump = jump;
            lastSneak = sneak;
            NetworkHandler.INSTANCE.sendToServer(new FlightInputPacket(jump, sneak));
        }
    }
}