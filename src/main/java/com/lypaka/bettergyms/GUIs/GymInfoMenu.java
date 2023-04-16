package com.lypaka.bettergyms.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.reflect.TypeToken;
import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.ConfigGetters;
import com.lypaka.bettergyms.Gyms.Gym;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import com.lypaka.lypakautils.JoinListener;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GymInfoMenu {

    public static void open (ServerPlayerEntity player) {

        ChestTemplate template = ChestTemplate.builder(ConfigGetters.infoRows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString(ConfigGetters.infoName))
                .build();

        String[] border = ConfigGetters.infoBorderSlots.split(", ");
        int[] borderSlots = new int[border.length];
        List<Integer> usedSlots = new ArrayList<>();
        List<Integer> remainingSlots = new ArrayList<>();
        for (int i = 0; i < border.length; i++) {

            borderSlots[i] = Integer.parseInt(border[i]);
            usedSlots.add(borderSlots[i]);

        }
        int totalSlots = 9 * ConfigGetters.infoRows;
        for (int i = 0; i < totalSlots; i++) {

            if (!usedSlots.contains(i)) {

                remainingSlots.add(i);

            }

        }
        String borderID = ConfigGetters.infoBorderID;
        for (int i : borderSlots) {

            page.getTemplate().getSlot(i).setButton(CommonButtons.getBorderButton(borderID));

        }
        int index = 0;
        for (int i : remainingSlots) {

            try {

                Gym gym = Gym.getFromName(ConfigGetters.gymNames.get(index)); // this is dumb, but it will enforce it to be in the same order as defined in the config
                page.getTemplate().getSlot(i).setButton(getGymButton(gym));

            } catch (IndexOutOfBoundsException | ObjectMappingException er) {

                break;

            }
            index++;

        }

        UIManager.openUIForcefully(player, page);

    }

    private static Button getGymButton (Gym gym) throws ObjectMappingException {

        String name = gym.getName();
        String icon = gym.getGymDisplayIcon();
        String location = gym.getGymLocation();
        String theme = gym.getTheme();
        ItemStack item = ItemStackBuilder.buildFromStringID(icon);

        item.setDisplayName(FancyText.getFormattedText("&e" + name));
        ListNBT lore = new ListNBT();
        if (!BetterGyms.gymConfigManager.get(name).getConfigNode(0, "Gym-Info-Lore").isVirtual()) {

            List<String> setLore = BetterGyms.gymConfigManager.get(name).getConfigNode(0, "Gym-Info-Lore").getList(TypeToken.of(String.class));
            for (String s : setLore) {

                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(s))));

            }

        } else {

            lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&aLocation: &e" + location))));
            lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&aTheme: &e" + theme))));
            if (!gym.getPlayerUUID().equalsIgnoreCase("")) {

                String leaderName = "Unknown";
                if (ConfigGetters.uuidStorage.containsKey(gym.getPlayerUUID())) {

                    leaderName = ConfigGetters.uuidStorage.get(gym.getPlayerUUID());

                }
                String status;
                if (!leaderName.equalsIgnoreCase("Unknown")) {

                    try {

                        ServerPlayerEntity leader = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(leaderName);
                        boolean online = false;
                        for (Map.Entry<UUID, ServerPlayerEntity> entry : JoinListener.playerMap.entrySet()) {

                            if (entry.getValue().getName().getString().equalsIgnoreCase(leader.getName().getString())) {

                                online = true;
                                break;

                            }

                        }
                        status = online ? "&eOnline" : "&cOffline";

                    } catch (NullPointerException er) {

                        BetterGyms.logger.warn("Couldn't detect a player name for the provided UUID: " + gym.getPlayerUUID());
                        status = "&eUnknown";

                    }

                } else {

                    status = "&eUnknown";

                }
                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&aLeader: &e" + leaderName))));
                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&aLeader is currently: " + status + "&a!"))));

            } else {

                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&aDoes not have a player Gym Leader"))));

            }

        }
        item.getOrCreateChildTag("display").put("Lore", lore);
        return GooeyButton.builder().display(item).build();

    }

}
