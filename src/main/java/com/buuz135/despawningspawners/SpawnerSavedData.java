package com.buuz135.despawningspawners;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;

@Deprecated
public class SpawnerSavedData extends WorldSavedData {

    public static final String NAME = "SPAWNERS_DATA";

    private HashMap<BlockPos, Integer> spawners;

    public SpawnerSavedData(String name) {
        super(name);
        spawners = new HashMap<>();
    }

    public SpawnerSavedData() {
        super(NAME);
        spawners = new HashMap<>();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        spawners = new HashMap<>();
        if (nbt.hasKey(NAME))
            nbt.getCompoundTag(NAME).getKeySet().forEach(s -> spawners.put(BlockPos.fromLong(Long.parseLong(s)), nbt.getCompoundTag(NAME).getInteger(s)));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound c = new NBTTagCompound();
        spawners.entrySet().forEach(blockPosIntegerEntry -> c.setInteger(String.valueOf(blockPosIntegerEntry.getKey().toLong()), blockPosIntegerEntry.getValue()));
        compound.setTag(NAME, c);
        return compound;
    }

    public HashMap<BlockPos, Integer> getSpawners() {
        return spawners;
    }

    public void setSpawners(HashMap<BlockPos, Integer> spawners) {
        this.spawners = spawners;
    }
}

