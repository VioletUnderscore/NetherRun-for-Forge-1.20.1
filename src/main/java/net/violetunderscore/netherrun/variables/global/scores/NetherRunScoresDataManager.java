package net.violetunderscore.netherrun.variables.global.scores;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class NetherRunScoresDataManager {
    private static final String DATA_NAME = "nether_run_scores";

    public static NetherRunScoresData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                NetherRunScoresData::load,
                NetherRunScoresData::new,
                DATA_NAME
        );
    }

    public static void save(ServerLevel level) {
        get(level).setDirty();
    }
}
