package org.fu.fly_fu.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.fu.fly_fu.Fly_fu;

@Mod.EventBusSubscriber(modid = Fly_fu.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue HORIZONTAL_FLIGHT_SPEED;
    public static final ForgeConfigSpec.DoubleValue VERTICAL_FLIGHT_SPEED;
    public static final ForgeConfigSpec.BooleanValue DISABLE_FLIGHT_INERTIA;
    public static final ForgeConfigSpec.BooleanValue KEEP_FLIGHT_ON_GROUND;

    static {
        BUILDER.push("Flight Settings");

        HORIZONTAL_FLIGHT_SPEED = BUILDER
                .comment("Horizontal flight speed multiplier (default: 1.0, vanilla creative speed)")
                .defineInRange("horizontalSpeed", 1.0, 0.1, 10.0);

        VERTICAL_FLIGHT_SPEED = BUILDER
                .comment("Vertical flight speed multiplier (default: 1.0, vanilla creative speed)")
                .defineInRange("verticalSpeed", 1.0, 0.1, 10.0);

        DISABLE_FLIGHT_INERTIA = BUILDER
                .comment("Disable flight inertia (instant stop when releasing movement keys)")
                .define("disableInertia", true);

        KEEP_FLIGHT_ON_GROUND = BUILDER
                .comment("Keep flight mode enabled when touching the ground")
                .define("keepFlightOnGround", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void register() {
        // 配置会自动保存到 config/fly_fu-client.toml
    }
}