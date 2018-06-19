package com.buuz135.despawningspawners;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Random;

@Mod(modid = "despawningspawners", name = "Despawning Spawners", version = "1.1", dependencies = "required:forge@[14.23.1.2596,);")
public class DespawningSpawners {

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        SpawnerStorage.register();
    }

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<TileEntity> event) {
        if (event.getObject() instanceof TileEntityMobSpawner) {
            event.addCapability(new ResourceLocation("despawningspawners", "spawner_storage"), new SpawnerStorage(DespawningConfig.maxSpawnerSpawns));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onEntitySpawn(LivingSpawnEvent.SpecialSpawn event) {
        BlockPos pos = getSpawner(event);
        if (pos != null) {
            TileEntity spawner = event.getWorld().getTileEntity(pos);
            if (spawner instanceof TileEntityMobSpawner && spawner.hasCapability(SpawnerStorage.SPAWNER_CAPABILITY, null)) {
                System.out.println(spawner.hasCapability(SpawnerStorage.SPAWNER_CAPABILITY, null));
                SpawnerStorage storage = spawner.getCapability(SpawnerStorage.SPAWNER_CAPABILITY, null);

                //Moving data from the old system
                SpawnerSavedData data = (SpawnerSavedData) event.getWorld().getPerWorldStorage().getOrLoadData(SpawnerSavedData.class, SpawnerSavedData.NAME);
                if (data != null && data.getSpawners().containsKey(pos)) {
                    storage.setAmount(data.getSpawners().get(pos));
                    data.getSpawners().remove(pos);
                }

                storage.setAmount(storage.getAmount() - 1);
                spawner.markDirty();

                if (DespawningConfig.displaySpawnerParticle && event.getWorld() instanceof WorldServer) {
                    WorldServer worldServer = (WorldServer) event.getWorld();
                    Color color = Color.GREEN;
                    if (DespawningConfig.maxSpawnerSpawns * 0.5 >= storage.getAmount()) color = Color.YELLOW;
                    if (DespawningConfig.maxSpawnerSpawns * 0.1 >= storage.getAmount()) color = Color.RED;
                    Random random = worldServer.rand;
                    for (int i = 0; i < DespawningConfig.amountOfParticles; ++i) {
                        worldServer.spawnParticle(EnumParticleTypes.REDSTONE, true, pos.getX() + 1.5 - random.nextDouble() * 2, pos.getY() + 1.5 - random.nextDouble() * 2, pos.getZ() + 1.5 - random.nextDouble() * 2, 0, (color.getRed() / 255D) - 1, color.getGreen() / 255d, color.getBlue() / 255d, 1D);
                    }
                }

                if (storage.getAmount() <= 0) {
                    event.getWorld().destroyBlock(pos, true);
                }
                System.out.println(storage.getAmount());
            }
        }


//        if (pos != null && Arrays.asList(DespawningConfig.spawnerBlocks).contains(event.getWorld().getBlockState(pos).getBlock().getRegistryName().toString())) {
//            SpawnerSavedData data = (SpawnerSavedData) event.getWorld().getPerWorldStorage().getOrLoadData(SpawnerSavedData.class, SpawnerSavedData.NAME);
//            if (data == null) {
//                data = new SpawnerSavedData();
//                event.getWorld().getPerWorldStorage().setData(SpawnerSavedData.NAME, data);
//            }
//            HashMap<BlockPos, Integer> spawners = data.getSpawners();
//            int amount = spawners.getOrDefault(pos, DespawningConfig.maxSpawnerSpawns) - 1;
//            if (DespawningConfig.displaySpawnerParticle && event.getWorld() instanceof WorldServer){
//                WorldServer worldServer = (WorldServer) event.getWorld();
//                Color color = Color.GREEN;
//                if (DespawningConfig.maxSpawnerSpawns * 0.5 >= amount) color = Color.YELLOW;
//                if (DespawningConfig.maxSpawnerSpawns * 0.1 >= amount) color = Color.RED;
//                Random random = worldServer.rand;
//                for (int i = 0; i < DespawningConfig.amountOfParticles; ++i){
//                    worldServer.spawnParticle(EnumParticleTypes.REDSTONE,true, pos.getX()+1.5-random.nextDouble()*2, pos.getY()+1.5-random.nextDouble()*2, pos.getZ()+1.5-random.nextDouble()*2, 0, (color.getRed()/255D)-1,color.getGreen()/255d, color.getBlue()/255d,1D);
//                }
//            }
//            spawners.put(pos, amount);
//            if (spawners.get(pos) <= 0) {
//                event.getWorld().destroyBlock(pos, true);
//                spawners.remove(pos);
//            }
//            data.setSpawners(spawners);
//            data.markDirty();
//        }
    }

    public BlockPos getSpawner(LivingSpawnEvent.SpecialSpawn event) {
        if (event.getSpawner() != null) {
            return event.getSpawner().getSpawnerPosition();
        }
        return null;
    }

}
