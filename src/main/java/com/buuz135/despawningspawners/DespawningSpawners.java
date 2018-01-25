package com.buuz135.despawningspawners;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

@Mod(modid = "despawningspawners", name = "Despawning Spawners", version = "1.1", dependencies = "required:forge@[14.23.1.2596,);")
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
            int amount = spawners.getOrDefault(pos, DespawningConfig.maxSpawnerSpawns) - 1;
            if (DespawningConfig.displaySpawnerParticle && event.getWorld() instanceof WorldServer){
                WorldServer worldServer = (WorldServer) event.getWorld();
                Color color = Color.GREEN;
                if (DespawningConfig.maxSpawnerSpawns * 0.5 >= amount) color = Color.YELLOW;
                if (DespawningConfig.maxSpawnerSpawns * 0.1 >= amount) color = Color.RED;
                Random random = worldServer.rand;
                for (int i = 0; i < DespawningConfig.amountOfParticles; ++i){
                    worldServer.spawnParticle(EnumParticleTypes.REDSTONE,true, pos.getX()+1.5-random.nextDouble()*2, pos.getY()+1.5-random.nextDouble()*2, pos.getZ()+1.5-random.nextDouble()*2, 0, (color.getRed()/255D)-1,color.getGreen()/255d, color.getBlue()/255d,1D);
                }
            }
            spawners.put(pos, amount);
            if (spawners.get(pos) <= 0) {
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
