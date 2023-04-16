package com.lypaka.bettergyms.Commands;

import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.GUIs.GymInfoMenu;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class InfoCommand {

    public InfoCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterGymsCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("info")
                                    .executes(c -> {

                                        if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                            ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                            GymInfoMenu.open(player);

                                        }

                                        return 0;

                                    })
                            )
            );

        }

    }

}
