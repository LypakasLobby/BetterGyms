package com.lypaka.bettergyms.Commands;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.ConfigGetters;
import com.lypaka.bettergyms.GUIs.CommonButtons;
import com.lypaka.bettergyms.Gyms.Gym;
import com.lypaka.bettergyms.Gyms.GymBadge;
import com.lypaka.bettergyms.PlayerAccounts.Account;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;

public class BadgecaseMenu {

    public static void open (ServerPlayerEntity player) throws ObjectMappingException {

        ChestTemplate template = ChestTemplate.builder(ConfigGetters.badgeCaseRows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString(ConfigGetters.badgeCaseName))
                .build();

        String[] border = ConfigGetters.badgeCaseBorderSlots.split(", ");
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
        String borderID = ConfigGetters.badgeCaseBorderID;
        for (int i : borderSlots) {

            page.getTemplate().getSlot(i).setButton(CommonButtons.getBorderButton(borderID));

        }
        int index = 0;
        Account account = BetterGyms.accountMap.get(player.getUniqueID());
        for (int i : remainingSlots) {

            try {

                Gym gym = Gym.getFromName(ConfigGetters.gymNames.get(index)); // this is dumb, but it will enforce it to be in the same order as defined in the config
                if (account.getBeatenGyms().contains(gym.getName())) {

                    page.getTemplate().getSlot(i).setButton(GooeyButton.builder().display(GymBadge.buildGymBadge(gym)).build());

                } else {

                    page.getTemplate().getSlot(i).setButton(getBarrierButton(gym));

                }

            } catch (IndexOutOfBoundsException er) {

                break;

            }
            index++;

        }

        UIManager.openUIForcefully(player, page);

    }

    private static Button getBarrierButton (Gym gym) {

        ItemStack barrier = new ItemStack(Items.BARRIER);
        barrier.setDisplayName(FancyText.getFormattedText("&c" + gym.getName()));
        ListNBT lore = new ListNBT();
        lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText("&eYou haven't beaten this Gym yet!"))));
        barrier.getOrCreateChildTag("display").put("Lore", lore);
        return GooeyButton.builder().display(barrier).build();

    }

}
