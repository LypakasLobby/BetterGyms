package com.lypaka.bettergyms.Commands;

import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.PlayerAccounts.Account;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ClearCommand {

    public ClearCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterGymsCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("clear")
                                    .then(Commands.argument("player", EntityArgument.players())
                                            .then(Commands.argument("gym", StringArgumentType.string())
                                                    .executes(c -> {

                                                        if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                            ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                            if (!PermissionHandler.hasPermission(player, "bettergyms.command.admin")) {

                                                                player.sendMessage(FancyText.getFormattedText("&cYou do not have permission to use this command!"), player.getUniqueID());
                                                                return 0;

                                                            }

                                                        }

                                                        ServerPlayerEntity target = EntityArgument.getPlayer(c, "player");
                                                        String gym = StringArgumentType.getString(c, "gym");
                                                        Account account = BetterGyms.accountMap.get(target.getUniqueID());
                                                        if (gym.equalsIgnoreCase("all")) {

                                                            account.clearGyms();

                                                        } else {

                                                            account.clearGym(gym);

                                                        }
                                                        account.save();
                                                        c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully cleared " + target.getName().getString() + "'s completed Gym(s)!"), true);
                                                        return 0;

                                                    })
                                            )
                                    )
                            )
            );

        }

    }

}
