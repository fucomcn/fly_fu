package org.fu.fly_fu;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.fu.fly_fu.config.ModConfig;
import org.fu.fly_fu.event.EventHandler;
import org.fu.fly_fu.keybind.KeyBindings;
import org.slf4j.Logger;

@Mod(Fly_fu.MOD_ID)
public class Fly_fu {
    public static final String MOD_ID = "fly_fu";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Fly_fu() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册配置
        ModConfig.register();

        // 注册客户端事件
        modEventBus.addListener(this::onClientSetup);

        // 注册主事件总线
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            // 注册按键绑定
            KeyBindings.register();

            // 注册事件处理器
            MinecraftForge.EVENT_BUS.register(EventHandler.class);

            LOGGER.info("Fly Fu mod loaded successfully!");
        });
    }
}