package org.fu.fly_fu.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.fu.fly_fu.Fly_fu;
import org.fu.fly_fu.config.FlyConfig;
import org.fu.fly_fu.keybind.KeyBindings;

@Mod.EventBusSubscriber(modid = Fly_fu.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    private static boolean flightEnabled = false;
    private static boolean lastKeyPressed = false;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        boolean currentPressed = KeyBindings.TOGGLE_FLIGHT.get().isDown();
        if (currentPressed && !lastKeyPressed) {
            toggleFlight(mc.player);
        }
        lastKeyPressed = currentPressed;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Minecraft mc = Minecraft.getInstance();

        if (player.level().isClientSide && player == mc.player) {
            updateFlightState(player, mc);
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            Minecraft mc = Minecraft.getInstance();
            if (player == mc.player && flightEnabled) {
                event.setCanceled(true);
            }
        }
    }

    private static void toggleFlight(Player player) {
        flightEnabled = !flightEnabled;

        if (flightEnabled) {
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("§a✓ 飞行已开启 (免疫摔落)"), true);
        } else {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("§c✗ 飞行已关闭"), true);
        }
        player.onUpdateAbilities();
    }

    private static void updateFlightState(Player player, Minecraft mc) {
        if (!flightEnabled) return;

        // 强制保持飞行
        player.getAbilities().mayfly = true;
        player.getAbilities().flying = true;

        // 触地不关闭飞行
        if (FlyConfig.KEEP_FLIGHT_ON_GROUND.get() && player.onGround()) {
            player.getAbilities().flying = true;
        }

        var input = mc.player.input;
        double vSpeed = FlyConfig.VERTICAL_SPEED.get();

        // ✅ 完全修复Shift+空格悬停问题
        // 优先处理同时按键的情况
        if (player.getAbilities().flying) {
            if (input.jumping && input.shiftKeyDown) {
                // 同时按下跳跃和下蹲：完全静止悬停
                player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
            } else if (input.jumping) {
                // 只按跳跃：向上飞
                player.setDeltaMovement(player.getDeltaMovement().x, 0.06 * vSpeed, player.getDeltaMovement().z);
            } else if (input.shiftKeyDown) {
                // 只按下蹲：向下飞
                player.setDeltaMovement(player.getDeltaMovement().x, -0.06 * vSpeed, player.getDeltaMovement().z);
            } else if (FlyConfig.DISABLE_INERTIA.get()) {
                // 都没按且无惯性：垂直速度置0
                player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
            }
        }

        // 取消水平飞行惯性
        if (FlyConfig.DISABLE_INERTIA.get() && player.getAbilities().flying) {
            if (!input.up && !input.down && !input.left && !input.right) {
                player.setDeltaMovement(0, player.getDeltaMovement().y, 0);
            }
        }

        // 应用自定义水平飞行速度
        float hSpeed = FlyConfig.HORIZONTAL_SPEED.get().floatValue();
        player.getAbilities().setFlyingSpeed(0.05F * hSpeed);
    }
}