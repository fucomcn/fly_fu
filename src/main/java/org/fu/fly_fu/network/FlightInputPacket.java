package org.fu.fly_fu.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FlightInputPacket {
    private final boolean jump;
    private final boolean sneak;

    public FlightInputPacket(boolean jump, boolean sneak) {
        this.jump = jump;
        this.sneak = sneak;
    }

    public static void encode(FlightInputPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.jump);
        buf.writeBoolean(msg.sneak);
    }

    public static FlightInputPacket decode(FriendlyByteBuf buf) {
        return new FlightInputPacket(buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(FlightInputPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.getPersistentData().putBoolean("fly_fu_jump", msg.jump);
                player.getPersistentData().putBoolean("fly_fu_sneak", msg.sneak);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}