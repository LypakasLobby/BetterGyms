package com.lypaka.bettergyms.Listeners;

import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.Gyms.GymRegistry;
import com.lypaka.lypakautils.ModVerification;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

@Mod.EventBusSubscriber(modid = BetterGyms.MOD_ID)
public class ServerStartingListener {

    @SubscribeEvent
    public static void onServerStarting (FMLServerStartingEvent event) throws ObjectMappingException {

        GymRegistry.registerGyms();
        MinecraftForge.EVENT_BUS.register(new BGJoinListener());
        Pixelmon.EVENT_BUS.register(new BattleStartListener());
        Pixelmon.EVENT_BUS.register(new BattleEndListeners());

    }

}
