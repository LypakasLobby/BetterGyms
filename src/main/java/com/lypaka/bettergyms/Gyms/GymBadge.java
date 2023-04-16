package com.lypaka.bettergyms.Gyms;

import com.google.common.reflect.TypeToken;
import com.lypaka.bettergyms.BetterGyms;
import com.lypaka.bettergyms.Gyms.Gym;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;

public class GymBadge {

    public static ItemStack buildGymBadge (Gym gym) throws ObjectMappingException {

        BasicConfigManager configManager = BetterGyms.gymConfigManager.get(gym.getName());
        String name = configManager.getConfigNode(0, "Badge", "Display-Name").getString();
        String id = configManager.getConfigNode(0, "Badge", "ID").getString();
        List<String> loreStrings = configManager.getConfigNode(0, "Badge", "Lore").getList(TypeToken.of(String.class));
        ListNBT lore = new ListNBT();
        ItemStack badge = ItemStackBuilder.buildFromStringID(id);

        badge.setDisplayName(FancyText.getFormattedText(name));
        if (!loreStrings.isEmpty()) {

            for (String s : loreStrings) {

                lore.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(FancyText.getFormattedText(s))));

            }

            badge.getChildTag("display").put("Lore", lore);

        }

        return badge;

    }

}
