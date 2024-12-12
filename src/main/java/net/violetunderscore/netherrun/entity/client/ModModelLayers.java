package net.violetunderscore.netherrun.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.resources.ResourceLocation;
import net.violetunderscore.netherrun.NetherRun;

public class ModModelLayers {
    public static final ModelLayerLocation NETHERRUN_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(NetherRun.MODID, "boat/netherrun"), "main"
    );
    public static final ModelLayerLocation NETHERRUN_CHEST_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(NetherRun.MODID, "chest_boat/netherrun"), "main"
    );
}
