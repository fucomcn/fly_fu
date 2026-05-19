package org.fu.fly_fu.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;

public class ModConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.DoubleValue horizontalSpeed;
    public static final ForgeConfigSpec.DoubleValue verticalSpeed;

    static {
        BUILDER.comment("Fly Fu Speed Settings").push("flight");

        horizontalSpeed = BUILDER
                .comment("Horizontal flight speed multiplier (default: 0.05)")
                .defineInRange("horizontalSpeed", 0.05, 0.001, 1.0);

        verticalSpeed = BUILDER
                .comment("Vertical flight speed when ascending/descending (default: 0.1)")
                .defineInRange("verticalSpeed", 0.1, 0.001, 1.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static void register() {
        // 使用完全限定名避免与本类名冲突
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, SPEC);
    }
}