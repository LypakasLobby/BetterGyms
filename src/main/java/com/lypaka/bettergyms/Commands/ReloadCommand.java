package com.lypaka.bettergyms.Commands;

import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.ConfigGetters;
import com.lypaka.bettergyms.Gyms.GymRegistry;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReloadCommand {

    public ReloadCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterGymsCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(Commands.literal("reload")
                                    .executes(c -> {

                                        if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                            ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                            if (!PermissionHandler.hasPermission(player, "bettergyms.command.admin")) {

                                                player.sendMessage(FancyText.getFormattedText("&cYou do not have permission to use this command!"), player.getUniqueID());
                                                return 0;

                                            }

                                        }

                                        try {

                                            BetterGyms.configManager.load();
                                            ConfigGetters.load();
                                            String[] gymFiles = new String[]{"settings.conf"};
                                            for (String gymName : ConfigGetters.gymNames) {

                                                BasicConfigManager bcm;
                                                if (BetterGyms.gymConfigManager.containsKey(gymName)) {

                                                    bcm = BetterGyms.gymConfigManager.get(gymName);
                                                    bcm.load();

                                                } else {

                                                    Path gymDir = ConfigUtils.checkDir(Paths.get("./config/bettergyms/gyms/" + gymName));
                                                    bcm = new BasicConfigManager(gymFiles, gymDir, BetterGyms.class, BetterGyms.MOD_NAME, BetterGyms.MOD_ID, BetterGyms.logger);
                                                    bcm.init();

                                                }
                                                BetterGyms.gymConfigManager.put(gymName, bcm);

                                            }
                                            GymRegistry.reloadGyms();
                                            c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully reloaded BetterGyms I hope!"), true);

                                        } catch (ObjectMappingException | IOException e) {

                                            e.printStackTrace();

                                        }

                                        return 1;
                                    })
                            )
            );

        }

    }

}
