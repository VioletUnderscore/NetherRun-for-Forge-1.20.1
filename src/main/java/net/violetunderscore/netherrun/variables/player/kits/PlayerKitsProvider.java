package net.violetunderscore.netherrun.variables.player.kits;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerKitsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerKits> PLAYER_KITS = CapabilityManager.get(new CapabilityToken<PlayerKits>() {

    });

    private PlayerKits scores = null;
    private final LazyOptional<PlayerKits> optional = LazyOptional.of(this::createPlayerKits);

    private PlayerKits createPlayerKits() {
        if (this.scores == null) {
            this.scores = new PlayerKits();
        }

        return this.scores;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == PLAYER_KITS) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerKits().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerKits().loadNBTData(nbt);
    }
}
