package com.lypaka.bettergyms.Commands;

import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.Gyms.Gym;
import com.lypaka.bettergyms.Gyms.GymBadge;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class GiveBadgeCommand {

    public GiveBadgeCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterGymsCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("givebadge")
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
                                                        String gymName = StringArgumentType.getString(c, "gym");
                                                        Gym gym = null;
                                                        for (Gym g : BetterGyms.gyms) {

                                                            if (g.getName().equalsIgnoreCase(gymName)) {

                                                                gym = g;
                                                                break;

                                                            }

                                                        }

                                                        if (gym == null) {

                                                            c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid Gym name!"));
                                                            return 0;

                                                        }

                                                        try {

                                                            ItemStack badge = GymBadge.buildGymBadge(gym);
                                                            badge.setCount(1);
                                                            if (target.addItemStackToInventory(badge)) {

                                                                c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully gave " + target.getName().getString() + " the badge for the " + gym.getName() + " Gym!"), true);

                                                            }

                                                        } catch (ObjectMappingException e) {

                                                            e.printStackTrace();

                                                        }

                                                        return 0;

                                                    })
                                            )
                                    )
                            )
            );

        }

    }

}
