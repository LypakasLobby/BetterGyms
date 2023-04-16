package com.lypaka.bettergyms;

import com.lypaka.bettergyms.Gyms.Gym;
import com.lypaka.bettergyms.PlayerAccounts.Account;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.ConfigurationLoaders.PlayerConfigManager;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("bettergyms")
public class BetterGyms {

    public static final String MOD_ID = "bettergyms";
    public static final String MOD_NAME = "BetterGyms";
    public static final Logger logger = LogManager.getLogger();
    public static BasicConfigManager configManager;
    public static Map<String, BasicConfigManager> gymConfigManager = new HashMap<>();
    public static PlayerConfigManager playerConfigManager;
    public static List<Gym> gyms = new ArrayList<>();
    public static Map<UUID, Account> accountMap = new HashMap<>();

    public BetterGyms() throws IOException, ObjectMappingException {

        Path dir = ConfigUtils.checkDir(Paths.get("./config/bettergyms"));
        String[] files = new String[]{"bettergyms.conf", "storage.conf"};
        configManager = new BasicConfigManager(files, dir, BetterGyms.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        playerConfigManager = new PlayerConfigManager("account.conf", "player-accounts", dir, BetterGyms.class, MOD_NAME, MOD_ID, logger);
        playerConfigManager.init();
        ConfigGetters.load();
        String[] gymFiles = new String[]{"settings.conf"};
        for (String gymName : ConfigGetters.gymNames) {

            Path gymDir = ConfigUtils.checkDir(Paths.get("./config/bettergyms/gyms/" + gymName));
            BasicConfigManager bcm = new BasicConfigManager(gymFiles, gymDir, BetterGyms.class, MOD_NAME, MOD_ID, logger);
            bcm.init();
            gymConfigManager.put(gymName, bcm);

        }

    }

}
