package net.violetunderscore.netherrun.client.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class NetherRunKeybinds {
    public static final KeyMapping NETHERRUN_TELEPORT = new KeyMapping(
            "key.netherrun.teleport",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.netherrun"
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(NETHERRUN_TELEPORT);
    }
}
