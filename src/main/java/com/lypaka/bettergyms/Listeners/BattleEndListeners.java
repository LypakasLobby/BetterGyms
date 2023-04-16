package com.lypaka.bettergyms.Listeners;

import com.lypaka.bettergyms.API.BeatGymEvent;
import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.ConfigGetters;
import com.lypaka.bettergyms.Gyms.Gym;
import com.lypaka.bettergyms.Gyms.GymBadge;
import com.lypaka.bettergyms.Gyms.GymRegistry;
import com.lypaka.bettergyms.PlayerAccounts.Account;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.WorldDimGetter;
import com.lypaka.lypakautils.WorldMap;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class BattleEndListeners {

    @SubscribeEvent
    public void onNPCDefeat (BeatTrainerEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = event.player;
        NPCTrainer trainer = event.trainer;
        String worldName = player.getServerWorld().getWorld().toString().replace("ServerLevel[", "").replace("]", "");
        String location = worldName + "," + trainer.getPosition().getX() + "," + trainer.getPosition().getY() + "," + trainer.getPosition().getZ();
        Gym gym = Gym.getFromNPCLocation(location);
        if (gym != null) {

            Account account = BetterGyms.accountMap.get(player.getUniqueID());
            if (!account.getBeatenGyms().contains(gym.getName())) {

                account.addGym(gym.getName());
                account.save();
                if (!ConfigGetters.beatMessage.equalsIgnoreCase("")) {

                    player.world.getPlayers().forEach(p -> p.sendMessage(FancyText.getFormattedText(ConfigGetters.beatMessage.replace("%gym%", gym.getName()).replace("%player%", player.getName().getString())), p.getUniqueID()));

                }

                ItemStack badge = GymBadge.buildGymBadge(gym);
                player.addItemStackToInventory(badge);
                if (!gym.getRewards().isEmpty()) {

                    for (String c : gym.getRewards()) {

                        player.world.getServer().getCommandManager().handleCommand(player.world.getServer().getCommandSource(), c.replaceAll("%player%", player.getName().getString()));

                    }

                }

                BeatGymEvent beatGymEvent = new BeatGymEvent(player, gym);
                MinecraftForge.EVENT_BUS.post(beatGymEvent);

            }

        }

    }

    @SubscribeEvent
    public void onPlayerDefeat (BattleEndEvent event) throws ObjectMappingException {

        BattleController bcb = event.getBattleController();
        if (bcb.participants.get(0) instanceof PlayerParticipant && bcb.participants.get(1) instanceof PlayerParticipant) {

            PlayerParticipant pp1 = (PlayerParticipant) bcb.participants.get(0);
            PlayerParticipant pp2 = (PlayerParticipant) bcb.participants.get(1);
            ServerPlayerEntity winner;
            ServerPlayerEntity loser;

            if (pp1.isDefeated) {

                winner = pp2.player;
                loser = pp1.player;

            } else {

                winner = pp1.player;
                loser = pp2.player;

            }

            if (GymRegistry.playerGymLeaderMap.containsKey(loser.getUniqueID())) {

                Gym gym = Gym.getFromPlayerLeader(loser.getUniqueID().toString());
                Account account = BetterGyms.accountMap.get(winner.getUniqueID());
                if (!account.getBeatenGyms().contains(gym.getName())) {

                    World loserWorld = loser.world;
                    int leaderWorld = WorldDimGetter.getDimID(loserWorld);
                    String gymLocation = gym.getGymLocation();
                    World w = WorldMap.worldMap.get(gymLocation.split(",")[0]);
                    int gymWorld = WorldDimGetter.getDimID(w);
                    if (leaderWorld == gymWorld) {

                        int x = Integer.parseInt(gymLocation.split(",")[1]);
                        int y = Integer.parseInt(gymLocation.split(",")[2]);
                        int z = Integer.parseInt(gymLocation.split(",")[3]);
                        //double distance = loser.getDistance(x, y, z);
                        double distance = Math.pow(loser.getDistanceSq(x, y, z), 2);
                        if (distance <= gym.getRange()) {

                            account.addGym(gym.getName());
                            account.save();
                            if (!ConfigGetters.beatMessage.equalsIgnoreCase("")) {

                                winner.world.getPlayers().forEach(p -> p.sendMessage(FancyText.getFormattedText(ConfigGetters.beatMessage.replace("%gym%", gym.getName()).replace("%player%", winner.getName().getString())), p.getUniqueID()));

                            }

                            ItemStack badge = GymBadge.buildGymBadge(gym);
                            winner.addItemStackToInventory(badge);
                            if (!gym.getRewards().isEmpty()) {

                                for (String c : gym.getRewards()) {

                                    winner.world.getServer().getCommandManager().handleCommand(winner.world.getServer().getCommandSource(), c.replaceAll("%player%", winner.getName().getString()));

                                }

                            }

                            BeatGymEvent beatGymEvent = new BeatGymEvent(winner, gym);
                            MinecraftForge.EVENT_BUS.post(beatGymEvent);

                        }

                    }

                }

            }

        }

    }

}
