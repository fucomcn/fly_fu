package org.fu.fly_fu.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.fu.fly_fu.Fly_fu;
import org.fu.fly_fu.config.ModConfig;
import org.fu.fly_fu.keybind.KeyBindings;

@Mod.EventBusSubscriber(modid = Fly_fu.MOD_ID, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class EventHandler {
    private static boolean flightEnabled = false;
    private static boolean wasOnGround = false;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        // 处理飞行切换按键
        if (KeyBindings.toggleFlight.consumeClick()) {
            toggleFlight(mc.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Minecraft mc = Minecraft.getInstance();

        // 只处理本地玩家
        if (player != mc.player) return;

        // 保持生存模式下的飞行权限
        if (flightEnabled) {
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
        }

        // 触地不关闭飞行
        if (ModConfig.KEEP_FLIGHT_ON_GROUND.get() && flightEnabled) {
            if (player.onGround() && !wasOnGround) {
                // 刚接触地面时，重新启用飞行
                player.getAbilities().flying = true;
                player.onGround = false; // 防止游戏自动关闭飞行
            }
        }
        wasOnGround = player.onGround();

        // 取消飞行惯性
        if (ModConfig.DISABLE_FLIGHT_INERTIA.get() && player.getAbilities().flying) {
            // 水平惯性取消
            if (!player.input.forwardImpulse && !player.input.leftImpulse &&
                    !player.input.backwardImpulse && !player.input.rightImpulse) {
                player.setDeltaMovement(player.getDeltaMovement().multiply(0, 1, 0));
            }

            // 垂直惯性取消
            if (!player.input.jumping && !player.input.shiftKeyDown) {
                player.setDeltaMovement(player.getDeltaMovement().multiply(1, 0, 1));
            }
        }

        // 应用自定义飞行速度
        if (player.getAbilities().flying) {
            // 水平速度
            float horizontalSpeed = 0.05f * ModConfig.HORIZONTAL_FLIGHT_SPEED.get().floatValue();
            player.getAbilities().setFlyingSpeed(horizontalSpeed);

            // 垂直速度
            double verticalSpeed = 0.06 * ModConfig.VERTICAL_FLIGHT_SPEED.get();
            if (player.input.jumping) {
                player.setDeltaMovement(player.getDeltaMovement().add(0, verticalSpeed, 0));
            }
            if (player.input.shiftKeyDown) {
                player.setDeltaMovement(player.getDeltaMovement().add(0, -verticalSpeed, 0));
            }
        }
    }

    private static void toggleFlight(Player player) {
        flightEnabled = !flightEnabled;

        if (flightEnabled) {
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("Flight enabled"), true);
        } else {
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("Flight disabled"), true);
        }

        player.onUpdateAbilities();
    }
}