package games.enchanted.lightmapview;

import games.enchanted.lightmapview.commands.PreviewCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightmapView implements ClientModInitializer {
	public static final String MOD_ID = "eg_lightmap_view";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(PreviewCommand::register);
	}
}