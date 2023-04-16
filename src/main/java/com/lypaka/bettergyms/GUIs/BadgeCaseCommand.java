package com.lypaka.bettergyms.GUIs;

import com.lypaka.bettergyms.Commands.BadgecaseMenu;
import com.lypaka.bettergyms.Commands.BetterGymsCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class BadgeCaseCommand {

    public BadgeCaseCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterGymsCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("badges")
                                    .executes(c -> {

                                        if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                            ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                            try {

                                                BadgecaseMenu.open(player);

                                            } catch (ObjectMappingException e) {

                                                e.printStackTrace();

                                            }

                                        }

                                        return 0;

                                    })
                            )
            );

        }

    }

}
