package com.buuz135.despawningspawners;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "despawningspawners")
public class DespawningConfig {

    @Config.RangeInt(min = 0, max =  Integer.MAX_VALUE)
    @Config.Comment("Amount of spawns can a spawner do. It won't stop the remaining spawns of a spawner 'work'.")
    public static int maxSpawnerSpawns = 50;

    @Config.Comment("A list of blocks that are considered a block")
    public static String[] spawnerBlocks = new String[]{"minecraft:spawner"};

    @Mod.EventBusSubscriber(modid = "despawningspawners")
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("despawningspawners")) {
                ConfigManager.sync("despawningspawners", Config.Type.INSTANCE);
            }
        }
    }

}
