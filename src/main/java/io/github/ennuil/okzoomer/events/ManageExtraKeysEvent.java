package io.github.ennuil.okzoomer.events;

import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

import io.github.ennuil.okzoomer.key_binds.ZoomKeyBinds;
import io.github.ennuil.okzoomer.packets.ZoomPackets;
import io.github.ennuil.okzoomer.utils.ZoomUtils;
import net.minecraft.client.MinecraftClient;

// This event manages the extra keybinds' behavior
public class ManageExtraKeysEvent implements ClientTickEvents.End {
	@Override
	public void endClientTick(MinecraftClient client) {
		if (!ZoomKeyBinds.areExtraKeyBindsEnabled()) return;
		if (ZoomPackets.getDisableZoomScrolling()) return;

		if (ZoomKeyBinds.DECREASE_ZOOM_KEY.isPressed() && !ZoomKeyBinds.INCREASE_ZOOM_KEY.isPressed()) {
			ZoomUtils.changeZoomDivisor(false);
		}

		if (ZoomKeyBinds.INCREASE_ZOOM_KEY.isPressed() && !ZoomKeyBinds.DECREASE_ZOOM_KEY.isPressed()) {
			ZoomUtils.changeZoomDivisor(true);
		}

		if (ZoomKeyBinds.RESET_ZOOM_KEY.isPressed()) {
			ZoomUtils.resetZoomDivisor(true);
		}
	}
}
