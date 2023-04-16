package com.lypaka.bettergyms.Commands;

import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.GUIs.BadgeCaseCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

/**
 * FUCK Brigadier
 */
@Mod.EventBusSubscriber(modid = BetterGyms.MOD_ID)
public class BetterGymsCommand {

    public static List<String> ALIASES = Arrays.asList("bettergyms", "bgyms", "gyms");

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new BadgeCaseCommand(event.getDispatcher());
        new ClearCommand(event.getDispatcher());
        new GiveBadgeCommand(event.getDispatcher());
        new InfoCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
