package com.lypaka.bettergyms;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static List<String> gymNames;
    public static String beatMessage;
    public static String joinMessage;
    public static String leaveMessage;
    public static String badgeCaseBorderID;
    public static int badgeCaseBorderMetadata;
    public static String badgeCaseBorderSlots;
    public static int badgeCaseRows;
    public static String badgeCaseName;
    public static String infoBorderID;
    public static int infoBorderMetadata;
    public static String infoBorderSlots;
    public static int infoRows;
    public static String infoName;
    public static Map<String, String> uuidStorage;

    public static void load() throws ObjectMappingException {

        gymNames = BetterGyms.configManager.getConfigNode(0, "Gyms").getList(TypeToken.of(String.class));
        beatMessage = BetterGyms.configManager.getConfigNode(0, "Messages", "Beat").getString();
        joinMessage = BetterGyms.configManager.getConfigNode(0, "Messages", "Join").getString();
        leaveMessage = BetterGyms.configManager.getConfigNode(0, "Messages", "Leave").getString();
        badgeCaseBorderID = BetterGyms.configManager.getConfigNode(0, "UIs", "Badgecase", "Border", "Item", "ID").getString();
        badgeCaseBorderMetadata = BetterGyms.configManager.getConfigNode(0, "UIs", "Badgecase", "Border", "Item", "Metadata").getInt();
        badgeCaseBorderSlots = BetterGyms.configManager.getConfigNode(0, "UIs", "Badgecase", "Border", "Slots").getString();
        badgeCaseRows = BetterGyms.configManager.getConfigNode(0, "UIs", "Badgecase", "Rows").getInt();
        badgeCaseName = BetterGyms.configManager.getConfigNode(0, "UIs", "Badgecase", "Title").getString();
        infoBorderID = BetterGyms.configManager.getConfigNode(0, "UIs", "Gym-Info", "Border", "Item", "ID").getString();
        infoBorderMetadata = BetterGyms.configManager.getConfigNode(0, "UIs", "Gym-Info", "Border", "Item", "Metadata").getInt();
        infoBorderSlots = BetterGyms.configManager.getConfigNode(0, "UIs", "Gym-Info", "Border", "Slots").getString();
        infoRows = BetterGyms.configManager.getConfigNode(0, "UIs", "Gym-Info", "Rows").getInt();
        infoName = BetterGyms.configManager.getConfigNode(0, "UIs", "Gym-Info", "Title").getString();

        uuidStorage = BetterGyms.configManager.getConfigNode(1, "UUIDs").getValue(new TypeToken<Map<String, String>>() {});

    }

}
