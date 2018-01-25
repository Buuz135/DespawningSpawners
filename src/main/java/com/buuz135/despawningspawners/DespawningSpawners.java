package com.buuz135.despawningspawners;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.HashMap;

@Mod(modid = "despawningspawners", name = "Despawning Spawners", version = "1.0")
public class DespawningSpawners {

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntitySpawn(LivingSpawnEvent.SpecialSpawn event) {
        BlockPos pos = getSpawnerinAABB(event);
        if (pos != null && Arrays.asList(DespawningConfig.spawnerBlocks).contains(event.getWorld().getBlockState(pos).getBlock().getRegistryName().toString())) {
            SpawnerSavedData data = (SpawnerSavedData) event.getWorld().getPerWorldStorage().getOrLoadData(SpawnerSavedData.class, SpawnerSavedData.NAME);
            if (data == null) {
                data = new SpawnerSavedData();
                event.getWorld().getPerWorldStorage().setData(SpawnerSavedData.NAME, data);
            }
            HashMap<BlockPos, Integer> spawners = data.getSpawners();
            spawners.put(pos, spawners.getOrDefault(pos, DespawningConfig.maxSpawnerSpawns) - 1);
            if (spawners.get(pos) < 0) {
                event.getWorld().destroyBlock(pos, true);
                spawners.remove(pos);
            }
            data.setSpawners(spawners);
            data.markDirty();
        }
    }

    public BlockPos getSpawnerinAABB(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getSpawner() != null) {
            return event.getSpawner().getSpawnerPosition();
        }
        return null;
    }


}
