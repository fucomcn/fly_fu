package org.fu.fly_fu.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class ToggleFlightPacket {

    public ToggleFlightPacket() {}

    public static void encode(ToggleFlightPacket msg, FriendlyByteBuf buf) {}

    public static ToggleFlightPacket decode(FriendlyByteBuf buf) {
        return new ToggleFlightPacket();
    }

    public static void handle(ToggleFlightPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                boolean enabled = player.getPersistentData().getBoolean("fly_fu_enabled");
                player.getPersistentData().putBoolean("fly_fu_enabled", !enabled);
                if (!enabled) {
                    // 开启飞行
                    player.getAbilities().mayfly = true;
                    player.getAbilities().flying = true;
                } else {
                    // 关闭飞行
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    // 清除按键残留状态
                    player.getPersistentData().putBoolean("fly_fu_jump", false);
                    player.getPersistentData().putBoolean("fly_fu_sneak", false);
                }
                player.onUpdateAbilities();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}