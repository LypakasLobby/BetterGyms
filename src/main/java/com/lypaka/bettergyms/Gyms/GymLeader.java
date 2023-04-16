package com.lypaka.bettergyms.Gyms;

public class GymLeader {

    private Gym gym;
    private String playerUUID;
    private String npcLocation;

    public GymLeader (Gym gym, String playerUUID, String npcLocation) {

        this.gym = gym;
        this.playerUUID = playerUUID;
        this.npcLocation = npcLocation;

    }

    public GymLeader reload (Gym gym, String playerUUID, String npcLocation) {

        this.gym = gym;
        this.playerUUID = playerUUID;
        this.npcLocation = npcLocation;
        return this;

    }

    public Gym getGym() {

        return this.gym;

    }

    public String getPlayerUUID() {

        return this.playerUUID;

    }

    public String getNPCLocation() {

        return this.npcLocation;

    }

}
