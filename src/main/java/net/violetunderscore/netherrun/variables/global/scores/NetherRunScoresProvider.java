package net.violetunderscore.netherrun.variables.global.scores;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NetherRunScoresProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Capability<NetherRunScores> NETHERRUN_SCORES = CapabilityManager.get(new CapabilityToken<NetherRunScores>() {

    });

    private NetherRunScores scores = null;
    private final LazyOptional<NetherRunScores> optional = LazyOptional.of(this::createNetherRunScores);

    private NetherRunScores createNetherRunScores() {
        if (this.scores == null) {
            this.scores = new NetherRunScores();
        }

        return this.scores;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == NETHERRUN_SCORES) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.scores.saveNBTData(nbt);
        LOGGER.info("[Provider] serializeNBT is Saving data: " + nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.scores.loadNBTData(nbt);
        LOGGER.info("[Provider] deserializeNBT is Loading data: " + nbt);
    }
}
