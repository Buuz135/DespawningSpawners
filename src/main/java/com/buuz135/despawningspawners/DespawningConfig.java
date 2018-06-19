package com.buuz135.despawningspawners;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "despawningspawners")
public class DespawningConfig {

    @Config.RangeInt(min = 0, max = Integer.MAX_VALUE)
    @Config.Comment("Amount of spawns can a spawner do. It won't stop the remaining spawns of a spawner 'work'.")
    public static int maxSpawnerSpawns = 50;

    @Config.Comment("NO LONGER VALID!! A list of blocks that are considered a spawner.")
    public static String[] spawnerBlocks = new String[]{"minecraft:mob_spawner"};

    @Config.Comment("If enabled the spawners will spawn particles depenending how many spawns have left. (GREEN > 50%; YELLOW < 50%; RED < 10%).")
    public static boolean displaySpawnerParticle = true;

    @Config.Comment("The amount of particles that the spawner will spawn.")
    public static int amountOfParticles = 50;

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
