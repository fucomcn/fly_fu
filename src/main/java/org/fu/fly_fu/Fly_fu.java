package org.fu.fly_fu;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.api.distmarker.Dist;
import org.fu.fly_fu.config.ModConfig;
import org.fu.fly_fu.event.ServerEventHandler;
import org.fu.fly_fu.network.NetworkHandler;

@Mod(Fly_fu.MODID)
public class Fly_fu {
    public static final String MODID = "fly_fu";

    public Fly_fu() {
        ModConfig.register();
        NetworkHandler.register();
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
    }
}