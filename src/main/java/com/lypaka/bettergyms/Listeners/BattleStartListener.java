package com.lypaka.bettergyms.Listeners;

import com.lypaka.bettergyms.Gyms.Gym;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonBuilder;
import com.pixelmonmod.pixelmon.api.storage.TrainerPartyStorage;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.BattleBuilder;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class BattleStartListener {

    private static Map<UUID, TrainerParticipant> npcMap = new HashMap<>();

    @SubscribeEvent
    public void onBattleStart (BattleStartedEvent event) {

        BattleController bcb = event.bc;
        TrainerParticipant tp = null;
        PlayerParticipant pp = null;

        if (bcb.participants.get(0) instanceof TrainerParticipant && bcb.participants.get(1) instanceof PlayerParticipant) {

            tp = (TrainerParticipant) bcb.participants.get(0);
            pp = (PlayerParticipant) bcb.participants.get(1);

        } else if (bcb.participants.get(0) instanceof PlayerParticipant && bcb.participants.get(1) instanceof TrainerParticipant) {

            tp = (TrainerParticipant) bcb.participants.get(1);
            pp = (PlayerParticipant) bcb.participants.get(0);

        } else {

            return;

        }

        ServerPlayerEntity player = pp.player;
        NPCTrainer trainer = tp.trainer;

        // TESTING PURPOSES
        trainer.getPokemonStorage().set(0, null);
        trainer.getPokemonStorage().add(PokemonBuilder.builder().species("Bulbasaur").build());
        //trainer.getPokemonStorage().sendCacheToPlayer(player);

        String worldName = player.getServerWorld().getWorld().toString().replace("ServerLevel[", "").replace("]", "");
        int x = trainer.getPosition().getX();
        int y = trainer.getPosition().getY();
        int z = trainer.getPosition().getZ();
        String location = worldName + "," + x + "," + y + "," + z;
        Gym gym = Gym.getFromNPCLocation(location);
        if (gym != null) {

            if (!gym.getPermissions().isEmpty()) {

                for (String p : gym.getPermissions()) {

                    if (!PermissionHandler.hasPermission(player, p)) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to challenge this Gym Leader!"), player.getUniqueID());
                        return;

                    }

                }

            }

            if (!npcMap.containsKey(player.getUniqueID())) {

                NPCTrainer newTrainer = new NPCTrainer(player.world);
                TrainerPartyStorage storage = newTrainer.getPokemonStorage();
                for (int i = 0; i < 6; i++) {

                    storage.set(i, null);

                }

                List<Pokemon> pokemonList = new ArrayList<>();
                Pokemon bulbasaur = PokemonBuilder.builder().species("bulbasaur").build();
                Pokemon charmander = PokemonBuilder.builder().species("charmander").build();
                Pokemon squirtle = PokemonBuilder.builder().species("squirtle").build();
                pokemonList.add(bulbasaur);
                pokemonList.add(charmander);
                pokemonList.add(squirtle);

                for (Pokemon pokemon : pokemonList) {

                    storage.add(pokemon);

                }

                TrainerParticipant newTP = new TrainerParticipant(newTrainer, 1);
                BattleController newBCB = new BattleController(new BattleParticipant[]{newTP}, new BattleParticipant[]{pp}, new BattleRules());
                npcMap.put(player.getUniqueID(), newTP);
                BattleRegistry.registerBattle(newBCB);
                BattleRegistry.startBattle(new BattleParticipant[]{newTP}, new BattleParticipant[]{pp}, BattleType.SINGLE);

            } else {

                event.setCanceled(true);

            }

        }

    }

}
