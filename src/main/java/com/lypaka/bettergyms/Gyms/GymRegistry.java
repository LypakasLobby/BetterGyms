package com.lypaka.bettergyms.Gyms;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.*;

public class GymRegistry {

    public static Map<UUID, String> playerGymLeaderMap = new HashMap<>();

    public static void registerGyms() throws ObjectMappingException {

        for (Map.Entry<String, BasicConfigManager> entry : BetterGyms.gymConfigManager.entrySet()) {

            String gymName = entry.getKey();
            BasicConfigManager configManager = entry.getValue();
            String gymDisplayIcon = configManager.getConfigNode(0, "Gym-Display-Icon").getString();
            String gymLocation = configManager.getConfigNode(0, "Gym-Location").getString();
            int range = configManager.getConfigNode(0, "Gym-Range").getInt();
            String theme = configManager.getConfigNode(0, "Gym-Theme").getString();
            List<String> permissions = new ArrayList<>();
            if (!configManager.getConfigNode(0, "Permissions").isVirtual()) {

                permissions = configManager.getConfigNode(0, "Permissions").getList(TypeToken.of(String.class));

            }
            String npcLocation = configManager.getConfigNode(0, "NPC-Location").getString();
            String playerUUID = configManager.getConfigNode(0, "Player-UUID").getString();
            if (!playerUUID.equalsIgnoreCase("")) {

                UUID uuid = UUID.fromString(playerUUID);
                playerGymLeaderMap.put(uuid, gymName);

            }
            List<String> rewards = configManager.getConfigNode(0, "Rewards").getList(TypeToken.of(String.class));

            Gym gym = new Gym(gymName, gymDisplayIcon, gymLocation, range, theme, permissions, npcLocation, playerUUID, rewards);
            BetterGyms.gyms.add(gym);
            BetterGyms.logger.info("Successfully registered gym: " + gymName + "!");

        }

    }

    public static void reloadGyms() throws ObjectMappingException {

        playerGymLeaderMap = new HashMap<>();
        for (Map.Entry<String, BasicConfigManager> entry : BetterGyms.gymConfigManager.entrySet()) {

            String gymName = entry.getKey();
            BasicConfigManager configManager = entry.getValue();
            String gymDisplayIcon = configManager.getConfigNode(0, "Gym-Display-Icon").getString();
            String gymLocation = configManager.getConfigNode(0, "Gym-Location").getString();
            int range = configManager.getConfigNode(0, "Gym-Range").getInt();
            String theme = configManager.getConfigNode(0, "Gym-Theme").getString();
            List<String> permissions = new ArrayList<>();
            if (!configManager.getConfigNode(0, "Permissions").isVirtual()) {

                permissions = configManager.getConfigNode(0, "Permissions").getList(TypeToken.of(String.class));

            }
            String npcLocation = configManager.getConfigNode(0, "NPC-Location").getString();
            String playerUUID = configManager.getConfigNode(0, "Player-UUID").getString();
            if (!playerUUID.equalsIgnoreCase("")) {

                UUID uuid = UUID.fromString(playerUUID);
                playerGymLeaderMap.put(uuid, gymName);

            }
            List<String> rewards = configManager.getConfigNode(0, "Rewards").getList(TypeToken.of(String.class));
            Gym gym;
            if (Gym.getFromName(gymName) != null) {

                gym = Gym.getFromName(gymName);
                gym = gym.reload(gymDisplayIcon, gymLocation, range, theme, permissions, npcLocation, playerUUID, rewards);
                BetterGyms.gyms.removeIf(gymEntry -> gymEntry.getName().equalsIgnoreCase(gymName));
                BetterGyms.logger.info("Successfully reloaded gym: " + gymName + "!");

            } else {

                gym = new Gym(gymName, gymDisplayIcon, gymLocation, range, theme, permissions, npcLocation, playerUUID, rewards);
                BetterGyms.logger.info("Successfully registered gym: " + gymName + "!");

            }

            BetterGyms.gyms.add(gym);

        }

    }

}
