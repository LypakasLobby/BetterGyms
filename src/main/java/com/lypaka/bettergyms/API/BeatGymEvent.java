package com.lypaka.bettergyms.API;

import com.lypaka.bettergyms.Gyms.Gym;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public class BeatGymEvent extends Event {

    private final ServerPlayerEntity player;
    private final Gym gym;

    public BeatGymEvent (ServerPlayerEntity player, Gym gym) {

        this.player = player;
        this.gym = gym;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Gym getGym() {

        return this.gym;

    }

}
