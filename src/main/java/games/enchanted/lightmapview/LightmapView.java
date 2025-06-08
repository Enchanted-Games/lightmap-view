package games.enchanted.lightmapview;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightmapView implements ClientModInitializer {
	public static final String MOD_ID = "eg_lightmap_view";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(
				ClientCommandManager.literal("eg_lightmap_view")
				.then(ClientCommandManager.argument("size", IntegerArgumentType.integer(16, 512))
					.executes(context -> {
						LightmapViewState.lightmapSize = IntegerArgumentType.getInteger(context, "size");
						LightmapViewState.itemAtlasEnabled = true;
						context.getSource().sendFeedback(Component.literal("Set lightmap viewer size to %s".formatted(LightmapViewState.lightmapSize)));
						return 1;
					})
				)
				.then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
					.executes(context -> {
						LightmapViewState.itemAtlasEnabled = BoolArgumentType.getBool(context, "enabled");
						context.getSource().sendFeedback(Component.literal(LightmapViewState.itemAtlasEnabled ? "Enabled lightmap viewer" : "Disabled lightmap viewer"));
						return 1;
					})
				)
			);
		});
	}
}