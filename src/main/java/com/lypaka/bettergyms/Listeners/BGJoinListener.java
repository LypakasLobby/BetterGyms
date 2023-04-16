package com.lypaka.bettergyms.Listeners;

import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.ConfigGetters;
import com.lypaka.bettergyms.Gyms.GymRegistry;
import com.lypaka.bettergyms.PlayerAccounts.Account;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class BGJoinListener {

    @SubscribeEvent
    public void onJoin (PlayerEvent.PlayerLoggedInEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ConfigGetters.uuidStorage.put(player.getUniqueID().toString(), player.getName().getString());
        BetterGyms.playerConfigManager.loadPlayer(player.getUniqueID());
        if (!BetterGyms.accountMap.containsKey(player.getUniqueID())) {

            Account account = new Account(player);
            account.load();

        }

        if (!ConfigGetters.joinMessage.equalsIgnoreCase("")) {

            if (GymRegistry.playerGymLeaderMap.containsKey(player.getUniqueID())) {

                String gymName = GymRegistry.playerGymLeaderMap.get(player.getUniqueID());
                player.world.getPlayers().forEach(p -> p.sendMessage(FancyText.getFormattedText(ConfigGetters.joinMessage.replace("%gym%", gymName).replace("%player%", player.getName().getString())), p.getUniqueID()));

            }

        }

    }

    @SubscribeEvent
    public void onLeave (PlayerEvent.PlayerLoggedOutEvent event) {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (!ConfigGetters.leaveMessage.equalsIgnoreCase("")) {

            if (GymRegistry.playerGymLeaderMap.containsKey(player.getUniqueID())) {

                String gymName = GymRegistry.playerGymLeaderMap.get(player.getUniqueID());
                player.world.getPlayers().forEach(p -> p.sendMessage(FancyText.getFormattedText(ConfigGetters.leaveMessage.replace("%gym%", gymName).replace("%player%", player.getName().getString())), player.getUniqueID()));


            }

        }

    }

}
