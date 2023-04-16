package com.lypaka.bettergyms.GUIs;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.ItemStackBuilder;
import net.minecraft.item.ItemStack;

public class CommonButtons {

    public static Button getBorderButton (String id) {

        ItemStack item = ItemStackBuilder.buildFromStringID(id);
        item.setDisplayName(FancyText.getFormattedText(""));
        return GooeyButton.builder().display(item).build();

    }

}
