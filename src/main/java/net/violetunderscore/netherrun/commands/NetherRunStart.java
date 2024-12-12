package net.violetunderscore.netherrun.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class NetherRunStart {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("yousaid")
                        .then(Commands.argument("message", StringArgumentType.string())
                                .executes(NetherRunStart::execute))
        );
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        String message = StringArgumentType.getString(context, "message");
        context.getSource().sendSuccess(() -> Component.literal("You said: " + message), false);
        if (message.equals("game")) {

        }
        return 1;
    }
}


