package org.fu.fly_fu.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.fu.fly_fu.Fly_fu;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Fly_fu.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.registerMessage(0, ToggleFlightPacket.class,
                ToggleFlightPacket::encode,
                ToggleFlightPacket::decode,
                ToggleFlightPacket::handle);
        INSTANCE.registerMessage(1, FlightInputPacket.class,
                FlightInputPacket::encode,
                FlightInputPacket::decode,
                FlightInputPacket::handle);
    }
}