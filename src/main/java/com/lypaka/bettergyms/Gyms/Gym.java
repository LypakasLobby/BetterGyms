package com.lypaka.bettergyms.Gyms;

import com.lypaka.bettergyms.BetterGyms;

import java.util.List;

public class Gym {

    private final String name;
    private String gymDisplayIcon;
    private String gymLocation;
    private int range;
    private String theme;
    private List<String> permissions;
    private String npcLocation;
    private String playerUUID;
    private List<String> rewards;
    private GymLeader gymLeader;

    public Gym (String name, String gymDisplayIcon, String gymLocation, int range, String theme, List<String> permissions, String npcLocation, String playerUUID, List<String> rewards) {

        this.name = name;
        this.gymDisplayIcon = gymDisplayIcon;
        this.gymLocation = gymLocation;
        this.range = range;
        this.theme = theme;
        this.permissions = permissions;
        this.npcLocation = npcLocation;
        this.playerUUID = playerUUID;
        this.rewards = rewards;
        this.gymLeader = new GymLeader(this, this.playerUUID, this.npcLocation);

    }

    public Gym reload (String gymDisplayIcon, String gymLocation, int range, String theme, List<String> permissions, String npcLocation , String playerUUID, List<String> rewards) {

        this.gymDisplayIcon = gymDisplayIcon;
        this.gymLocation = gymLocation;
        this.range = range;
        this.theme = theme;
        this.permissions = permissions;
        this.npcLocation = npcLocation;
        this.playerUUID = playerUUID;
        this.rewards = rewards;
        this.gymLeader = this.gymLeader.reload(this, this.playerUUID, this.npcLocation);
        return this;

    }

    public String getName() {

        return this.name;

    }

    public String getGymDisplayIcon() {

        return this.gymDisplayIcon;

    }

    public String getGymLocation() {

        return this.gymLocation;

    }

    public int getRange() {

        return this.range;

    }

    public String getTheme() {

        return this.theme;

    }

    public List<String> getPermissions() {

        return this.permissions;

    }


    public String getNPCLocation() {

        return this.npcLocation;

    }


    public String getPlayerUUID() {

        return this.playerUUID;

    }

    public List<String> getRewards() {

        return this.rewards;

    }

    public GymLeader getGymLeader() {

        return gymLeader;

    }

    public static Gym getFromName (String name) {

        Gym gym = null;
        for (Gym g : BetterGyms.gyms) {

            if (g.getName().equalsIgnoreCase(name)) {

                gym = g;
                break;

            }

        }

        return gym;

    }

    public static Gym getFromNPCLocation (String location) {

        Gym gym = null;
        for (Gym g : BetterGyms.gyms) {

            if (g.getNPCLocation().equalsIgnoreCase(location)) {

                gym = g;
                break;

            }

        }

        return gym;

    }

    public static Gym getFromPlayerLeader (String playerUUID) {

        Gym gym = null;
        for (Gym g : BetterGyms.gyms) {

            if (g.getPlayerUUID().equalsIgnoreCase(playerUUID)) {

                gym = g;
                break;

            }

        }

        return gym;

    }

}
