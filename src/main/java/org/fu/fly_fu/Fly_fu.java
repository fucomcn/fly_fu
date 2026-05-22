package org.fu.fly_fu;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.fu.fly_fu.config.FlyConfig;
import org.fu.fly_fu.event.EventHandler;
import org.fu.fly_fu.keybind.KeyBindings;
import org.slf4j.Logger;

@Mod(Fly_fu.MOD_ID)
public class Fly_fu {
    public static final String MOD_ID = "fly_fu";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Fly_fu(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // ✅ 最新Forge 47.2.0+ 正确的配置注册方式
        context.registerConfig(ModConfig.Type.CLIENT, FlyConfig.SPEC, MOD_ID + ".toml");

        // 注册事件
        MinecraftForge.EVENT_BUS.register(EventHandler.class);

        // 客户端初始化
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            KeyBindings.register();
            LOGGER.info("Fly Fu 飞行模组加载成功！");
        });
    }
}