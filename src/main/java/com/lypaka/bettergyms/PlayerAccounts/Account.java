package com.lypaka.bettergyms.PlayerAccounts;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettergyms.BetterGyms;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;

public class Account {

    private final ServerPlayerEntity player;
    private List<String> beatenGyms;

    public Account (ServerPlayerEntity player) {

        this.player = player;

    }

    public void load() throws ObjectMappingException {

        beatenGyms = new ArrayList<>(BetterGyms.playerConfigManager.getPlayerConfigNode(this.player.getUniqueID(), "Beaten-Gyms").getList(TypeToken.of(String.class)));
        BetterGyms.accountMap.put(this.player.getUniqueID(), this);

    }

    public List<String> getBeatenGyms() {

        return this.beatenGyms;

    }

    public void addGym (String gym) {

        this.beatenGyms.add(gym);

    }

    public void clearGyms() {

        this.beatenGyms = new ArrayList<>();

    }

    public void clearGym (String gym) {

        this.beatenGyms.removeIf(entry -> entry.equalsIgnoreCase(gym));

    }

    public void save() {

        BetterGyms.playerConfigManager.getPlayerConfigNode(this.player.getUniqueID(), "Beaten-Gyms").setValue(this.beatenGyms);
        BetterGyms.playerConfigManager.savePlayer(this.player.getUniqueID());

    }

}
