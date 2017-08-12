package com.buuz135.despawningspawners;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

@Mod(modid = "despawningspawners", name = "Despawning Spawners", version = "1.0")
public class DespawningSpawners {

    private static AxisAlignedBB box = new AxisAlignedBB(-5, -5, -5, 5, 5, 5);
    private static int MAX_SPAWNS;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        MAX_SPAWNS = config.getInt("maxSpawnerSpawns", Configuration.CATEGORY_GENERAL, 50, 0, Integer.MAX_VALUE, "Amount of spawns can a spawner do. It won't stop the remaining spawns of a spawner 'work'.");
        config.save();
    }

    @SubscribeEvent
    public void onEntitySpawn(LivingSpawnEvent.SpecialSpawn event) {
        BlockPos pos = getSpawnerinAABB(event.getWorld(), box.offset(event.getEntity().getPosition()));
        if (pos != null) {
            SpawnerSavedData data = (SpawnerSavedData) event.getWorld().getPerWorldStorage().getOrLoadData(SpawnerSavedData.class, SpawnerSavedData.NAME);
            if (data == null) {
                data = new SpawnerSavedData();
                event.getWorld().getPerWorldStorage().setData(SpawnerSavedData.NAME, data);
            }
            HashMap<BlockPos, Integer> spawners = data.getSpawners();
            spawners.put(pos, spawners.getOrDefault(pos, MAX_SPAWNS) - 1);
            if (spawners.get(pos) < 0) {
                event.getWorld().destroyBlock(pos, true);
                spawners.remove(pos);
            }
            data.setSpawners(spawners);
            data.markDirty();
        }
    }

    public BlockPos getSpawnerinAABB(World world, AxisAlignedBB axisAlignedBB) {
        for (double x = axisAlignedBB.minX; x < axisAlignedBB.maxX; ++x) {
            for (double z = axisAlignedBB.minZ; z < axisAlignedBB.maxZ; ++z) {
                for (double y = axisAlignedBB.minY; y < axisAlignedBB.maxY; ++y) {
                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Blocks.MOB_SPAWNER))
                        return new BlockPos(x, y, z);
                }
            }
        }
        return null;
    }


}
