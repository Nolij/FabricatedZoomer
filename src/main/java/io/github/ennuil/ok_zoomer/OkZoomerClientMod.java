package io.github.ennuil.ok_zoomer;

import io.github.ennuil.ok_zoomer.events.*;
import net.fabricmc.api.ClientModInitializer;

import io.github.ennuil.ok_zoomer.config.OkZoomerConfigManager;
import io.github.ennuil.ok_zoomer.key_binds.ZoomKeyBinds;
import io.github.ennuil.ok_zoomer.packets.ZoomPackets;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

// This class is responsible for registering the commands and packets
public class OkZoomerClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Initialize the config
		new OkZoomerConfigManager();

		// Register all the key binds
		KeyBindingHelper.registerKeyBinding(ZoomKeyBinds.ZOOM_KEY);
		if (ZoomKeyBinds.areExtraKeyBindsEnabled()) {
			KeyBindingHelper.registerKeyBinding(ZoomKeyBinds.DECREASE_ZOOM_KEY);
			KeyBindingHelper.registerKeyBinding(ZoomKeyBinds.INCREASE_ZOOM_KEY);
			KeyBindingHelper.registerKeyBinding(ZoomKeyBinds.RESET_ZOOM_KEY);
		}

		// Register the zoom-controlling packets
		ZoomPackets.registerPackets();

		// Register events
		ClientLifecycleEvents.CLIENT_STARTED.register(new ApplyLoadOnceOptionsEvent());
		ClientTickEvents.END_CLIENT_TICK.register(new ManageExtraKeysEvent());
		ClientTickEvents.END_CLIENT_TICK.register(new ManageZoomEvent());
		ClientTickEvents.END_CLIENT_TICK.register(new OpenScreenEvent());
		ClientCommandRegistrationCallback.EVENT.register(new RegisterCommands());
	}
}
