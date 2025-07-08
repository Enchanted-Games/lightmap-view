package games.enchanted.lightmapview.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import games.enchanted.lightmapview.TextureViewState;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;

public class PreviewCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext commandBuildContext) {
        dispatcher.register(ClientCommandManager.literal("eg_texture_viewer")
            .then(
                ClientCommandManager.literal("lightmap")
                .then(ClientCommandManager.argument("size", IntegerArgumentType.integer(16, 1024))
                    .executes((context -> {
                        setLightmapSize(context.getSource(), IntegerArgumentType.getInteger(context, "size"));
                        return 1;
                    }))
                )
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes((context -> {
                        setLightmapEnabled(context.getSource(), BoolArgumentType.getBool(context, "enabled"));
                        return 1;
                    }))
                )
            )
            .then(
                ClientCommandManager.literal("items_atlas")
                    .then(ClientCommandManager.argument("size", IntegerArgumentType.integer(16, 1024))
                        .executes((context -> {
                            setItemsAtlasSize(context.getSource(), IntegerArgumentType.getInteger(context, "size"));
                            return 1;
                        }))
                    )
                    .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                        .executes((context -> {
                            setItemsAtlasEnabled(context.getSource(), BoolArgumentType.getBool(context, "enabled"));
                            return 1;
                        }))
                    )
            )
        );
    }

    private static void sendMessage(FabricClientCommandSource source, Component message) {
        source.sendFeedback(Component.literal("[Texture Viewer]: ").append(message));
    }

    private static void setLightmapSize(FabricClientCommandSource source, int value) {
        TextureViewState.lightmapSize = value;
        TextureViewState.lightmapEnabled = true;
        sendMessage(source, Component.literal("Set lightmap viewer size to %s".formatted(TextureViewState.lightmapSize)));
    }

    private static void setLightmapEnabled(FabricClientCommandSource source, boolean value) {
        TextureViewState.lightmapEnabled = value;
        sendMessage(source, Component.literal(TextureViewState.lightmapEnabled ? "Enabled lightmap viewer" : "Disabled lightmap viewer"));
    }

    private static void setItemsAtlasSize(FabricClientCommandSource source, int value) {
        TextureViewState.itemsAtlasSize = value;
        TextureViewState.itemAtlasEnabled = true;
        sendMessage(source, Component.literal("Set items atlas viewer size to %s".formatted(TextureViewState.itemsAtlasSize)));
    }

    private static void setItemsAtlasEnabled(FabricClientCommandSource source, boolean value) {
        TextureViewState.itemAtlasEnabled = value;
        sendMessage(source, Component.literal(TextureViewState.itemAtlasEnabled ? "Enabled items atlas viewer" : "Disabled items atlas viewer"));
    }
}
