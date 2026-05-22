package org.fu.fly_fu.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class FlyConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue HORIZONTAL_SPEED;
    public static final ForgeConfigSpec.DoubleValue VERTICAL_SPEED;
    public static final ForgeConfigSpec.BooleanValue DISABLE_INERTIA;
    public static final ForgeConfigSpec.BooleanValue KEEP_FLIGHT_ON_GROUND;

    static {
        BUILDER.comment("Fly Fu 飞行设置").push("flight");

        HORIZONTAL_SPEED = BUILDER
                .comment("水平飞行速度倍数 (默认: 1.0 = 创造模式速度, 范围: 0.001~100.0)")
                .defineInRange("horizontal_speed", 1.0, 0.001, 100.0);

        VERTICAL_SPEED = BUILDER
                .comment("垂直飞行速度倍数 (默认: 1.0 = 创造模式速度, 范围: 0.001~100.0)")
                .defineInRange("vertical_speed", 1.0, 0.001, 100.0);

        DISABLE_INERTIA = BUILDER
                .comment("取消飞行惯性 (松开按键立即停止)")
                .define("disable_inertia", true);

        KEEP_FLIGHT_ON_GROUND = BUILDER
                .comment("触地时保持飞行状态 (不会自动退出飞行)")
                .define("keep_flight_on_ground", true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}