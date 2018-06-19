package com.buuz135.despawningspawners;


import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpawnerStorage implements ICapabilitySerializable<NBTTagCompound> {

    @CapabilityInject(SpawnerStorage.class)
    public static Capability<SpawnerStorage> SPAWNER_CAPABILITY = null;
    private int amount;

    public SpawnerStorage(int amount) {
        this.amount = amount;
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(SpawnerStorage.class, new CapabilitySpawnerStorage(), () -> new SpawnerStorage(DespawningConfig.maxSpawnerSpawns));
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == SPAWNER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == SPAWNER_CAPABILITY ? (T) this : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("Amount", this.getAmount());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.setAmount(nbt.getInteger("Amount"));
    }

    private static class CapabilitySpawnerStorage implements Capability.IStorage<SpawnerStorage> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<SpawnerStorage> capability, SpawnerStorage instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("Amount", instance.getAmount());
            return nbt;
        }

        @Override
        public void readNBT(Capability<SpawnerStorage> capability, SpawnerStorage instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;
            instance.setAmount(tags.getInteger("Amount"));
        }
    }
}
