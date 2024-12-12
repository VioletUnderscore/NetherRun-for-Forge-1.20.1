package net.violetunderscore.netherrun.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.violetunderscore.netherrun.entity.ModEntities;
import net.violetunderscore.netherrun.item.ModItems;

public class NetherRunChestBoatEntity extends ChestBoat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    public NetherRunChestBoatEntity(EntityType<? extends ChestBoat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public NetherRunChestBoatEntity(Level pLevel, double pX, double pY, double pZ) {
        this(ModEntities.NETHERRUN_CHEST_BOAT.get(), pLevel);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }

    @Override
    public Item getDropItem() {
        switch (getModVariant()) {
            case NETHERRUN -> {
                return ModItems.NETHERRUN_CHEST_BOAT.get();
            }
        }
        return super.getDropItem();
    }

    public void setVariant(NetherRunBoatEntity.Type pVariant) {
        this.entityData.set(DATA_ID_TYPE, pVariant.ordinal());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, NetherRunBoatEntity.Type.NETHERRUN.ordinal());
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putString("Type", this.getModVariant().getSerializedName());
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Type", 8)) {
            this.setVariant(NetherRunBoatEntity.Type.byName(pCompound.getString("Type")));
        }
    }

    public NetherRunBoatEntity.Type getModVariant() {
        return NetherRunBoatEntity.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }
}
