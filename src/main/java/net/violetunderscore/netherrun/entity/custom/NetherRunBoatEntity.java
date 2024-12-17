package net.violetunderscore.netherrun.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.violetunderscore.netherrun.entity.ModEntities;
import net.violetunderscore.netherrun.item.ModItems;

import java.util.function.IntFunction;

public class NetherRunBoatEntity extends Boat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
    public NetherRunBoatEntity(EntityType<? extends Boat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public NetherRunBoatEntity(Level level, double pX, double pY, double pZ) {
        this(ModEntities.NETHERRUN_BOAT.get(), level);
        this.setPos(pX, pY, pZ);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else if (!this.level().isClientSide && !this.isRemoved()) {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            if (pSource.getMsgId().equals("lava")) {
                this.setDamage(this.getDamage() + pAmount * 5.0F);
            }
            else {
                this.setDamage(this.getDamage() + pAmount * 10.0F);
            }
            this.markHurt();
            this.gameEvent(GameEvent.ENTITY_DAMAGE, pSource.getEntity());
            boolean flag = pSource.getEntity() instanceof Player && ((Player)pSource.getEntity()).getAbilities().instabuild;
            if (flag || this.getDamage() > 20.0F) {
                if (!flag && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.destroy(pSource);
                }

                this.discard();
            }

            return true;
        } else {
            return true;
        }
    }

    @Override
    public Item getDropItem() {
        return switch (getModVariant()) {
            case NETHERRUN -> ModItems.NETHERRUN_BOAT.get();
        };
    }

    public void setVariant(Type pVariant) {
        this.entityData.set(DATA_ID_TYPE, pVariant.ordinal());
    }

    public Type getModVariant() {
        return Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, Type.NETHERRUN.ordinal());
    }

    protected void addAditionalSaveData(CompoundTag pCompound) {
        pCompound.putString("Type", this.getModVariant().getSerializedName());
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Type", 0)) {
            this.setVariant(Type.byName(pCompound.getString("Type")));
        }
    }


    public static enum Type implements StringRepresentable {
        NETHERRUN(Blocks.NETHERRACK, "netherrun");

        private final String name;
        private final Block planks;
        public static final StringRepresentable.EnumCodec<NetherRunBoatEntity.Type> CODEC = StringRepresentable.fromEnum(NetherRunBoatEntity.Type::values);
        private static final IntFunction<NetherRunBoatEntity.Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        private Type(Block pPlanks, String pName) {
            this.name = pName;
            this.planks = pPlanks;
        }

        public String getSerializedName() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        public Block getPlanks() {
            return this.planks;
        }

        public String toString() {
            return this.name;
        }

        public static NetherRunBoatEntity.Type byId(int pId) {
            return (NetherRunBoatEntity.Type)BY_ID.apply(pId);
        }

        public static NetherRunBoatEntity.Type byName(String pName) {
            return (NetherRunBoatEntity.Type)CODEC.byName(pName, NETHERRUN);
        }
    }
}
