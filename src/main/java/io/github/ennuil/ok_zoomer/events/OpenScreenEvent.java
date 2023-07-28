package io.github.ennuil.ok_zoomer.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import io.github.ennuil.ok_zoomer.commands.OkZoomerCommandScreen;
import io.github.ennuil.ok_zoomer.utils.ZoomUtils;
import net.minecraft.client.MinecraftClient;

public class OpenScreenEvent implements ClientTickEvents.EndTick {
	@Override
	public void onEndTick(MinecraftClient client) {
		if (ZoomUtils.shouldOpenCommandScreen()) {
			client.setScreen(new OkZoomerCommandScreen());
			ZoomUtils.setOpenCommandScreen(false);
		}
	}
}
