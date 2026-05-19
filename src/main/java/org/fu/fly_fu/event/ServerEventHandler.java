package org.fu.fly_fu.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import org.fu.fly_fu.config.ModConfig;

public class ServerEventHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side != LogicalSide.SERVER) return;
        Player player = event.player;
        boolean flightEnabled = player.getPersistentData().getBoolean("fly_fu_enabled");

        if (flightEnabled) {
            player.getAbilities().mayfly = true;
            if (!player.getAbilities().flying) {
                player.getAbilities().flying = true;
                player.onUpdateAbilities();
            }
            applyFlightMovement(player);
        } else {
            if (player.getAbilities().mayfly) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
    }

    private void applyFlightMovement(Player player) {
        double hSpeed = ModConfig.horizontalSpeed.get();
        double vSpeed = ModConfig.verticalSpeed.get();

        float moveForward = player.zza;
        float moveStrafe  = player.xxa;

        boolean jump  = player.getPersistentData().getBoolean("fly_fu_jump");
        boolean sneak = player.getPersistentData().getBoolean("fly_fu_sneak");

        float yaw = player.getYRot();
        double rad = Math.toRadians(yaw);
        double sin = Math.sin(rad);
        double cos = Math.cos(rad);

        double vx = (moveStrafe * cos + moveForward * sin) * hSpeed;
        double vz = (moveStrafe * sin - moveForward * cos) * hSpeed;

        double vy;
        if (jump) {
            vy = vSpeed;
        } else if (sneak) {
            vy = -vSpeed;
        } else {
            vy = 0;
        }

        player.setDeltaMovement(vx, vy, vz);
        player.fallDistance = 0f;
        player.hurtMarked = true;
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        boolean wasFlying = event.getOriginal().getPersistentData().getBoolean("fly_fu_enabled");
        if (wasFlying) {
            event.getEntity().getPersistentData().putBoolean("fly_fu_enabled", true);
        }
    }
}